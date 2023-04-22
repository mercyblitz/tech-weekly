<a name="TlMJS"></a>
## 第九章：Spring Bean 生命周期（Bean Lifecycle）
<a name="E1LVu"></a>
### [96 | Spring Bean属性赋值前阶段：配置后的PropertyValues还有机会修改吗？](https://time.geekbang.org/course/detail/265-209709)

```java
public class User {

    private Long id;

    private String name;

    private Integer age;

    public User(){
    }

    public User(Long id, String name, Integer age) {
        
    }
    
}
```

```xml
<bean id="user" class="User">
    <constructor-arg index="0" value="1" />
    <constructor-arg index="1" value="Mercy" />
  	<constructor-arg index="2" value="18" />
</bean>
```
<constructor-arg> XML 元素会转换成 PropertyValue，如果 Java 字节码编译时不带上调试信息的话，参数名称会是类似于 arg0 , arg1 , arg2
```java
public class User {

    ...

    public User(Long arg0, String arg1, Integer arg2) {
    }

}
```
PropertyValues = 在 User 构造器上会有三个对象 PropertyValue<br />PropertyValue[0]  -> name = arg0 , index = 0 , value = 1L<br />PropertyValue[1]  -> name = arg1 , index = 1 , value = "Mercy"<br />PropertyValue[2]  -> name = arg2 , index = 2 , value = 18

```java
@Bean
public B b (A a){
	// Bean B 在实例化时需要 Bean A 已经初始化
    return new B(a); // 当 new B(a) 作为方法返回对象时，它并没有初始化，仅仅是实例化
    // B 类实现生命周期回调方法，或者在 @Bean 注解上指定自定义的生命周期回调方法
}
```

<a name="D5on8"></a>
### [97 | Aware接口回调阶段：众多Aware接口回调的顺序是安排的？](https://time.geekbang.org/course/detail/265-209711)
> initializeBean 方法会在 populateBean 方法之后调用

Aware 它是一种植入 Bean 的回调方式，植入的对象是开发中需要的，比如：<br />当某个 Bean 需要 ClassLoader，开发人员可以选择将该 Bean 的类实现 BeanClassLoaderAware，然后强制实现 setBeanClassLoader  方法，获取到 Spring IoC 加载该 Bean Class 的 ClassLoader<br />不过 Aware 接口在 Spring IoC 容器中是有限，其共同扩展 org.springframework.beans.factory.Aware 接口：

- BeanNameAware -> Bean 名称
- BeanClassLoaderAware -> ClassLoader
- BeanFactoryAware -> BeanFactory
- ApplicationContextAware -> ApplicationContext
- ApplicationEventPublisherAware -> ApplicationEventPublisher
- EnvironmentAware -> Environment
- ResourceLoaderAware -> ResourceLoader
- ImportAware -> AnnotationMetadata

大多数 Aware 接口调用来自于 ApplicationContextAwareProcessor，它实际上是一个 BeanPostProcessor 的实现，核心逻辑实现在 postProcessBeforeInitialization：
```java
	@Override
	@Nullable
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		...
		else {
			invokeAwareInterfaces(bean);
		}
		return bean;
	}

	private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof EnvironmentAware) {
			((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
		}
		if (bean instanceof EmbeddedValueResolverAware) {
			((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
		}
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
		}
		if (bean instanceof ApplicationEventPublisherAware) {
			((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(this.applicationContext);
		}
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
		}
	}
```
请关注 Aware 接口在 ApplicationContextAwareProcessor  执行属性，最好植入的对象不要相互依赖

大多数 Aware 接口能够被 @Autowired 来替代，实际上，Aware 植入的方式是一种传统的 Spring 基于接口的植入方式，便于单元测试的方式。

<a name="NMJ2X"></a>
### [98 | Spring Bean初始化前阶段：BeanPostProcessor](https://time.geekbang.org/course/detail/265-209714)
在 InstantiationAwareBeanPostProcessor 阶段，它将 Bean 的生命周期划分的粒度更小，增加了<br />实例化部分以及 Bean 属性值的部分

Spring IoC 容器

- BeanDefinition 合并：将 应用定义的 BeanDefinition 合并成 RootBeanDefinition
- Bean 实例化前：resolveBeforeInstantiation
   - InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation
- Bean 实例创建：createBeanInstance
   - instantiateBean
- Bean 装配：populateBean
   - Bean 实例化后：postProcessAfterInstantiation
   - Bean 属性值已准备：postProcessProperties
- Bean 初始化：initializeBean
   - Bean 初始化前：postProcessBeforeInitialization
   - Bean 初始化后：postProcessAfterInitialization
- 单例 Bean（非延迟初始化） Beans 创建完成
   - SmartInitializingSingleton#afterSingletonsInstantiated

<a name="mz1tk"></a>
### [99 | Spring Bean初始化阶段：@PostConstruct、InitializingBean以及自定义方法](https://time.geekbang.org/course/detail/265-212100)
某个 Bean 类中可以定 1 个或 N 个 初始化方法
<a name="GiXvS"></a>
### [100 | Spring Bean初始化后阶段：BeanPostProcessor](https://time.geekbang.org/course/detail/265-212101)

<a name="QcPQ6"></a>
### [101 | Spring Bean初始化完成阶段：SmartInitializingSingleton](https://time.geekbang.org/course/detail/265-212103)

```java
public enum State {
    NEW,
	OLD
}
```

```java
public final class State extends Enum<State> { // 这个类是 Java 编译器生成
    
    public static final State NEW = new State();
    
    public static final State OLD = new State();

    // ...
}
```

SCJP OCJP
<a name="UNDAQ"></a>
### [102 | Spring Bean销毁前阶段：DestructionAwareBeanPostProcessor用在怎样的场景?](https://time.geekbang.org/course/detail/265-212105)

<a name="HHlXL"></a>
### [103 | Spring Bean销毁阶段：@PreDestroy、DisposableBean以及自定义方法](https://time.geekbang.org/course/detail/265-212106)

<a name="fiEbc"></a>
### [104 | Spring Bean垃圾收集（GC）：何时需要GC Spring Bean？](https://time.geekbang.org/course/detail/265-212107)

<a name="orTuH"></a>
### [105 | 面试题精选](https://time.geekbang.org/course/detail/265-212108)
