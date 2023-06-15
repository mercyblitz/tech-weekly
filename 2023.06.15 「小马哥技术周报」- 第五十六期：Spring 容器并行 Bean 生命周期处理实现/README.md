<a name="MBkFK"></a>
# 底层实现
<a name="gDX5b"></a>
## 代码实现
项目工程 - [microsphere-spring](https://github.com/microsphere-projects/microsphere-spring)<br />核心代码 - [io.microsphere.spring.context.event.ParallelBeanFactoryListener](https://github.com/microsphere-projects/microsphere-spring/blob/main/microsphere-spring-context/src/main/java/io/microsphere/spring/context/event/ParallelBeanFactoryListener.java)

<a name="eAFzK"></a>
# 知识贮备
<a name="sPnuo"></a>
## Spring 可依赖对象
<a name="o01hh"></a>
### Spring Bean 对象
<a name="bmgMU"></a>
#### Spring BeanDefinition
<a name="G0xTQ"></a>
##### Spring Class 级别 BeanDefinition
指定 BeanDefinition class<br />指定 BeanDefinition class -> FactoryBean 实现<br />@Component AnnotatedBeanDefinition class = 配置类
<a name="vhh82"></a>
##### Spring 工厂方法 BeanDefinition
BeanDefinition#setFactoryBean (可选）

- 如果存在的话，说明工厂方法是对象方法
- 如果存在的话，说明工厂方法是类方法

BeanDefinition#setFactoryMethod（可选）

- 如果存在的话，Bean 会通过工厂方法来创建
<a name="IE8ah"></a>
##### Spring @Bean BeanDefinition
Spring 3.0 + <br />Configuration class @Bean 方法声明<br />通过 AnnotatedBeanDefinition#getFactoryMethodMetadata() 获取 @Bean 方法信息
<a name="vJIzS"></a>
##### Spring Suppiler BeanDefinition
Spring 5.0 + <br />BeanDefinition 执行 org.springframework.beans.factory.support.AbstractBeanDefinition#setInstanceSupplier<br />该 Bean 通过 AbstractBeanDefinition#getInstanceSupplier() 方法返回 Supplier 来获取<br /> 
<a name="zdNj5"></a>
#### Spring 外部注册的 Singleton 对象
SingletonBeanRegistry#registerSingleton(String,Object)<br />被注册 Singleton 对象的生命周期由 IoC 容器外部管理，该对象是一个已准备就绪的 Bean
<a name="HyyX7"></a>
### Spring ResolvableDependency
手动注册，AbstractApplicationContext
```java
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    	...
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);
    	...
	}
```

```java
public class SomeBean {

    @Autowired
    private ApplicationContext context; // 非 Spring Bean，而是 ResolvableDependency

    // 非 Spring Bean，而是 ResolvableDependency
    @Autowired
    private ConfigurableApplicationContext applicationContext; 

    @Autowired
    private EchoService echoService; // EchoService Spring Bean，通过类型依赖查找

    @Autowired
    @Qualifier("userService")
    private UserService userService; // UserService Spring Bean，通过 @Qualifier 查找

    @Autowird
    private Map<String,UserService> userServices;
}
```

SomeBean 实际上依赖的 Spring Beans：EchoService 和 UserService，需要进一步分析出两个类型对应的 Bean 名称。


```java
  public MybatisAutoConfiguration(MybatisProperties properties,
      ObjectProvider<Interceptor[]> interceptorsProvider,
      ObjectProvider<TypeHandler[]> typeHandlersProvider, 
      ObjectProvider<LanguageDriver[]> languageDriversProvider,
      ResourceLoader resourceLoader, 
      ObjectProvider<DatabaseIdProvider> databaseIdProvider,
      ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
      ObjectProvider<List<SqlSessionFactoryBeanCustomizer>> sqlSessionFactoryBeanCustomizers) {
    this.properties = properties;
    this.interceptors = interceptorsProvider.getIfAvailable();
    this.typeHandlers = typeHandlersProvider.getIfAvailable();
    this.languageDrivers = languageDriversProvider.getIfAvailable();
    this.resourceLoader = resourceLoader;
    this.databaseIdProvider = databaseIdProvider.getIfAvailable();
    this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
    this.sqlSessionFactoryBeanCustomizers = sqlSessionFactoryBeanCustomizers.getIfAvailable();
  }
```
Spring Bean 类型：

1. MybatisProperties
   1. parameter.getParameterizedType() == MybatisProperties.class
2. Interceptor - ObjectProvider<Interceptor[]>
   1. parameter.getParameterizedType() == ObjectProvider<Interceptor[]>>
      1. rawType = ObjectProvider.class
      2. actualTypeParameters = 
         1. [0] = Interceptor[].class
3. TypeHandler - ObjectProvider<TypeHandler[]>
   1. parameter.getParameterizedType() == ObjectProvider<TypeHandler[]>>
      1. rawType = ObjectProvider.class
      2. actualTypeParameters = 
         1. [0] = TypeHandler[].class
4. LanguageDriver - ObjectProvider<LanguageDriver[]>
5. DatabaseIdProvider - ObjectProvider<DatabaseIdProvider>
6. ConfigurationCustomizer - ObjectProvider<List<ConfigurationCustomizer>>
   1. parameter.getParameterizedType() == ObjectProvider<List<ConfigurationCustomizer>>
      1. rawType = ObjectProvider.class
      2. actualTypeParameters = 
         1. [0] = List<ConfigurationCustomizer>
            1. rawType = List.class
            2. actualTypeParameters = 
               1. [0] = ConfigurationCustomizer.class
7. SqlSessionFactoryBeanCustomizer - ObjectProvider<List<SqlSessionFactoryBeanCustomizer>>

Spring ResovlableType：

1. ResourceLoader
<a name="Pwm6e"></a>
## Bean 作用域
<a name="oGTMD"></a>
### Singleton Scope Bean

