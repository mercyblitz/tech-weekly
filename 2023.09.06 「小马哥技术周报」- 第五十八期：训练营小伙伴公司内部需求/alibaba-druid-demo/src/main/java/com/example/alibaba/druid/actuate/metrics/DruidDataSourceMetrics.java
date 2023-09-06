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
package com.example.alibaba.druid.actuate.metrics;

import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * Metrics for {@link DruidDataSource}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class DruidDataSourceMetrics implements MeterBinder {

    private final DataSource dataSource; // DruidDataSourceWrapper -> unwrap

    private final DruidDataSource druidDataSource;

    private volatile boolean bound = false;


    public DruidDataSourceMetrics(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.druidDataSource = dataSource.unwrap(DruidDataSource.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (bound) {
            return;
        }

        bindDataSource(registry, "active",
                "Current number of active connections that have been allocated from the data source.",
                DruidDataSource::getActivePeak);

        bindDataSource(registry, "max-idle", "The max number of established but idle connections.",
                DruidDataSource::getMaxIdle);

        bindDataSource(registry, "min-idle", "The min number of established but idle connections.",
                DruidDataSource::getMinIdle);

        bindDataSource(registry, "max",
                "Maximum number of active connections that can be allocated at the same time.",
                DruidDataSource::getMaxActive);
        bindDataSource(registry, "min", "Minimum number of idle connections in the pool.",
                DruidDataSource::getInitialSize);

        bindDataSource(registry, "reset-count", "Reset count of connections in the pool.",
                DruidDataSource::getResetCount);

        bound = true;
    }

    private <N extends Number> void bindDataSource(MeterRegistry registry, String metricName,
                                                   String description,
                                                   Function<DruidDataSource, N> function) {
        if (function.apply(this.druidDataSource) != null) {
            Gauge.builder("druid.jdbc.connections." + metricName, this.druidDataSource, (m) -> function.apply(m).doubleValue())
                    .description(description)
                    .register(registry);
        }
    }
}
