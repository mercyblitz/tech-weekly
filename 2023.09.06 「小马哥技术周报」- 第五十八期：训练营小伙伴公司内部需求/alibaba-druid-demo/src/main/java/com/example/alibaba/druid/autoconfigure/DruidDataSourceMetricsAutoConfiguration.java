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
package com.example.alibaba.druid.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.example.alibaba.druid.actuate.metrics.DruidDataSourceMBeanMetrics;
import com.example.alibaba.druid.actuate.metrics.DruidDataSourceMetrics;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DruidPoolDataSource Metrics Auto-Configuration for Alibaba Druid
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(value = {
        DruidDataSource.class, // druid-core
        DruidDataSourceAutoConfigure.class // druid-spring-boot-starter
})
@ConditionalOnProperty(prefix = "druid.datasource.metadata", name = "profile", havingValue = "metrics")
@AutoConfigureAfter({
        DruidDataSourceAutoConfigure.class,
})
public class DruidDataSourceMetricsAutoConfiguration {


    @Bean
    public DruidDataSourceMetrics druidDataSourceMetrics(DataSource dataSource) {
        return new DruidDataSourceMetrics(dataSource);
    }

    @Bean
    public DruidDataSourceMBeanMetrics druidDataSourceMBeanMetrics() throws Throwable {
        return new DruidDataSourceMBeanMetrics();
    }
}
