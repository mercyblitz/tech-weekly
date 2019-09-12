# 「小马哥技术周报」- 第三十一期《Apache Dubbo 设计与实现系列之 Spring Cloud 工程（中）》



## Spring Cloud Open Feign



### 使用方式

1. 标注 `@EnableFeignClients`
   1. `@Import` `FeignClientsRegistrar`
      1. `FeignClientsRegistrar` 属于`ImportBeanDefinitionRegistrar` 实现类
   2. 指定一些注解属性方法
      - `@FeignClient` 组件扫描（模糊搜索）
        - value()
        - basePackages()
        - basePackageClasses()
      - `@FeignClient` 配置类指定（精确指定）
        - clients()
      - `@FeignClient` 默认配置（全局）
        - defaultConfiguration()（可选）
        - FeignClientsConfiguration（默认）



`@FeignClient` `configuration()` 属性方法指定 Feign 配置



### 组件注册

1. Spring Framework 3.1 + Enable 模块驱动
   1. `@Import`
      1. `@Configuration` Class
      2. `ImportSelector` 实现类
      3. `ImportBeanDefinitionRegistrar` 实现类
   2. 注解“派生”
      1. `@EnableABC` -> `@EnableDEF`
2. 理解 Spring Framework 3.0+ `@Import` 含义
3. 理解 Spring Boot 2.1.0 + 阻止同名 Bean 注册
4. 理解 Spring Framework FactoryBean 使用场景
5. 理解 Spring ApplicationContext 层次性
6. 理解 Spring Cloud ApplicationContext Factory



> Root ApplicationContext 
>
> 例子：Spring Boot 2.1.0 +
>
> @FeignClient(name="abc") // abc ApplicationContext -> parent Root ApplicationContext
>
> public class A {}
>
> @FeignClient(name="abc") // abc ApplicationContext -> parent Root ApplicationContext
>
> public class B {}
>
> @FeignClient(name="def")
>
> public class C {}



### 执行流程



#### HTTP 调用

FeignClientFactoryBean#getObject 获取的是一个标注 `@FeignClient` 类代理对象，被代理的方法执行时，相当于 HTTP 调用

Client 能够提供多种适配，包括 JDK HTTP Client、Apache HttpClient、以及 OkHttp3



#### Feign 配置

- 全局配置

  `org.springframework.cloud.openfeign.FeignClientsConfiguration`

- 独立配置

  `@FeignClient#configuration()` 属性





#### 序列化/反序列化过程

- 序列化 Encoder

Spring Cloud Open Feign ->  SpringEncoder



- 反序列化 Decoder

Spring Cloud Open Feign -> SpringDecoder 依赖于 `HttpMessageConverters` HTTP 返回类型转换成目标类型对象



#### Feign 注解契约解析

- feign.Contract 

Spring Cloud Open Feign -> SpringMvcContract -> 利用 Feign Contract 扩展出识别/解析 Spring MVC 标准注解

- 请求映射注解 
  - `@RequestMapping`
- 方法参数注解
  - `@RequestParam`
  - `@RequestHeader`
  - `@PathVariable`	
  - Spring Cloud `@SpringQueryMap`



#### 方法参数注解解析处理器

- AnnotatedParameterProcessor
  - RequestParamParameterProcessor - `@RequestParam`
  - RequestHeaderParameterProcessor - `@RequestHeader`
  - PathVariableParameterProcessor - `@PathVariable`
  - QueryMapParameterProcessor - Spring Cloud `@SpringQueryMap`



## Dubbo 在 Spring Cloud Open Feign 中的扩展

### 目的

Dubbo 服务替换掉 Spring Cloud Open Feign

Dubbo 多协议支持替换 Spring Cloud Open Feign 单协议（HTTP/REST)



### 用法

在原有 `@FeignClient` 声明类上追加一个 `@DubboTransported` 的注解，使得 Feign 调用变为 Dubbo



#### 使用前提

Dubbo 需要解析 Dubbo 服务提供方 REST 元信息，主要支持三种类型 Feign Contract 扩展：

- Feign 原生
- Spring Cloud OpenFeign - `SpringMvcContract `
- JAX-RS 1/2（标准 Java REST 注解） `JAXRS2Contract` extends `JAXRSContract`







Feign defaultProxy

Dubbo proxy(deaultProxy)



if( meta matched ) {

   Dubbo proxy -> Generic

}else{

  defaultProxy // Feign 原始实现

}





