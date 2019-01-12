# 2019.01.11「小马哥技术周报」- 第十六期《Apache Dubbo 微服务系列之 Dubbo Spring Cloud 实现》



## 预备知识

- Spring Cloud Commons 抽象
  - 服务注册和发现
    - 应用名称（服务名称）：`spring.aplication.name` 
    - 应用实例：`ServiceInstance`
      - 信息
        - 服务名：serviceId
        - 主机：host
        - 端口：port
        - 安全：secure（HTTPS / HTTP)
        - 元信息：`Map<String,String>`  metadata
      - `Registration`
    - 服务注册（到注册中心）
    - 服务发现（从注册中心）
    - 实现模块
      - 内建：`eureka`、`consul`、`zookeeper`
      - 孵化：`nacos`
  - Spring Cloud Environment（基于 Spring Framework Environment）
- Spring Cloud OpenFeign
  - 类型：客户端面向 Java 接口的 REST 框架
  - 协议：REST（HTTP）
  - 注解： `@FeignClient`
    - 服务接口绑定应用名称



## 场景分析



### 场景一：服务端（Dubbo + REST） 与客户端（Spring Cloud Feign）



Dubbo Spring Boot : 只会注册 Dubbo 服务接口相关的注册信息

Dubbo Spring Cloud：Dubbo Spring Boot  + 应用本身的信息



### 场景二：服务端（Dubbo + REST） 与客户端（Spring Cloud Feign底层使用 Dubbo 协议）



#### 代码分析猜测实现

- 示例代码 - 客户端接口声明

```java
    @FeignClient("spring-cloud-alibaba-dubbo")
    public interface EchoService {

        @GetMapping(value = "/echo")
        String echo(@RequestParam("message") String message);
    }
```

- 示例代码 - 客户端接口使用

```java
        @Autowired
        private EchoService echoService;

        @GetMapping("/call/echo")
        public String echo(@RequestParam("message") String message) {
            return echoService.echo(message);
        }
```



`EchoService` 是一个Bean（实现对象），猜测它是动态实现~~或者字节码提升~~

- 特征注解： `@FeignClient`
- 激活注解：`@EnableFeignClients `
- 协议：HTTP（REST）
- 路由规则：
  - URI：`/echo`
  - 请求参数：与服务提供方保持一致
  - 应用名称：`spring-cloud-alibaba-dubbo`
    - 服务实例：HOST+PORT
    - URL： http://${HOST}:${PORT}/${URI}



- 服务提供方
  - `DefaultEchoService`
    - Dubbo 唯一接口名称：`org.springframework.cloud.alibaba.dubbo.service.EchoService:1.0.0`
    - 接口
      - REST 接口
      - Dubbo Java 接口



- 疑问
  - 如何注册 REST 和 Dubbo 接口作为特殊服务到注册中心？
    - Dubbo Registry SPI + Spring Cloud Commons 抽象 API
      - Dubbo Registry  : `RegistryFactory` 以及 `Registry`
      - 注册实例数据：`Registration`（`ServiceInstance`）
      - 注册中心接口：`ServiceRegistry`
  - 如何获取当前应用暴露的 Dubbo 多协议的元信息







