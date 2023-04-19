<a name="YeImf"></a>
# 关联「小马哥技术周报」直播
[第三十九期 解读极客时间《小马哥讲 Spring 核心编程思想》目录大纲](https://www.bilibili.com/video/BV1CJ411r7Fq)<br />[第四十一期 小马哥讲Spring核心编程思想课程答疑 2020.01 期 - P1 至 P26](https://www.bilibili.com/video/BV1P7411x7rz)<br />[第四十二期 小马哥讲Spring核心编程思想课程答疑 2020.01 期（续）- P25 至 P42](https://www.bilibili.com/video/BV1K7411x72S/)<br />[第四十三期 小马哥讲Spring核心编程思想课程答疑 2020.02 期 -  P51 至 P70](https://www.bilibili.com/video/BV1RE41177Yx)<br />[第四十四期 小马哥讲Spring核心编程思想课程答疑 2020.03 期 - P71 至 P85](https://www.bilibili.com/video/BV1gE41137Xc)

<a name="z7IVU"></a>
# 小马哥讲Spring核心编程思想
<a name="krDYf"></a>
## 第九章：Spring Bean生命周期（Bean Lifecycle） 
<a name="hekf6"></a>
### [86 | 课外资料：Spring Cloud RefreshScope是如何控制Bean的动态刷新？](https://time.geekbang.org/course/detail/265-206756)
<a name="TBXOF"></a>
### 
<a name="dkm4H"></a>
### [89 | Spring Bean 元信息解析阶段：BeanDefinition的解析](https://time.geekbang.org/course/detail/265-206766)

```java
@Import(MyConfiguration2.class)
@Configuration
public class MyConfiguration1{ // MyConfiguration1 -> MyConfiguration2 -> MyConfiguration3
    
}

@Import(MyConfiguration3.class)                   
class MyConfiguration2 { 
}
```

<a name="rfq44"></a>
### [90 | Spring Bean 注册阶段：BeanDefinition与单体Bean注册](https://time.geekbang.org/course/detail/265-206767)

<a name="lDJC4"></a>
#### 单体手动注册


```java
class A {

    @Autowired B b;
    
}

A a = beanFactory.getBean(A.class);

// 再次注入，假设 Bean B 它是 scope = prototype
// A Bean 中的 b 对象会重新生成
beanFactory.autowireBean(a);
```


<a name="ezGJn"></a>
#### Bean 实例化方法
<a name="pPeAo"></a>
##### 策略接口 - org.springframework.beans.factory.support.InstantiationStrategy

- BeanDefinition Class 策略
- BeanDefinition 构造器策略
- FactoryBean 构造策略

```java
class A {
    @Autowired B b;
}

class B {
    @Autowired A a;
}

A 初始化需要依赖 B 对象（不需要初始化完成，early object， B 对象还未 @Autowired A Bean）
```


<a name="JKLVv"></a>
### [91 | Spring BeanDefinition合并阶段：BeanDefinition合并过程是怎样出现的？](https://time.geekbang.org/course/detail/265-209704)

MergedBeanDefinitionPostProcessor 回调应早于 Bean 实例创建


CDI = Contexts and Dependency Injection



<a name="Vl3W6"></a>
### [92 | Spring Bean Class加载阶段：Bean ClassLoader能够被替换吗?](https://time.geekbang.org/course/detail/265-209705)

<a name="CGO1p"></a>
### [93 | Spring Bean实例化前阶段：Bean的实例化能否被绕开？](https://time.geekbang.org/course/detail/265-209706)

CGLIB  Bean 提升时会产生子类，子类对象集成原始 Bean 类型
```java
class A {

    @PostConsturct
    public void init(){
    }

    @PreDestroy
    public void destroy(){
        
    }
}

class A$$CGLIB$... extends A { // CGLIG 提升类

    // CGLIG 提升类类同样继承了 A 所有的生命周期方法
    
}

A a = beanFactory.getBean(A.class); // 实际获取的 A bean 类型是 “A$$CGLIB$...”
```

如果是 JDK 动态代理的话，假设 Bean 类型为 B
```java
class B implements InitializingBean {

    @Overrode
	public void afterPropertiesSet() throws Throwable {
    }

    @PostConsturct
    public void init(){
    }
}

Class[] interfacesB = {InitializingBean.class}

Oject proxy = Proxy.newProxyInstance(classLoader,interfacesB, InvocationHandler());


B b = beanFactory.getBean(B.class); // 实际获取的 B bean 类型是 JDK 动态代理子类 "Proxy$1"


类 Proxy$1 是由 JDK 动态代理在运行时动态生成，它继承了 java.lang.reflect.Proxy，
同时实现了  Proxy.newProxyInstance 传递接口类型 Class[]，以当前为例，

class Proxy$1 extends java.lang.reflect.Proxy implements InitializingBean {

    private Object instance; // 模拟代理

    private Method afterPropertiesSetMethod; // 模拟代理

    // private InvocationHandler handler; // 实现 InvocationHandler 类型
    // 来自于 super Class java.lang.reflect.Proxy.h

	@Overrode
	public void afterPropertiesSet() throws Throwable { // 模拟代理
        // 通过反射调用了
        h.invoke(this,afterPropertiesSetMethod,new Object[0]);
    }
    
}

// 实际实现类为：org.springframework.aop.framework.JdkDynamicAopProxy
class BInvocationHandler implements InvocationHandler { // 模拟实现

    private Object source;

    @Override
	public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
            // 相当于调用了接口的目标方法
            method.invoke(source,args);
    }

}

@Transactional // Transaction AOP
@Cacheable // Caching AOP
public void set(...) {
    
}


```
<a name="UgjuS"></a>
### [94 | Spring Bean实例化阶段：Bean实例是通过Java反射创建吗？](https://time.geekbang.org/course/detail/265-209707)

<a name="s9Oea"></a>
### [95 | Spring Bean实例化后阶段：Bean实例化后是否一定被是使用吗？](https://time.geekbang.org/course/detail/265-209708)


下期再讨论
<a name="wxjks"></a>
### [ 96 | Spring Bean属性赋值前阶段：配置后的PropertyValues还有机会修改吗？](https://time.geekbang.org/course/detail/265-209709)

<a name="pPV67"></a>
### [ 97 | Aware接口回调阶段：众多Aware接口回调的顺序是安排的？](https://time.geekbang.org/course/detail/265-209711)

<a name="KynFV"></a>
### [ 98 | Spring Bean初始化前阶段：BeanPostProcessor](https://time.geekbang.org/course/detail/265-209714)

<a name="m8JSs"></a>
### [ 99 | Spring Bean初始化阶段：@PostConstruct、InitializingBean以及自定义方法](https://time.geekbang.org/course/detail/265-212100)

<a name="MXbuu"></a>
### [ 100 | Spring Bean初始化后阶段：BeanPostProcessor](https://time.geekbang.org/course/detail/265-212101)

<a name="alg1M"></a>
### [ 101 | Spring Bean初始化完成阶段：SmartInitializingSingleton](https://time.geekbang.org/course/detail/265-212103)

<a name="WMD8K"></a>
### [ 102 | Spring Bean销毁前阶段：DestructionAwareBeanPostProcessor用在怎样的场景?](https://time.geekbang.org/course/detail/265-212105)

<a name="mB1NP"></a>
### [ 103 | Spring Bean销毁阶段：@PreDestroy、DisposableBean以及自定义方法](https://time.geekbang.org/course/detail/265-212106)

<a name="LZYHB"></a>
### [ 104 | Spring Bean垃圾收集（GC）：何时需要GC Spring Bean？](https://time.geekbang.org/course/detail/265-212107)

<a name="u41bZ"></a>
### [ 105 | 面试题精选](https://time.geekbang.org/course/detail/265-212108)
