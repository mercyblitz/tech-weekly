# 「小马哥技术周报」- 第二十九期  Apache Dubbo 设计与实现系列之Dubbo Spring Boot 工程





## 功能特性





### 自动装配



### Production-Ready 



## 目录结构



### [dubbo-spring-boot-actuator](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-actuator)

关注 Dubbo 在 Production-Ready 特性，Endpoints（JMX、Web） - Health



### [dubbo-spring-boot-autoconfigure](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-autoconfigure)

Dubbo 自动装配模块，针对 Spring Boot 2.x



### [dubbo-spring-boot-compatible](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-compatible)

Dubbo Spring Boot 1.x 和 2.0 兼容模块，被依赖（条件化）



#### Dubbo 核心自动装配类 - `DubboAutoConfiguration`

复用 Dubbo Spring 核心处理 

- `@Service` 处理类 - `ServiceAnnotationBeanPostProcessor`

- `@Reference` 处理类 - `ReferenceAnnotationBeanPostProcessor`



Spring Boot 松散绑定（Spring 不存在的）

```properties
dubbo.application.qos-enable = false
# RegistyConfig
dubbo.registry.name = nacos 
```



Dubbo Config 绑定



- dubbo-spring-boot-starter
  - dubbo-spring-boot-autoconfigure
    - dubbo-spring-boot-compatible 



| 自动装配类                              | Spring Boot 1.x | Spring Boot 2.x |
| --------------------------------------- | --------------- | --------------- |
| `DubboRelaxedBinding2AutoConfiguration` | X               | O               |
| `DubboRelaxedBindingAutoConfiguration`  | O               | X               |
|                                         |                 |                 |



Dubbo `@Service` 包路劲扫描

```properties
# base-packages 松散绑定
dubbo.scan.base-packages = com.acme.xxx
dubbo.scan.basePackages = com.acme.xxx
dubbo.scan.BASE_PACKAGES = com.acme.xxx
```





Dubbo Spring 模块

- Spring 3.1
- Spring Boot 1.x
- Spring Boot 2.x



- Dubbo Spring (内核)
  - Dubbo Spring Boot
    - Dubbo Spring Cloud







#### [dubbo-spring-boot-distribution](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-distribution)

Dubbo 二进制分发模块



### [dubbo-spring-boot-parent](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-parent)

Dubbo  Maven 依赖控制



### [dubbo-spring-boot-samples](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-samples)

Dubbo Spring Boot 示例



### [dubbo-spring-boot-starter](https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-starter)

Spring Boot 官方推荐模式 Starter 通常就依赖 autoconfigure（自动装配）



## 版本的支持





### Dubbo >= 2.7 - `org.apache.dubbo`



#### 2.7.3

Spring Boot 1.x 和 2.x





### Dubbo < 2.7 - `com.alibaba`



#### [0.2.1.RELEASE](https://github.com/apache/dubbo-spring-boot-project/tree/0.2.x)

Spring Boot 2.x + Dubbo 2.6.x



#### [0.1.2.RELEASE](https://github.com/apache/dubbo-spring-boot-project/tree/0.1.x)

Spring Boot 1.x + Dubbo 2.6.x



## Production-Ready 



通过 Endpoints 与外界交互

- JMX 
- REST



JMX

Spring JMX 模块

Spring Boot JMX

Spring Boot Actuator





Spring Boot 1.x

- Beans -> /beans
- Environment -> /env

Spring Boot 2.x

- Beans -> /actuator/beans
- Environment ->  /actuator/env



Dubbb

- Services -> /dubbo/services



@Endpoint



Spring MVC - Servlet 

Spring WebFlux

- Servlet
- Reactor + Netty



@RequestMapping

