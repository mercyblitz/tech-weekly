/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.alibaba.druid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 账户服务
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionMessageService transactionMessageService;

    public List<Map<String, Object>> getAllUsers() {
        try {
            // 切换读库
            // DataSourceType.READ.switchDataSource();
            return jdbcTemplate.execute(new StatementCallback<List>() {

                @Override
                public List doInStatement(Statement stmt) throws SQLException, DataAccessException {

                    ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");
                    List<Map<String, Object>> result = new ArrayList<>();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (resultSet.next()) {
                        Map<String, Object> rowMap = new HashMap<>(columnCount);
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnClassName = metaData.getColumnClassName(i);
                            Object columnValue = null;
                            switch (columnClassName) {
                                case "java.lang.String":
                                    columnValue = resultSet.getString(i);
                                    break;
                                case "java.lang.Long":
                                    columnValue = resultSet.getLong(i);
                                    break;
                                case "java.lang.Integer":
                                    columnValue = resultSet.getInt(i);
                                    break;
                            }
                            rowMap.put(columnName, columnValue);
                        }

                        result.add(rowMap);
                    }

                    return result;
                }
            });
        } finally {
            // resetDataSource();
        }
    }

    @Transactional
    public boolean updateAmount(Long txId, Long sellerId, Long buyerId, Long amount) {

        if (transactionMessageService.hasProcessedTransaction(txId, sellerId, amount)) {
            logger.warn("The transaction[id :{}] for seller[id : {}] has been processed", txId, sellerId);
            return false;
        }

        jdbcTemplate.execute("UPDATE users SET amt_sold=amt_sold + ? WHERE id=?",
                (PreparedStatementCallback<Void>) ps -> {
                    ps.setLong(1, amount);
                    ps.setLong(2, sellerId);
                    ps.executeUpdate();
                    return null;
                });

        jdbcTemplate.execute("UPDATE users SET amt_bought=amt_bought + ? WHERE id=?",
                (PreparedStatementCallback<Void>) ps -> {
                    ps.setLong(1, amount);
                    ps.setLong(2, buyerId);
                    ps.executeUpdate();
                    return null;
                });

        transactionMessageService.addTransactionMessage(txId, sellerId, amount);
        transactionMessageService.addTransactionMessage(txId, buyerId, amount);

        return true;
    }
}
