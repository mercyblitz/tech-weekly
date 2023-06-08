<a name="bdFD4"></a>
# 知识贮备
<a name="rmuxS"></a>
## Spring 知识储备
<a name="uqDRJ"></a>
### Spring BeanDefinition 注册
Spring BeanDefinition 先来先服务（FIFO）注册，Spring Bean 在默认初始化的时候，是依赖 BeanDefinition 的定义顺序，**Bean 属于非延迟单例 Bean（Non-Lazy Singleton Beans）**<br />假设，存在 Spring Bean 定义：A、B、C、D、E 和 F，依赖如下：<br />![](https://cdn.nlark.com/yuque/0/2023/png/222258/1686230873280-7d98c470-24a2-438e-ba11-8b3e7895c0a2.png#averageHue=%23fdfcfa&clientId=uc2f896bc-6132-4&from=paste&id=u318ed320&originHeight=636&originWidth=770&originalType=url&ratio=1.25&rotation=0&showTitle=false&status=done&style=none&taskId=ufd82c10c-46ad-460b-8653-6765b53e9c7&title=)<br />A 依赖于 B 和 C，C 依赖于 D，D 和 E 循环依赖，同时，F 与其他 Bean 没有依赖关系，所以 A 和 F 可以并行初始化。

A 在初始化阶段，需要完成 B、C、D 和 E 的初始化。<br />A（B，C（D（E）））或 A（B，C（E（D））），允许循环依赖，D 和 E 必须是非构造器注入

<a name="FYGXx"></a>
### Spring Bean 注入方式
<a name="NgUsh"></a>
#### 构造器注入
不支持循环依赖处理
<a name="mHFXW"></a>
#### 方法注入
高版本的 Spring Boot 默认关闭了循环依赖处理
<a name="aPEIf"></a>
##### Setter 方法注入
setXXX
<a name="YiujV"></a>
##### 普通方法注入
```java
@Autowired
public void init(E e){

}
```
<a name="Hdx9k"></a>
#### 字段注入
高版本的 Spring Boot 默认关闭了循环依赖处理

<a name="SFD4P"></a>
### Spring 注入实现
<a name="kXo1s"></a>
#### Spring XML 注入方式
<a name="DZDHl"></a>
#####  <property> ref 属性
<a name="sxY2B"></a>
##### <constructor> ref 属性
<a name="r4o27"></a>
#### Spring 方式
<a name="gUZb4"></a>
##### Spring 注解注入 - @Autowired
底层实现类 - AutowiredAnnotationBeanPostProcessor
<a name="gGhoM"></a>
##### Java EE 注解注入 - @Inject
底层实现类 - AutowiredAnnotationBeanPostProcessor
<a name="AiOzV"></a>
##### Spring @Bean 方法参数注入
底层实现类 - ConfigurationClassParser
<a name="CgiHD"></a>
##### Java 标准注解注入 - @Resource
底层实现类 - CommonAnnotationBeanPostProcessor
<a name="PjRBZ"></a>
##### EJB 注解注入 - @EJB
底层实现类 - CommonAnnotationBeanPostProcessor
<a name="Cl60r"></a>
##### WebServices 注解注入 - @WebServicesRef
底层实现类 - CommonAnnotationBeanPostProcessor

<a name="JTxiU"></a>
### Spring Bean 初始化方式
<a name="gFMCg"></a>
#### Spring IoC 容器启动过程中的 BeanFactory 后置处理器初始化
主要代表有：BeanDefinitionRegistryPostProcessor 以及 BeanFactoryPostProcessor：
```java
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			...
			try {
            	...
				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);
				...
			}
        	...
		}
	}

```
特别注意，除了 BeanDefinitionRegistryPostProcessor 和 BeanFactoryPostProcessor Beans 被初始化之外，这两个接口实现类中可能也有 Beans 获取（主要通过依赖查找的方式）

BeanDefinitionRegistryPostProcessor 主要职责在 IoC 容器启动过程中注册 BeanDefinition。<br />BeanFactoryPostProcessor 主要职责在 IoC 容器启动过程中，修改 BeanFactory 行为，或者初始化少量的 Beans。

<a name="DQZrO"></a>
#### Spring IoC 容器过程中的默认初始化方式
Spring IoC（ApplicationContext）在启动工程中默认 Bean 初始化方式，**非延迟单例 Bean（Non-Lazy Singleton Beans）：**
```java
	protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
		...
		// Instantiate all remaining (non-lazy-init) singletons.
		beanFactory.preInstantiateSingletons();
	}
```
<a name="o2re6"></a>
#### Spring IoC 容器过程后的 Lazy Beans 的初始化
它不再影响启动时间，但是会影响整体性能。

<a name="Wh45R"></a>
### BeanDefintion 分类
<a name="Wtyb5"></a>
#### Java Beans
<a name="FgETM"></a>
##### 普通 Java Beans
<a name="n0J8W"></a>
##### 注解方式 Java Beans
<a name="fyl9H"></a>
#### FactoryBean 实现

<a name="ZRCHW"></a>
### Spring Bean 注入和被注入关系
如 A 注入 B， B 是容器 Bean，A 是被依赖 Bean（Dependent）<br />注入和被注入关系是多对多的关系，M 和 N 的数字关系<br />A 可以注入 B，也可以注入到 C<br />C 可以注入 A


当 A @Autowired B 时，A 会主动的注册 Dependent Bean Names，即 B Bean 名称<br />底层有一个 dependentBeanMap，是多对多 接口

dependentBeanMap.get("A") == ["B","C"]

```java
@Autowired
private Optional<User> user; // 延迟依赖注入

@Autowired 
private ObjectFactory<User> user2; // 延迟依赖注入

@Autowired
private ObjectProvider<User> user3; // 延迟依赖注入

@Inject
private User user4; // 非延迟依赖注入

// Map , Collection

@Autowired
private Map<String,User> users; // 非延迟依赖注入

@Autowired
private User user5; // 非延迟依赖注入

```
<a name="lonab"></a>
### Spring 注入的被依赖 Bean 筛选方法
<a name="Ntoi5"></a>
#### Spring @Autowired 注解注入的被依赖 Bean 筛选方法
```java
@Autowired         // 通过 Bean 类型查找
private User user; // 非延迟依赖注入

@Autowired
@Qualifier("user") // 通过 Bean 名称查找
private User user; // 非延迟依赖注入
```
参考 org.springframework.beans.factory.support.DefaultListableBeanFactory#doResolveDependency：
```java
	@Nullable
	public Object doResolveDependency(DependencyDescriptor descriptor, @Nullable String beanName,
			@Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {

		InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint(descriptor);
		try {
        	...
			Class<?> type = descriptor.getDependencyType();
			Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
    		...
        }
		...
	}
```
@Autowired 注解与 @Value 注解均通过 AutowireCandidateResolver 底层实现来决定。

可以利用 AutowireCandidateResolver#getSuggestedValue 拿到被 @Autowired Bean 名称，不过需要通过 DefaultListableBeanFactory 对象来获取。想要拿到 AutowireCandidateResolver 对象的话，需要拿到 DefaultListableBeanFactory，通过通过 BeanFactoryAware 中的 BeanFactory 对象进行强制转型。该操作存在风险，当 ApplicationContext 实现的底层 IoC 容器 BeanFactory 非 DefaultListableBeanFactory（以及它子类）的话。
<a name="Nztrt"></a>
# 需求分析
加速 Spring 应用启动速度，允许并行的初始化 Spring Beans，具体而言，在 IoC 容器启动过程中影响默认初始化方式，即并行初始化非延迟单例 Bean（(non-lazy-init) singletons）。<br />非延迟单例 Bean实际上是绝大多数业务场景的 Beans。<br />需要分析非延迟单例 Bean 之间的依赖关系

本质上是通过并行的方式，加速 Bean 整体实例化 + 初始化过程，初始化过程并不仅仅是 init 方法调用。<br />能够通过 BeanFactory 拆分成分支化的 BeanFactory

<a name="eB97C"></a>
# 实现步骤
<a name="h5JbQ"></a>
## 实现非延迟单例 Beans 依赖关系图
<a name="alWzn"></a>
### 获取所有的非延迟单例 BeanDefinition
通过 BeanDefinition#isSingleton 来判断
<a name="bpXlb"></a>
### 分析 BeanDefinition 之间的依赖关系
以 @Autowired 为例，需要关注构造器注入、字段注入和方法方法，具体实现：AutowiredAnnotationBeanPostProcessor
<a name="VkD4e"></a>
#### AutowiredAnnotationBeanPostProcessor 实现参考
AutowiredAnnotationBeanPostProcessor 能够为单个 Spring Bean 查找依赖注入元信息，并且能够执行注入操作，不过这个操作单个 Spring Bean 迭代操作完成的，并非提前预处理 BeanDefinition。
<a name="z4mIw"></a>
##### MergedBeanDefinitionPostProcessor 接口实现
为 Spring Bean 类找到（并缓存）对应的依赖注入元信息（InjectionMetadata）
```java
	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
		metadata.checkConfigMembers(beanDefinition);
	}
```
<a name="aRxNV"></a>
##### InstantiationAwareBeanPostProcessor 接口实现
为 Spring Bean 属性进行依赖注入处理：
```java
	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
		InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
		try {
			metadata.inject(bean, beanName, pvs);
		}
		...
		return pvs;
	}
```
<a name="XDgyu"></a>
##### SmartInstantiationAwareBeanPostProcessor 接口实现
为 Spring Bean 构造器注入找到（并缓存）对应的依赖注入元信息（InjectionMetadata）：
```java
	@Override
	@Nullable
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName)
			throws BeanCreationException {
        ...
    }
```

<a name="Fx3Kf"></a>
### 基于 microsphere-spring-context 工程中的 io.microsphere.spring.context.event.BeanListener 接口实现
实现 onBeanDefinitionReady 方法，获取到所有的 BeanDefinition 集合

BeanFactory 冻结配置，BeanDefinition 是一个只读集合，在并发场景下可以做到并行分析

<a name="A1ITj"></a>
### 分析 BeanDefinition 依赖关系
借助于 AutowiredAnnotationBeanPostProcessor，仅处理 @Autowired 注解

以 A -> F Beans 为例：<br />"A" -> ["B,"C"]<br />"C" -> ["D"]<br />"D" -> ["E"]<br />"E" -> ["D"]<br />flatten -> ["A","B,"C","D","E"]<br />参考 DefaultSingletonBeanRegistry#getDependentBeans 方法：
```java
	public String[] getDependentBeans(String beanName) {
		Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
		if (dependentBeans == null) {
			return new String[0];
		}
		synchronized (this.dependentBeanMap) {
			return StringUtils.toStringArray(dependentBeans);
		}
	}
```
<a name="WIiYR"></a>
#### 自动化依赖分析
<a name="YNmyS"></a>
#### 手动配置依赖分析
<a name="xQec5"></a>
## 给线程池分配执行分支
假设分支是 N 条，线程是 M 个，每个线程执行 N/M 条分支，通常 N >= M


<a name="w1vNa"></a>
# 相关资源
<a name="Ocbzz"></a>
### [探索｜Spring并行初始化加速的思路和实践](https://mp.weixin.qq.com/s/1xIWCoCdQtAPlcZe3zHusQ)
