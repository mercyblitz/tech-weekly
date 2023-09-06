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
package com.example.alibaba.druid.metadata;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * {@link DataSourcePoolMetadata} for Alibaba Druid
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class DruidDataSourcePoolMetadata implements DataSourcePoolMetadata {

    private final DataSource dataSource; // DruidDataSourceWrapper -> unwrap

    private final DruidDataSource druidDataSource;

    public DruidDataSourcePoolMetadata(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.druidDataSource = dataSource.unwrap(DruidDataSource.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Float getUsage() {
        // FIXME
        return getActive() / (getMax() * 1.0f);
    }

    @Override
    public Integer getActive() {
        return druidDataSource.getActivePeak();
    }

    @Override
    public Integer getMax() {
        return druidDataSource.getMaxActive();
    }

    @Override
    public Integer getMin() {
        return druidDataSource.getInitialSize();
    }

    @Override
    public String getValidationQuery() {
        return druidDataSource.getValidationQuery();
    }

    @Override
    public Boolean getDefaultAutoCommit() {
        return druidDataSource.isDefaultAutoCommit();
    }
}
