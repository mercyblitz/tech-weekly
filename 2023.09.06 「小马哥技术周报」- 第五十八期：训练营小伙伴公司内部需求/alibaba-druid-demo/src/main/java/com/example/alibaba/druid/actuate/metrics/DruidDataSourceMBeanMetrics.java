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

import com.alibaba.druid.pool.DruidDataSourceMBean;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 * Metrics for {@link DruidDataSourceMBean}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class DruidDataSourceMBeanMetrics implements MeterBinder {

    private static final String[] ATTRIBUTE_NAMES = {
            "ActivePeak",
            "MaxIdle",
            "MinIdle",
            "MaxActive",
            "InitialSize",
            "ResetCount",
    };

    private final MBeanServer mbeanServer;

    private final Set<ObjectName> objectNames;

    private volatile boolean bound = false;

    public DruidDataSourceMBeanMetrics() throws Throwable {
        this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
        this.objectNames = findObjectNames();
    }

    private Set<ObjectName> findObjectNames() throws Throwable {
        ObjectName nameToQuery = ObjectName.getInstance("com.alibaba.druid:type=DruidDataSource,id=*");
        Set<ObjectName> objectNames = mbeanServer.queryNames(nameToQuery, null);
        return objectNames;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        if (bound) {
            return;
        }

        for (ObjectName objectName : objectNames) {
            // ObjectName = com.alibaba.druid:type=DruidDataSource,id=my-druid-datasource
            // id = my-druid-datasource
            String id = objectName.getKeyProperty("id");
            // TODO attributeList 按照需要 bind to MeterRegistry
            for (String attributeName : ATTRIBUTE_NAMES) {
                Gauge.builder("druid.jmx." + id + ".jdbc.connections." + attributeName, () -> {
                            Object attribute = null;
                            try {
                                attribute = mbeanServer.getAttribute(objectName, attributeName);
                            } catch (Throwable e) {
                            }
                            return attribute instanceof Number ? (Number) attribute : null;
                        })
                        .register(registry);
            }
        }
    }
}
