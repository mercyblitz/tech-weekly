## 「小马哥技术周报」- 第三十三期《InfoQ 2019 中国（大陆） Java 发展趋势报告解读》





### HTTP Streaming

Async 不间断



Observer 扩展

客户端数据被动接受

客户端订阅一个数据源，“持续”不断（连接不断，数据可能不确定达到）



### RPC

Request/Response = Command  -> Sync

周期性



请求发起方 -> 请求接受方

服务消费者     服务提供者（不确定何时请求到来）



HTTP/1.1

API -> Server



同步性能好于异步

ExecturoService.submit(Runnable) -> Future -> Future#get()



Spring WebFlux

默认情况并不是异步，而不是 Reactive -> 非阻塞 -> 回调 -> 观察者模式



Spring WebFlux 基于 Reactor



Spring + RSocket

HTTP/2.0

gRPC

dubbo



Java 9 

Java 10  非 TLS

Java 11 TLS

Java 12 非 TLS

Java 13 非 TLS



## 云原生对 Java 的趋势变化



Java 启动慢，Serverless 架构 Java 没有优势（劣势），快速启停，NodeJS

JVM 进程 -> JVM 运行时 + Jars(字节码) -> Javac 编译时 + JVM 运行时 JIT 编程 -> native code

Java 9 模块化减少 JVM 体积（不需要 JAR 不用加载，Metaspace）

GraalVM、OpenJ9 -> 编译时变成 native code



Spring 特性：IoC/DI、AOP（JDK 动态代理、字节码提升 ASM、AspectJ）

Spring 5.0 `@Indexed` 提升 `@Component` 扫描或者加载的时间，Annotation Processing Tools API 在编译时将 `@Compoent` 来构建索引

Spring Framework/Spring Boot 条件装配 + AOP -> 即使相同的 JAR，通过外部化配置（或参数），会生成不同的字节码

Spring 5.3 支持镜像化、静态化 -> JVM 镜像化





## 国内报告解读



### Spring Stack



### Apache Dubbo

