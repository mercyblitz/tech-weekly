## 议题一：[【Java面试】Bean的创建顺序如何控制](https://www.bilibili.com/video/BV16WfAYcEUo)
### 解决方案：
1. 通过 `@DependsOn`
2. 通过 `BeanDefinitionRegistryPostProcessor`

Bean 注册过程往往是多条路径来处理，路径优先级存在的不确定性。

BeanFactory -> DefaultListableBeanFactory

<font style="color:#080808;background-color:#ffffff;">BeanFactoryPostProcessor 处理 BeanFactory（处理入口：AbstractApplicationContext#invokeBeanFactoryPostProcessors）</font>

<font style="color:#080808;background-color:#ffffff;">BeanDefinitionRegistryPostProcessor 属于特殊 BeanFactoryPostProcessor ，并且优先于普通的 BeanFactoryPostProcessor。</font>

## 议题二：[spring-如果优雅关闭java应用](https://www.bilibili.com/video/BV1gEZUYMEJg)
### 解决方案：
1. Spring Boot 优雅关闭的配置（解决方法）
2. Spring 优雅关闭
    1. Bean 生命周期 - 销毁周期
        1. `@PreDestroy`- JSR-250（可选，支持）
        2. `DisposableBean`（Spring 内建）
    2. 生命周期接口 - Lifecycle
        1. 手动 Lifecycle - Lifecycle
        2. 自动 Lifecycle - SmartLifecycle（Spring Boot 优雅关闭与该接口有这个关系）
    3. 事件监听 - ContextClosedEvent
        1. 接口监听 - `ApplicationListener`
        2. 注解监听 - `@EventListener`

补充：

+ 自定义线程池
    - 建议使用 Spring 包装线程池，为 `@Async`注解提供 Spring 线程池 Bean 配置

> 具体逻辑在：AsyncExecutionAspectSupport#getDefaultExecutor
>

        * `ExecutorConfigurationSupport`扩展类
            + 任务线程池
                - `ThreadPoolExecutorFactoryBean`
                - `ThreadPoolTaskExecutor`
            + 调度线程池
                - `ScheduledExecutorFactoryBean`
                - `ThreadPoolTaskScheduler`
+ 外部服务
    - RPC 服务
        * Feign
        * HttpInterface（Spring 6+）
        * Apache Dubbo
        * gRPC
    - Message 服务
        * Kafka
        * RocketMQ
    - DB 服务
        * DataSource
    - Caching 服务
        * Spring Caching
    - NoSQL 服务
        * Redis
        * Elasticsearch
    - 事务
        * Transactional



大多数的 Spring Boot 应用线程池来自于 Web 容器，所以基本 Spring Boot 优雅关闭可以实现基本优化关闭。

复杂的调用链路，SeviceA(Spring WebMVC `@RestController)`-> ServiceB(RPC Service) -> ServiceB(@Transactional -> DataSource -> Kafka Provider)（发送类似于半消息 RocketMQ）-> ServiceC(Kafka Consumer -> DataSource -> 提交事务 -> Kafka Provider（确认本地事务））



假设 Service A 优雅关闭应用，需要不再接受下游服务的调用，如 Service Z -> Service A，如果 Service Z 通过服务发现调用 Service A，那么，首先需要将 Service A 从注册中心移除/下线，然后通知/定期关闭 Web 服务，比如 Tomcat Connector。再如 Service A 本身存在 DB 服务的话，那么 Service A Web 不仅给 Service Z 调用，而且可能给其他服务调用，如 Service Y，但是由于Service A 从注册中心移除/下线，所以 Service Y 流量也不会再次进入 Service A。但是，DB 服务可能在处理相对较长的业务逻辑，所以 Service A 的优雅关闭需要充分时间来处理遗留的任务，那么，通常可以给定一个较为合理的关闭时间，或者等待它处理结束。如果选择等待处理结束，需要关注 DB 服务的连接活动情况。当所有的 DB Connection idle 超时后，收到某种通知，随后才能关闭完整的服务。

依次类推，Sevice A 如果存在其他服务的话，需要关注服务的生命周期回调。

复杂的应用优雅关闭一定是需要服务之前的依赖关系。

一般而言，最好是 Sevice 采用较长/合理的静默时间，不要立即关闭仅依赖于 Spring 生命周期管理。



## 议题三：[大厂技术专家带你手撕源码！Spring设计缺陷？](https://www.bilibili.com/video/BV11JZdYSEgd)
### 解决方案：
+ 通过 Comparator 来控制 `CommonAnnotationBeanPostProcessor`以及  `AutowiredAnnotationBeanPostProcessor`之间的Bean 处理顺序。

### 还原现场（最先还原）
#### 如何确定版本
Java 8

Spring Framework : 5.1.6.RELEASE

Spring Boot : 2.1.4.RELEASE

#### 如何确定应用使用哪些模块
+ Spring WebMVC
+ Spring AOP
+ Spring TX
+ Spring JDBC



#### 组件使用场景
##### CommonAnnotationBeanPostProcessor 使用场景
JSR-250 以及传统 Java EE 注解（可选支持，不一定存在于 Class-Path）

+ 依赖注入
    - `@javax.xml.ws.WebServiceRef`
    - `@javax.ejb.EJB`
    - `@javax.annotation.Resource`
+ 生命周期回调
    - 初始化 - `@PostConstruct`
    - 销毁 - `@PreDestroy`



Bean 配置实际晚于 `AutowiredAnnotationBeanPostProcessor`:

> AnnotationConfigUtils#registerAnnotationConfigProcessors(BeanDefinitionRegistry, Object)
>

```java
public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
			BeanDefinitionRegistry registry, @Nullable Object source) {

		DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
        ...

		if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
		}

		// Check for JSR-250 support, and if present add the CommonAnnotationBeanPostProcessor.
		if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {
			RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
			def.setSource(source);
			beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
		}
        ...
    }
```



##### AutowiredAnnotationBeanPostProcessor  使用场景
处理依赖注入注解：

+ `@Value` - 是与 Spring 配置相关，配置优先于 Spring Bean 初始化
+ `@Autowired` - 与 Spring Bean 初始化相关



将两类不同的注解用于同一个处理是否合适，值得讨论。



问题的症结在于：

DataSourceConfig Bean 在初始化过程中依赖了自身 Bean（自我依赖，循环依赖），导致了被依赖的 Bean（自己）不完全被初始化。源代码导致了 `CommonAnnotationBeanPostProcessor`在 `AutowiredAnnotationBeanPostProcessor`之前被执行。

原视频中之所以能解决是因为代码调整了 `AutowiredAnnotationBeanPostProcessor `与 `CommonAnnotationBeanPostProcessor`之间的加载顺序，使其正确。

DataSourceConfig Bean 编程方式是没有问题，理论上 Spring Framework 应该予以支持，因为默认 BeanProcessor 执行顺序是 `AutowiredAnnotationBeanPostProcessor`在 `CommonAnnotationBeanPostProcessor`之前。



BeanDefinition 定义顺序是 `AutowiredAnnotationBeanPostProcessor`在 `CommonAnnotationBeanPostProcessor`之前。

由于 `AutowiredAnnotationBeanPostProcessor`Order 数值高于 `CommonAnnotationBeanPostProcessor`的 Order 数值，所以优先级是 `CommonAnnotationBeanPostProcessor`高于 `AutowiredAnnotationBeanPostProcessor`。（直到最新版本也是如此。）

所以 DataSourceConfig Bean 在用法上需要注意这个细节。

 



## 议题四：[关税、股市和就业](https://content-static.cctvnews.cctv.com/snow-book/index.html)




