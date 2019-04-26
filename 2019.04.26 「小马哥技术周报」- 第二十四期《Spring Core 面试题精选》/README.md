# 2019.04.26 「小马哥技术周报」- 第二十四期《Spring Core 面试题精选》



## 精选



### 为什么要使用 Spring？

### 

### 解释一下什么是 IoC？

好莱坞原则

BeanFactory



Spring -> Interface21

BeanFactory

1.0 ~ 2.0 XML 主导

1.2+ ~ 5.2（PRE） 注解

2.0 注解



<bean id="user" class="User" >



<bean class="XXX">

​	<property bean-ref="user" />

</bean>

### Spring 中的 bean 是线程安全的吗？

Bean 对象的本身是否为是不确定的。



<bean id="map" class="java.util.concurrent.ConcurrentHashMap" />

>  获得 Bean 是线程安全



### Spring 常用的注入方式有哪些？

Setter（方法参数）

构造器（参数）

方法注入

```java
// @Autowired 可选
public User user(Money money){
    
}
```



### Spring 支持几种 bean 的作用域？

```xml
<bean id="map" class="java.util.concurrent.ConcurrentHashMap"  scope="prototype" />
```



request -> ServletRequest



Servlet 引擎的线程模型 1 Thread -> 1 ServletRequest

ServletRequest 是线程安全的



HTTP -> Tomcat(NIO) -> Thread -> Request( Bean)



```java
@RestController
public class MyController {
    
    @Autowired
    private User user;
    
    @GetMapping("/user/xxx")
    public void execute(){
        user.getName();
    }
}
```



JSP、JSTL、EL

Page - `PageContext`

Request - `ServletRequest`

Session - `HttpSession`

Application - `ServletContext`



### Spring 自动装配(Autowired) bean 有哪些方式？

Type

Name

Constructor

Auto detected

`AUTOWIRE_NO`

`AUTOWIRE_BY_NAME`

`AUTOWIRE_BY_TYPE`

`AUTOWIRE_CONSTRUCTOR`

`AUTOWIRE_AUTODETECT`



### 如何理解 Bean 的生命周期？



AbstractAutowireCapableBeanFactory

- invokeAwareMethods

ApplicationContextAwareProcessor

- ApplicationContextAware
- BeanClassLoaderAware
- ...

BeanPostProcessor

- InstantiationAwareBeanPostProcessor
  - SmartInstantiationAwareBeanPostProcessor

- postProcessBeforeInitialization()
- postProcessAfterInitialization()

InitializaingBean 

- afterPropertiesSet()

DisposableBean

- destroy()



### Spring 事务实现方式有哪些？



### 说一下 Spring 的事务隔离？



### @Autowired 的作用是什么？

@Autowired  用于依赖注入，Spring 官方不推荐使用，推荐使用构造器注入。不能修饰 static 字段



QQ 3群：982215002