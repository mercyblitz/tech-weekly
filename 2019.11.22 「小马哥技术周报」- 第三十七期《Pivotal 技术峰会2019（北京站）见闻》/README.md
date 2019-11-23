# Pivotal 技术峰会2019（北京站）见闻



> 会议时间：2019 年 11 月 21 日（昨天）
>
> 会议地点：北京维景国际大酒店
>
> 会议详情：http://www.pivotal-china.com/html/summit_2019/index.html



## 整体感受



Cloud Computing

One - AWS

Others 

- Google
  - GO
  - Python
- Microsoft Azure
  - TypeScript
  - C#
- Aliyun
- Netflix

CNCF



### 商业议题



#### 微服务



#### 云原生



#### 圆桌会议



##### 铁科院信息化板块货运业务群

Spring Cloud Netflix

Eureka + Ribbon + OpenFeign



Spring Cloud Alibaba 的架构，Spring Cloud Alibaba 是 Spring Cloud 官方认证的解决方案

1. 贵公司在使用 Eureka 的集群规模，比如机器实例数量

   没有正面回答

2. 在使用 Eureka 过程中是否存在问题或者瓶颈

   没有正面回答

3. 是否考虑过其他 Spring Cloud 解决方案，比如 Spring Cloud Alibaba 或者 Spring Cloud Azure

   过去使用 Dubbo，Dubbo 2016 没有开源

继续补充：Spring Cloud Alibaba 已将 Spring Cloud 和 Dubbo 整合，利用 Dubbo 作为完整的 RPC 解决方案，同时，如果贵公司需要技术支持，我们可以提供无偿的服务



问题：Spring Cloud 如何应对 Service Mesh 的冲击



### 开源议题



#### Spring Cloud Gateway



#### Spring Cloud Stream



> 大多数程序员的编程习惯
>
> - 面向过程多于面向对象
>   - 多 if else 少多态
> - 串行编程多于并行/并发编程
>   - a -> b
>   - 并发：指令重排 ->  变量可见性
> - 面向对象多于面向模式
> - 面向对象多于面向函数
> - 面向函数多于面向 Reactive
> - 面向 Reactive 多于面向 Stream（分布式）



#### Spring RSocket



- request/response (stream of 1)

  类似于 Servlet#service(ServletRequest,ServletResponse);

  大多数情况是同步，也可能异步 Servlet 3.0 异步

- request/stream (finite stream of many)

  类似于流媒体

- fire-and-forget (no response)

  UDP 协议

- channel (bi-directional streams)

​      双向管道



Reactive 如果在客户端实现，那就是自娱自乐（手淫）

ReactiveX 或 Reactor

客户端调用客户端是同步（及时返回结果）

Reactive 如果是在服务端试下，那就是 Netty

Reactor、Proactor

BOSS（请求转发）、Worker（具体做事）



##  Spring Cloud 2020 RoadMap 

Spring Cloud 版本利用伦敦地铁站 Hoxton



Spring Cloud Commons 抽象 API，负载均衡利用 Netflix Ribbon，服务熔断是一个简单注解的处理



从 Hoxton 开始，负载均衡 + 服务熔断融合到 Spring Cloud Commons



### Spring Cloud 负载均衡 API - [LoadBalancer](https://cloud.spring.io/spring-cloud-static/spring-cloud-commons/2.2.0.RC2/reference/html/#spring-cloud-loadbalancer)



### Spring Cloud 服务熔断 API - [Circuit Breaker](https://cloud.spring.io/spring-cloud-static/spring-cloud-commons/2.2.0.RC2/reference/html/#spring-cloud-circuit-breaker)



### Spring Cloud 对 RSocket 支持

Spring RSocket 是通过 Reactor 包装



#### Spring Cloud 与 Service Mesh

Service Mesh 是相对来说不成熟，Broker 

Client -> 用户 Process -> MySQL CLI Agent（192.168.1.3）

------>  DB（MySQL：192.168.1.2）

Client2 -> 用户 Process2 -> MySQL CLI Agent（192.168.1.1）



DB Mesh



MQ


Message Mesh



Spring Cloud 会被削弱

- 服务注册与发现
- 负载均衡
- 服务熔断



Gateway  -> 服务器（Nginx(80) -> Tomcat(8080)）



Business Process -> Agent（注册）

Business Process（Consumer） <--> Service Center（Provider）

Business Process（Consumer） -> Agent <--> Service Center（Provider）



Performance Issue



前后端分离

客户端计算能力差，业务逻辑在服务端执行

HTTP（文本）-> CSS + JavaScript



富客户端 + 富服务器（硬件成本低）

页面渲染（服务，JSP、Velocity、Themeleaf）

Vue.js、React



HTTP /2、HTTP 3

B/S 架构走回 C/S



## Spring Cloud 

Spring Cloud 富客户端 Cloud Native Java 实现

Service Mesh 轻量级客户端 Cloud Native 实现



Spring Cloud Alibaba -> Service 



Java

- Java API
- Java Visual Machine
  - 传统实现
  - Native
    - penj9
    - GraalVM



@Indexed



## Spring Boot 编程思想下两本计划



### 已出版的核心篇



### 运维篇

更高的质量

更严谨的描述

更系统化的编写



### Web 篇



