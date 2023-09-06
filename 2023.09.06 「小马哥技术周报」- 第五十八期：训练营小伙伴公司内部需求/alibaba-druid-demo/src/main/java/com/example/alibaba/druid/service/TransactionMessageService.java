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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 交易消息服务
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Service
public class TransactionMessageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long addTransactionMessage(Long txId, Long userId, Long amount) {
        Long id = jdbcTemplate.execute((ConnectionCallback<Long>) connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO tx_messages(xid,user_id,amount) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, txId);
            ps.setLong(2, userId);
            ps.setLong(3, amount);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getLong(1) : null;
        });
        return id;
    }

    public boolean hasProcessedTransaction(Long txId, Long userId, Long amount) {
        return jdbcTemplate.execute("SELECT count(id) FROM tx_messages WHERE xid = ? AND user_id = ? AND amount = ?",
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setLong(1, txId);
                    ps.setLong(2, userId);
                    ps.setLong(3, amount);
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt(1) > 0 : false;
                });
    }
}
