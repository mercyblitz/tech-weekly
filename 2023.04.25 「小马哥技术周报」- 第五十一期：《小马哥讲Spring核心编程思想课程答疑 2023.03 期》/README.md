<a name="cVI7K"></a>
# 讨论主题
<a name="bC046"></a>
## 第十章：Spring配置元信息（Configuration Metadata） 
<a name="HkF6c"></a>
### 106 | Spring配置元信息：Spring存在哪些配置元信息？它们分别用在什么场景？
元信息是用于描述或者间接提供编程辅助的信息
<a name="om3xw"></a>
### 107 | Spring Bean配置元信息：BeanDefinition
<a name="EMBHt"></a>
#### GenericBeanDefinition  
通用或者一般性的 BeanDefinition，主要继承于 AbstractBeanDefinition

<a name="DtiYM"></a>
#### RootBeanDefinition 
通常是被 Spring IoC 容器合并应用 BeanDefinition 后的结果，可以通过 MergedBeanDefinitionPostProcessor 来获取<br />如果应用定义 BeanDefinition 类型为 RootBeanDefinition，它将不会被合并

<a name="rmx5s"></a>
#### AnnotatedBeanDefinition
基于注解 BeanDefinition，比如 @Component、@Configuration<br />@Bean 是例外

- ScannedGenericBeanDefinition  - @Component Class 被 @ComponentScan 扫描出来
- AnnotatedGenericBeanDefinition - 普通被注解的 BeanDefinition
- ConfigurationClassBeanDefinition - @Configuration Class

<a name="f1joD"></a>
### 108 | Spring Bean属性元信息：PropertyValues
<a name="r3FSS"></a>
#### PropertyValues
PropertyValues 属于一个 PropertyValue 容器，每个 PropertyValue 针对是属性的内容， name 代表字段（Field）名、Properrty （Method）Name（Setter）、构造器（Constructor）参数名称，value 是一个配置值，它通常是 String 类型的，并且会根据目标成员类型进行转换。

<a name="tbGfS"></a>
#### PropertyValues 来源
原始的数据来源 BeanDefinition，XML 只是配置 BeanDefinition 一种方式<br />修改（可能）的数据来源于：

- InstantiationAwareBeanPostProcessor#postProcessProperties （Since Spring 5.1 +）
- InstantiationAwareBeanPostProcessor#postProcessPropertyValues

修改（可能）的数据将会运用到 Spring Bean 对象，即 AbstractAutowireCapableBeanFactory#applyPropertyValues 方法

建议不要调整 PropertyValues 成员，即 PropertyValue 集合中的成员，容易造成 Bean 字段对应不上，评估调整后的结果是否符合预期。

<a name="wqEVC"></a>
#### PropertyValues 类型转换
支持两种转换方式：

- 传统的 Java Beans PropertyEditor
- Spring 3.0+ ConversionService
<a name="BFxln"></a>
### 109 | Spring容器配置元信息
<a name="tJ41h"></a>
#### Spring 容器 XML 解析过程
<a name="CLiCo"></a>
##### 配置资源定位
XML 资源，Spring 使用 Resource 接口来表达资源<br />XML 资源来自于：

- 文件系统：File System
- ClassPath：URL
<a name="SFXY0"></a>
##### 加载 XML 配置资源
<a name="PhJ2g"></a>
###### 文件系统加载方式 - FileSystemXmlApplicationContext
加载器是 FileSystem
<a name="r50W2"></a>
###### ClassPath 加载方式 - ClassPathXmlApplicationContext
来自于 JVM ClassPath 路径，加载器是 ClassLoader

如何通过 XML 资源转换成 BeanDefinition

<a name="TRomt"></a>
##### 抽象 Spring XML 应用上下文 - AbstractXmlApplicationContext
<a name="HdS8C"></a>
###### 实现类

- 文件系统加载方式 - FileSystemXmlApplicationContext
- ClassPath 加载方式 - ClassPathXmlApplicationContext
<a name="I6a5I"></a>
##### 加载 BeanDefinition 方法 - loadBeanDefinitions
```java
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		...
		loadBeanDefinitions(beanDefinitionReader);
	}

	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);
		}
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);
		}
	}
```
loadBeanDefinitions 方法主要职责是获取 XML 资源，并且将 XML 资源交给 XmlBeanDefinitionReader 进行解析BeanDefinition ，并注册它们（BeanDefinition）到 Spring IoC 容器，即 BeanDefinitionRegistry 对象。


<a name="MoB7m"></a>
##### XML 配置 BeanDefinition 读取器 - XmlBeanDefinitionReader
<a name="c1UFn"></a>
###### 实际 XML 资源加载方法 - loadBeanDefinitions
```java
	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int count = 0;
		for (Resource resource : resources) {
			count += loadBeanDefinitions(resource);
		}
		return count;
	}

	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}

	public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		...
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();
			}
		}
    	...
	}

	protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {

		try {
			Document doc = doLoadDocument(inputSource, resource);
			int count = registerBeanDefinitions(doc, resource);
			...
			return count;
		}
		...
	}
```

doLoadBeanDefinitions 详单与 XML Resource（资源）转化并注册 BeanDefinition：
```java
	public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		int countBefore = getRegistry().getBeanDefinitionCount();
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}
```
registerBeanDefinitions 方法创建以下对象：

- BeanDefinitionDocumentReader - 默认为 DefaultBeanDefinitionDocumentReader
- XmlReaderContext
```java
	public XmlReaderContext createReaderContext(Resource resource) {
		return new XmlReaderContext(resource, this.problemReporter, this.eventListener,
				this.sourceExtractor, this, getNamespaceHandlerResolver());
	}
```
XmlReaderContext 注入的关键组件：

- XML Resource 对象
- this 对象 - XmlBeanDefinitionReader（内含：BeanDefinitionRegistry）
- XML Namespace 扩展接口 - NamespaceHandlerResolver
<a name="YhG05"></a>
##### XML DOM 文档 BeanDefinition 读取器 - BeanDefinitionDocumentReader
BeanDefinitionDocumentReader#registerBeanDefinitions 方法
```java
	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		this.readerContext = readerContext;
		doRegisterBeanDefinitions(doc.getDocumentElement());
	}
```
BeanDefinitionDocumentReader 解析 beans 节点内容，Schema 扩展机制 NamespaceHandlerResolver 实现。

<a name="YunUO"></a>
### 110 | 基于XML资源装载Spring Bean配置元信息
<a name="CgE4Q"></a>
### 111 | 基于Properties资源装载Spring Bean配置元信息：为什么Spring官方不推荐？
<a name="oWfGS"></a>
### 112 | 基于Java注解装载Spring Bean配置元信息
<a name="GwkrF"></a>
### 113 | Spring Bean配置元信息底层实现之XML资源
<a name="OkTZn"></a>
### 114 | Spring Bean配置元信息底层实现之Properties资源
<a name="fCWeW"></a>
### 115 | Spring Bean配置元信息底层实现之Java注解
<a name="Typ6Q"></a>
### 116 | 基于XML资源装载Spring IoC容器配置元信息
<a name="IJfWQ"></a>
### 117 | 基于Java注解装载Spring IoC容器配置元信息
<a name="p6QIk"></a>
### 118 | 基于Extensible XML authoring 扩展Spring XML元素
<a name="O8TIV"></a>
### 119 | Extensible XML authoring扩展原理
<a name="bmnfW"></a>
### 120 | 基于Properties资源装载外部化配置
<a name="UtWUJ"></a>
### 121 | 基于YAML资源装载外部化配置
<a name="lhDUQ"></a>
### 122 | 面试题
