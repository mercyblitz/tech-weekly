# 2019.01.25「小马哥技术周报」- 第十八期《Apache Dubbo 微服务系列之 Dubbo  Spring Cloud 实现总结》



## 场景解读



Spring Cloud 是 Cloud Native Java 技术栈的解决方案（框架）



### Spring Cloud 服务调用（Service-To-Service calls）

客户端（REST）：Spring Cloud REST（HTTP）实现方式：

- `@LoadBalanced` RestTemplate
  - 面向 URL：${serviceName} + ${serviceURI}
- Spring Cloud OpenFeign : @FeignClient + Java Interfaces
  - ${serviceName} + 接口方法（绑定 ${serviceURI}）

> ${serviceName} : DNS ，使用应用为维度的 IP + Port 列表



服务端（REST）：Servlet 技术栈和 Reactive 技术栈

- Servlet：Spring WebMVC
- Reactive：
  - Spring WebFlux
  - Spring Web Reactive（Servlet 3.1）

#### 服务注册与发现

Spring Cloud Commons 抽象 API



### Dubbo 服务调用

#### 客户端 

Dubbo 二进制、本文协议

- 面向接口设计客户端调用
  - dubbo
  - REST
- ~~RestTemplate~~
- ~~OpenFeign~~

#### 服务端



#### 服务注册与发现

Dubbo Registry SPI



### Dubbo + Spring Cloud

客户端 Dubbo 二进制、本文协议

- 面向接口设计客户端调用
  - dubbo
  - REST
- RestTemplate
- OpenFeign



REST 协议暴露：

- JAX-RS（标准 Java REST API）
- Spring MVC 注解（Spring WebMVC 和 Spring WebFlux 公用，事实的标准）



#### 服务注册与发现

Dubbo Registry SPI + Spring Cloud Commons 抽象 API



- Dubbo Registry SPI
  - `org.apache.dubbo.registry.RegistryFactory`
  - `org.apache.dubbo.registry.Registry`
- Spring Cloud Commons API
  - Registration
    - DubboRegistration
    - NacosRegistration
    - EurekaRegistration
    - ZookeeperRegistration
    - ConsulRegistration



场景一： 传统 Dubbo  注册到 Nacos +Dubbo Spring Cloud 也注册到 Nacos

必须相同的 Nacos 服务命名规则：

`${CATEGORY}:${PROTCOL}:${INTERFACE_CLASS_NAME}:${VERSION}:${GROUP}`



Spring Cloud Commons 抽象 API

`${CATEGORY}:${PROTCOL}:${INTERFACE_CLASS_NAME}:${VERSION}:${GROUP}`



### Feign Contract 实现

- 默认 Feign Contract
  - Spring Cloud 不会运用
  - Dubbo 也不会运用
- JAX-RS (1 和 2 ) Feign Contract -> JAX-RS 请求映射元信息
- Spring Cloud OpenFeign Contract -> Spring WebMVC 请求映射元信息



#### 问题：

- Dubbo Registry API 如何适配不同的 Spring Cloud Registration 的实现

- Dubbo 服务接口作为 Spring Cloud 应用名称，与普通不同的应用名同级

  - Dubbo 服务接口：
    - providers:dubbo:org.springframework.cloud.alibaba.dubbo.service.EchoService:1.0.0
    - providers:rest:org.springframework.cloud.alibaba.dubbo.service.EchoService:1.0.0
    - consumers:dubbo:org.springframework.cloud.alibaba.dubbo.service.EchoService:1.0.0
  - Spring Cloud 应用：
    - spring-cloud-alibaba-dubbo

- Dubbo 服务端暴露 REST 和 Dubbo 协议

  - 已支持：客户端 Spring Cloud OpenFeign 或者 RestTemplate 直接调用

  - 待支持：客户端 Spring Cloud OpenFeign 走 Dubbo 协议

    - 服务端

      - 应用名称：`spring-cloud-alibaba-dubbo`

      - REST 服务

        - 请求映射：
          - URI: `/echo`
          - query string : `message=${message}`

      - Dubbo 服务

        - 协议：dubbo
        - 端口：12345
        - 接口：`org.springframework.cloud.alibaba.dubbo.service.EchoService#echo(String)`

      - 暴露动作

        - 当 Dubbo 服务暴露时，将 REST 请求映射信息推送到配置中心

    - 客户端

      - 调用应用：`spring-cloud-alibaba-dubbo`
      - OpenFeign 
        - 请求映射：
          - URI: `/echo`
          - query string : `message=${message}`
      - 订阅动作
        - 当应用启动时，去订阅调用应用的 Dubbo REST 请求映射信息
      - 匹配动作
        - 当调用应用的请求映射信息与 OpenFeign 接口定义的元信息如果匹配的话，可以替换为 dubbo 协议的调用

  - 待支持：客户端 Spring Cloud `@LoadBalanced` RestTemplate 走 Dubbo 协议

- Spring Cloud Feign 客户端如何控制调用非 REST 协议的Dubbo 服务端

  - dubbo
  - HTTP
  - Hession



#### TODO 

Dubbo 服务接口与 Spring Cloud 应用名称做隔离



## 功能演示



## 吐槽 Spring Cloud OpenFeign 实现设计

- `@FeignClient` 接口
  - 生成 Spring Bean
    - `org.springframework.cloud.openfeign.FeignClientFactoryBean`
  - spring-cloud-context 抽象：
    - 关联 `NamedContextFactory`
    - 生成一个 `ApplicationContext`
- `@EnableFeignClients`
  - `FeignClientsRegistrar`



## 介绍 Spring Cloud 官方实现中的特性缺失

- Spring Cloud 不支持配置推送
  - 间接方法：`spring-cloud-bus`
    - 依赖：
      - `spring-cloud-stream`
      - MQ 实现（RocketMQ）
  - 间接方式：`spring-cloud-config` + `spring-cloud-bus` + `spring-cloud-monitor`



## 为什么说 Spring Cloud 官方实现“能用但不成熟”

