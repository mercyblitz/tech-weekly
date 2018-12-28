# 2018.12.28「小马哥技术周报」- 第十四期《Apache Dubbo 微服务系列之 Dubbo 服务熔断》





## Hystrix

Based on RxJava ( J.U.C + Lambda)

### 熔断特性

- 线程池

  - ThreadPoolExecutor
  - 线程隔离
    - 执行线程 > 熔断线程（堵塞）

- 信号量

  - Semaphore

- 同性：

  - 消费者/生产者模型（线程/OS 原语） = 消费者/生产者 Queue（AQS）


### 推荐书籍

- 高并发
  - 《Concurrent Programming in Java™: Design Principles and Patterns, Second 
    Edition》 - Doug Lea
- 





### Dubbo 微服务支持需求

- Dubbo 对接 Spring Web MVC

  - Dubbo 对接 Spring WebFlux

- Dubbo 整合 Spring Cloud

  - 服务端：依赖于 Dubbo 与 Spring Web MVC 整合
  - 客户端：Spring Cloud Open Feign -> Dubbo 调用
    - OpenFeign 客户单 -> Dubbo 服务端
      - Dubbo 没有 REST 服务
      - Dubbo 提供 REST 服务
      - Dubbo REST 协议适配
        - Dubbo 序列化/反序列化与 Spring Web MVC 是不是兼容

- Dubbo 双协议 + Dubbo OPS 整合
