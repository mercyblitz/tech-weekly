<a name="dsfDn"></a>
# 议程安排
<a name="CfjM0"></a>
## Spring 异步处理
> 问题来源
> ![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687254430466-b9565d2a-832c-4a17-80c6-2c63ce459b22.png#averageHue=%23eeeeee&clientId=u3b014e2d-79b9-4&from=paste&height=619&id=ue77b87dc&originHeight=774&originWidth=529&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=65563&status=done&style=none&taskId=u5b9e6fb8-4ed4-4072-abc9-6a46315559c&title=&width=423.2)


<a name="vHfDC"></a>
### Spring @Async
<a name="h6ChX"></a>
#### 基本原理
@EnableAsync 来激活，底层基于 Spring AOP，对目标 Spring Bean 做方法级别的拦截。
<a name="y2b4W"></a>
#### AOP 实现类
拦截器类  - AnnotationAsyncExecutionInterceptor，扩展了 AsyncExecutionInterceptor
<a name="l4J2m"></a>
#### 任务执行器筛选
使用 Spring IoC 容器中的 TaskExecutor 作为具体任务执行器，如果找不到的话，会使用 SimpleAsyncTaskExecutor。因此，推荐基于 ThreadPoolExecutor 来实现的 TaskExecutor，如 ThreadPoolTaskScheduler。

<a name="mGbob"></a>
### Spring @Scheduled
<a name="NUS98"></a>
#### 基本原理
@EnableScheuding 来激活，底层基于 Spring AOP，对目标 Spring Bean 做方法级别的拦截。
<a name="fcF3s"></a>
#### 实现类
ScheduledAnnotationBeanPostProcessor 
<a name="xcR8y"></a>
##### 实现接口 MergedBeanDefinitionPostProcessor
常见的使用场景：分析 Spring BeanDefinition（RootBeanDefintion） 的相关元信息，比如 @Autowired
<a name="gVpiU"></a>
##### 实现接口 DestructionAwareBeanPostProcessor
常见的使用场景：拦截 Spring Bean 销毁动作，前置动作
<a name="umjQI"></a>
##### 实现接口 SmartInitializingSingleton
当 Spring IoC 容器预实例化所有非 Lazy 单例 Spring Bean 之后的回调操作。
```java
	@Override
	public void afterSingletonsInstantiated() {
		// Remove resolved singleton classes from cache
		this.nonAnnotatedClasses.clear();

		if (this.applicationContext == null) {
			// Not running in an ApplicationContext -> register tasks early...
			finishRegistration();
		}
	}
```
<a name="Kf0EK"></a>
##### 调度任务注册器 - ScheduledTaskRegistrar

<a name="G5mwi"></a>
### Spring ThreadPoolTaskScheduler 
可以通过 JDK 5 ThreadPoolExecutor 来整合 @Async 和 @Scheduled<br />是 Spring Bean，满足 Spring Bean 生命周期，通常是单例，继承了 ExecutorConfigurationSupport 的行为。
<a name="cq5lA"></a>
### 延伸问题：如何基于 Spring Quartz 实现动态 Cron 任务
<a name="AZxM5"></a>
#### 方法一：基于 ScheduledTaskRegistrar 来动态扩展  Cron 任务
比较推荐
<a name="DmHJ8"></a>
#### 方法二：动态的替换 CronTrigger 实现
推荐，Hack 程度稍微高一点
<a name="ZnXsp"></a>
#### 方法三：Spring Cloud @RefreshScope 实现
不推荐使用，这方式会重新初始化整合 Spring Bean，代价有点高。

可以采用分布式调度程序，动态地替换任务。
<a name="ofYQp"></a>
## Spring Boot 如何写单元测试
> 问题来源：
> ![c42e7b1087c39a407da3601e94ca2e2.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687254478284-de5dacf7-6046-4935-b731-73c3a654f457.png#averageHue=%23403f3e&clientId=u3b014e2d-79b9-4&from=paste&height=345&id=u7dbc1264&originHeight=431&originWidth=547&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=40482&status=done&style=none&taskId=u056cbdc0-0f0b-4b67-ac28-1d5d3a2b861&title=&width=437.6)
> @Valid 注解不生效
> 该单元测试如何在非 Spring MVC 下，执行 Bean Validation


在 Spring WebMVC 中，通过 org.springframework.web.method.annotation.ModelAttributeMethodProcessor#validateValueIfApplicable 方法来处理 @Validated 注解，以及触发 Bean Validation 框架。<br />如果要在 Spring Testing 或者Spring Boot Testing 场景下，处理 Controller 实现类的话，方法如下：

- Spring MockMvc Testing 处理
- 基于 Spring AOP 与 Bean Validation 整合类 - MethodValidationInterceptor，增加到 Spring 继承测试中
<a name="b10mj"></a>
## Spring Boot 如何优雅启动检测
> 问题来源：
> ![31cd862920fd0895a9e0ad7efe95ae6.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687254555601-e89848df-ec29-4ed2-b917-6a6ea8751167.png#averageHue=%23cad0dd&clientId=u3b014e2d-79b9-4&from=paste&height=427&id=ub75c2193&originHeight=534&originWidth=587&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=197596&status=done&style=none&taskId=uaef05685-ac58-416f-8f10-852dcc3299c&title=&width=469.6)
> ![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687254592612-da78fa51-a3e7-4a89-8fa4-d4e067be0f95.png#averageHue=%23efefef&clientId=u3b014e2d-79b9-4&from=paste&height=222&id=Qzwid&originHeight=278&originWidth=525&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=23249&status=done&style=none&taskId=u5673d073-78ae-48e0-986b-ae9085199b1&title=&width=420)
> ![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687254643498-d36f9b94-4148-4772-b64c-ee33a123836a.png#averageHue=%23f1f0f0&clientId=u3b014e2d-79b9-4&from=paste&height=391&id=u080f5fec&originHeight=489&originWidth=525&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=60753&status=done&style=none&taskId=uef70fbd0-2ce3-479a-96b3-2f092d6288e&title=&width=420)


巧用 Exit Code，退出码，正常退出 0，其他情况退出其他整数。

方便运维了解具体情况。

假设配置校验错误的退出码：-1

Spring Boot 有一个 SPI ：ExitCodeGenerator。

基于 Spring Boot 监听器来实现启动检测。

实现方式：

- SpringApplicationRunListener
- ApplicationListener
- ApplicationContextInitializer
- EnvrionmentPostProcessor
- 其他

 
<a name="Gs2OB"></a>
## Spring Boot 如何优雅停机
<a name="u2zrb"></a>
### Spring 应用如何优雅地关闭 Spring Bean（组件）

1. Spring Bean 实现：
- @PreDestroy 标注方法
- DisposableBean 实现
- 自定义指定 destroy 方法
2. Spring ApplicationListener 实现：
- 监听 ContextClosedEvent
- 监听 ApplicationFailedEvent
3. Spring SmartLifecycle 实现：
- 实现 stop 方法
<a name="bZkgB"></a>
#### Spring 应用如何优雅关闭 Kafka 服务
<a name="mRyjo"></a>
#### Spring 应用如何优雅关闭 ES 服务
<a name="h63nW"></a>
#### Spring 应用如何优雅关闭 Redis 服务
<a name="h1VAv"></a>
#### Spring 应用如何优雅关闭线程服务
<a name="strrE"></a>
##### 使用 Spring TaskExecutor 或者 TaskScheduler Bean 实现
Spring 任务或调度执行 Bean 实现往往继承 ExecutorConfigurationSupport
<a name="LMmtD"></a>
###### 初始化操作
```java
	@Override
	public void afterPropertiesSet() {
		initialize();
	}

	/**
	 * Set up the ExecutorService.
	 */
	public void initialize() {
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (!this.threadNamePrefixSet && this.beanName != null) {
			setThreadNamePrefix(this.beanName + "-");
		}
		this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
	}
```
this.executor 是 ExecutorService 实现，比如 ThreadPoolExecutor 或者 ScheduledExecutorService，具体依赖于 initializeExecutor 抽象方法实现。
<a name="OPUha"></a>
###### 销毁操作
```java
	@Override
	public void destroy() {
		shutdown();
	}

	/**
	 * Perform a shutdown on the underlying ExecutorService.
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public void shutdown() {
		if (logger.isDebugEnabled()) {
			logger.debug("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (this.executor != null) {
			if (this.waitForTasksToCompleteOnShutdown) {
				this.executor.shutdown();
			}
			else {
				for (Runnable remainingTask : this.executor.shutdownNow()) {
					cancelRemainingTask(remainingTask);
				}
			}
			awaitTerminationIfNecessary(this.executor);
		}
	}
```
当 waitForTasksToCompleteOnShutdown 为true，等待未完成执行结束。<br />否则，立即取消先有任务。
<a name="BJOHF"></a>
##### 将 JDK 5+ ThreadPoolExecutor 或者 ScheduledThreadPoolExecutor 对象作为 Spring Bean
特别注意：需要显示地指明 destroy 方法，比如：
```java
    @Bean(initMethod="prestartAllCoreThreads" , destroyMethod="shutdown")
    public ScheduledThreadPoolExecutor executor(){
        return new ScheduledThreadPoolExecutor();
    }
```
<a name="GPRLf"></a>
### Spring Boot 2.3 + 支持的 Web Server 优雅停机
小于 Spring Boot 2.3 的话，可以参考该代码来实现。
<a name="TwN7o"></a>
### Spring Cloud 应用优雅停机
<a name="fh4R2"></a>
#### Spring Cloud 服务注册与发现
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1687269910339-679f1a44-6882-46af-bd6f-0fb6ebe90782.png#averageHue=%23fdfdfc&clientId=u14a4d967-ca10-4&from=paste&height=457&id=u3d606783&originHeight=571&originWidth=1123&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=34430&status=done&style=none&taskId=u85f8d48f-aaa1-448b-b8ff-565f6f11d07&title=&width=898.4)<br />根据注册中心定制服务发现客户端策略，配合负载均衡逻辑
<a name="kz3MX"></a>
#### Spring Cloud 分布式调用
<a name="F00Iu"></a>
##### Spring Cloud OpenFeign 调用
<a name="YTMzR"></a>
##### Spring Cloud 服务调用 - @HttpExchange
<a name="vtequ"></a>
##### Spring Cloud Dubbo 调用方式
<a name="il0lK"></a>
##### Spring Cloud @LoadBalanced RestTemplate
<a name="YtkU5"></a>
##### Spring Cloud WebClient

<a name="kKkhm"></a>
#### Spring Cloud 异步组件
Spring Kafka、Spring AMQP 等<br />Spring Bean 销毁逻辑与 Spring Cloud 应用关闭时实习细节。<br />ContextClosedEvent 事件触发时机会早于 Spring Beans 销毁<br />ContextClosedEvent < SmartLifecycle < Spring Beans 销毁
<a name="OIfn5"></a>
#### Spring Cloud 分布式锁
JVM Runtime Shutdown 来整合，信号的方式来控制 Spring 应用上下文的关闭。
<a name="QEjPF"></a>
## 面试题《Mybatis的SqlSession和SqlSessionFactory线程安全》吗？
SqlSession 和 SqlSessionFactory 的默认实现均不是线程安全的，不过在企业并发场景中不会遇到线程安全问题。

<a name="rv5eA"></a>
### 并发预备知识
判断一个对象是否存在线程安全：

- 该对象是否有状态
   - 有状态，可能会面临线程安全风险
      - 只读状态是没有分享
   - 无状态，没有线程安全分享
- 该对象是否为容器对象
   - 是，判断其成员是否为线程安全
      - 如果成员不是线程安全对象的话，容器也不会是线程安全的，除非容器对象增加线程安全措施
<a name="q3gYh"></a>
### SqlSessionFactory 默认实现 - DefaultSqlSessionFactory
DefaultSqlSessionFactory 可以通过 getConfiguration() 拿到 Configuration 对象，因此它可能会多线程修改。

<a name="yBMP2"></a>
### SqlSession 默认实现 - DefaultSqlSession
DefaultSqlSession 也依赖于 Configuration 对象，它也不是严格意义上的线程安全。


<a name="EVPIt"></a>
### Mybatis 工作原理
企业级 MyBatis 开发过程中，往往通过 Mapper 接口来操作 JDBC 数据。<br />Mapper 接口会生成 Mapper 动态代理

理论上，Mapper 每次执行会有触发 SqlSessionFactory 实现的 openSession 方法，来获取 SqlSession。<br />假设，某个 Mapper 执行了 N 次，那么是否意味着 SqlSession 对象也会创建 N 次？<br />实际上，SqlSession 仅会创建一个。

SqlSessionFactory 底层实现，的确每次调用 openSession 都会创建 SqlSession 对象，但是 Mapper 代理对象<br />使用到的 SqlSessionFactory 对象，并非是 SqlSessionFactory 底层对象，而是被包装成 SqlSessionManager


SqlSessionFactory 底层实现调用 openSession 方法时，会使用 TransactionFactory 来创建 Transaction 对象，该对象能够返回 JDBC 底层的 Connnection 对象。

从 Mybatis Spring 角度分析，Mapper Bean 是通过 MapperFactoryBean 创建，调用其 getObject() 方法：
```java
  public T getObject() throws Exception {
    return getSqlSession().getMapper(this.mapperInterface);
  }
```

MyBatis JDBC Connection 与 Spring JDBC Connection，从代码上来看，如果 Spring 与 MyBatis 整合，Spring Connection 与 Mybatis Connection 是独立的？

Spring Connection 来自于 Spring DataSource Bean<br />Mybatis Connection 来自于 DataSource#getConnection

在 MyBatis Spring 中提供了一个 SqlSessionFactory 的实现，即 SpringManagedTransactionFactory：
```java
  @Override
  public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
    return new SpringManagedTransaction(dataSource);
  }
```

```java
  private void openConnection() throws SQLException {
    this.connection = DataSourceUtils.getConnection(this.dataSource);
    this.autoCommit = this.connection.getAutoCommit();
    this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

    LOGGER.debug(() -> "JDBC Connection [" + this.connection + "] will"
        + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
  }
```

<a name="fdDAa"></a>
## 下一次讨论：浅谈 GraalVM 
<a name="rbt41"></a>
## 连麦讨论

