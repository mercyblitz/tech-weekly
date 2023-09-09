<a name="dsfDn"></a>
# 议程安排
<a name="sT2WN"></a>
## Alibaba Druid 简介
<a name="LvBMP"></a>
### Druid 是什么？
Druid 是一个 JDBC 组件库，包含数据库连接池、SQL Parser 等组件, 被大量业务和技术产品使用或集成，经历过最严苛线上业务场景考验，是你值得信赖的技术产品。
<a name="ypF2g"></a>
### 项目地址
Github：[https://github.com/alibaba/druid](https://github.com/alibaba/druid)

<a name="I3sQL"></a>
### 个人评价
核心特性：

- 数据库连接池（DataSource Pool）
- 监控（Monitoring）
- SQL 解析（AST）
- JDBC 代理（拦截）
   - Connection
   - Statement/PreparedStatement/CallableStatement
   - ResultSet

<a name="tTIrg"></a>
### 关于 Alibaba Druid 可观测性现状
<a name="R2nRY"></a>
#### Alibaba Druid 日志
<a name="s0jIq"></a>
#### 统计监控过滤器 - StatFilter
<a name="PJB2P"></a>
#### JMX 整合

- 日志相关
   - LogFilterMBean
      - Log4j2FilterMBean
      - Log4jFilterMBean
      - CommonsLogFilterMBean
- StatFilter 相关
   - StatFilterMBean
- 统计相关
   - DruidDataSourceStatManagerMBean
   - DruidStatServiceMBean
   - JdbcConnectionStatMBean
   - JdbcDataSourceStatMBean
   - JdbcResultSetStatMBean
   - JdbcSqlStatMBean
   - JdbcStatementStatMBean
   - JdbcStatManagerMBean
   - JdbcTraceManagerMBean
- Wall 相关
   - WallConfigMBean
   - WallFilterMBean

<a name="OGUdI"></a>
## Alibaba Druid 整合 Prometheus + Grafana
<a name="qkXzx"></a>
### 需求分析
目前 Alibaba Druid 已支持特性：

- Alibaba Druid 日志
- 统计监控过滤器 - StatFilter
- JMX 整合

不支持特性：

- Spring Boot Actuator 
   - Metrics - Micrometer 整合（ Spring Boot 1.5+）
- 指标存储 - Prometheus 
- 监控展示 - Grafana

因此，Alibaba Druid 需要整合：

- （必须）Micrometer，通过 Micrometer Prometheus Registry 导出到 Prometheus，再通过 Grafana 展现
- （可选）Spring Boot Actuator 

<a name="CjkyC"></a>
### 功能实现
<a name="KWeOL"></a>
#### 简单方案：基于 Spring Boot 2.x+ DataSourcePoolMetadata 接口
<a name="ocHRE"></a>
##### 实现 DataSourcePoolMetadata 接口
```java
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

```
<a name="mxlNb"></a>
##### 装配 DataSourcePoolMetadata 实现 - DruidDataSourcePoolMetadata
```java
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
import com.example.alibaba.druid.metadata.DruidDataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link DataSourcePoolMetadata} Auto-Configuration for Alibaba Druid
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(value = {
        DruidDataSource.class, // druid-core
        DruidDataSourceAutoConfigure.class // druid-spring-boot-starter
})
@AutoConfigureAfter({
        DruidDataSourceAutoConfigure.class,
})
public class DruidDataSourceMetadataProviderAutoConfiguration {

    @Bean
    public DataSourcePoolMetadataProvider druidDataSourcePoolMetadataProvider() {
        return DruidDataSourcePoolMetadata::new;
    }
}

```

<a name="l1gNz"></a>
##### 自动装配 DruidDataSourceMetadataProviderAutoConfiguration
配置 `/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`：
```latex
com.example.alibaba.druid.autoconfigure.DruidDataSourceMetadataProviderAutoConfiguration
```


<a name="ZswJU"></a>
#### 常规方法：基于 Micrometer API 整合
<a name="FXWoQ"></a>
##### 实现 Druid 与 MeterBinder 整合
```java
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

```

<a name="aHhur"></a>
##### 装配 DataSourcePoolMetadata 实现 - DruidDataSourcePoolMetadata
```java
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
import com.example.alibaba.druid.actuate.metrics.DruidDataSourceMetrics;
import com.example.alibaba.druid.metadata.DruidDataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
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
@ConditionalOnProperty(prefix = "druid.datasource.metadata",name = "profile",havingValue = "metrics")
@AutoConfigureAfter({
        DruidDataSourceAutoConfigure.class,
})
public class DruidDataSourceMetricsAutoConfiguration {


    @Bean
    public DruidDataSourceMetrics druidDataSourceMetrics(DataSource dataSource) {
        return new DruidDataSourceMetrics(dataSource);
    }
}
```

<a name="EVw3A"></a>
##### 自动装配 DruidDataSourceMetricsAutoConfiguration
配置 `/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`：
```latex
com.example.alibaba.druid.autoconfigure.DruidDataSourceMetricsAutoConfiguration
```

<a name="Vt5uL"></a>
#### 困难方法：基于 JMX 桥接到 Micrometer
Alibaba Druid 自动将 DruidDataSource 作为 JMX MBean 来注册 MBeanServer：
```java
    public void registerMbean() {
        if (!mbeanRegistered) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    ObjectName objectName = DruidDataSourceStatManager.addDataSource(DruidDataSource.this,
                            DruidDataSource.this.name);

                    DruidDataSource.this.setObjectName(objectName);
                    DruidDataSource.this.mbeanRegistered = true;

                    return null;
                }
            });
        }
    }
```
执行 DruidDataSourceStatManager#addDataSource 方法：
```java
    public static synchronized ObjectName addDataSource(Object dataSource, String name) {
        final Map<Object, ObjectName> instances = getInstances();

        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        synchronized (instances) {
            if (instances.size() == 0) {
                try {
                    ObjectName objectName = new ObjectName(MBEAN_NAME);
                    if (!mbeanServer.isRegistered(objectName)) {
                        mbeanServer.registerMBean(instance, objectName);
                    }
                } catch (JMException ex) {
                    LOG.error("register mbean error", ex);
                }

                DruidStatService.registerMBean();
            }
        }

        ObjectName objectName = null;
        if (name != null) {
            try {
                objectName = new ObjectName("com.alibaba.druid:type=DruidDataSource,id=" + name);
                mbeanServer.registerMBean(dataSource, objectName);
            } catch (Throwable ex) {
                LOG.error("register mbean error", ex);
                objectName = null;
            }
        }

        if (objectName == null) {
            try {
                int id = System.identityHashCode(dataSource);
                objectName = new ObjectName("com.alibaba.druid:type=DruidDataSource,id=" + id);
                mbeanServer.registerMBean(dataSource, objectName);
            } catch (Throwable ex) {
                LOG.error("register mbean error", ex);
                objectName = null;
            }
        }

        instances.put(dataSource, objectName);
        return objectName;
    }
```

<a name="dhOvn"></a>
##### 基于 DruidDataSourceMBean 实现
```java
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

```
<a name="bCTcd"></a>
### 准备工作
<a name="wj1Fu"></a>
#### 环境准备
<a name="QlfAx"></a>
#### 项目准备


<a name="gFSf2"></a>
## 连麦讨论

