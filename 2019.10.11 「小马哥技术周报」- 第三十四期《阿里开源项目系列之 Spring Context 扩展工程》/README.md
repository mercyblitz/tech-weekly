## 「小马哥技术周报」- 第三十四期《阿里开源项目系列之 Spring Context 扩展工程》





## 使用场景



### 工具类 - `com.alibaba.spring.util`

为 `spring-core` 提供工具类的补充

>  `spring-core` 模块里面提供大量的工具类：
>
> - StringUtils





### Spring Beans 相关扩展 - `com.alibaba.spring.beans`



#### Spring Bean 生命周期后置处理器 - `BeanPostProcessor`



##### 基本语义

- 处理 Bean 初始化生命周期，包括 before 和 after

- 在回调方法处理时，可能会对象变化，不过多数情况是不需要变化类型，即改变当前 Bean 参数对象中的状态即可

  - 比如：

  - ```java
    class MyBeanPostProcessor implements BeanPostProcessor {
    
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (ClassUtils.isAssignable(bean.getClass(), CharSequence.class)) { // 凡是 Bean 类型为 CharSequence 的子类
                return bean.toString();                                         // 被转化 String 类型
            }
            return bean;
        }
    }
    ```

    

##### 实现细节

- 在 Spring Framework 5.0 之前，需要完全实现 `BeanPostProcessor` 接口定义的方法：
  - 初始化前 - postProcessBeforeInitialization
  - 初始化后 - postProcessAfterInitialization

- 从 Spring Framework 5.0 + 开始，`BeanPostProcessor` 接口提供了默认实现，即无操作返回

> 使用不便的地方：
>
> - 对 bean 类型通常需要自行判断



N Spring Beans -> M  `BeanPostProcessor`  -> Ops Count = N * M



##### 与 BeanFactory 关系

关联关系 - `BeanPostProcessor` 与 `BeanFactory ` 是 N 对 1 的的关系，即一个 Spring BeanFactory 实例可以关联 N 个 `BeanPostProcessor` ， `BeanPostProcessor`  来源：

- 显示地插入，即调用  `ConfigurableBeanFactory#addBeanPostProcessor` 方法
-  `BeanPostProcessor` 定义成普通 Spring Bean，即 `AbstractApplicationContext#registerBeanPostProcessors`



#### Spring BeanFactory 生命周期后置处理器 - `BeanFactoryPostProcessor`

##### 基本语义

自定义处理 `ConfigurableListableBeanFactory`，当它已经被基本处理完成，即 `AbstractApplicationContext#prepareBeanFactory` 方法处理



##### 生命周期回调方法 - `postProcessBeanFactory`

- 方法参数 - `ConfigurableListableBeanFactory`

- 回调时机 - `AbstractApplicationContext#invokeBeanFactoryPostProcessors`

  - 回调 `BeanFactoryPostProcessor`
  - 回调 `BeanDefinitionRegistryPostProcessor` - 注册 `BeanDefinition`

  

##### 与 ApplicationContext 的关系

关联关系 - `BeanFactory` 与 `ApplicationContext` 是 1 对 1 的关系，绝大多数情况， `BeanFactory` 的实现是 `DefaultListableBeanFactory`，并且 `ApplicationContext` 实现类是 `AbstractApplicationContext` 的子类

`BeanFactoryPostProcessor` 与 `ApplicationContext` 是 N 对 1 的关系：

```java
public abstract class AbstractApplicationContext ... {
    ...
 	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>(); // 有序       
    ...
}
```

`BeanFactoryPostProcessor` 的来源：

- 显示地插入，即调用 `AbstractApplicationContext#addBeanFactoryPostProcessor` 方法
- `BeanFactoryPostProcessor`  定义成普通 Spring Bean，即 `AbstractApplicationContext#invokeBeanFactoryPostProcessors` 方法处理



#### Spring Bean 定义 - `BeanDefinition`

##### 基本语义

`BeanDefinition` 是 Bean 声明元数据的一种描述接口

- 主要元数据指标：
  - Bean 类型 - `getBeanClassName()`
  - Parent Bean 名称 -`getParentName()`
  - 工厂 Bean 名称 - `getFactoryBeanName()`
  - 工厂方法名称 - `getFactoryMethodName()`
  - Bean 生命范围  - `getScope()`
- 次要元数据指标
  - Bean 是否懒加载 - `isLazyInit()`
  - Bean 是否为 Primary - `isPrimary()`



##### 黑科技

- 特殊元数据指标
  - Bean 定义的来源 - `setSource(Object)` - 区分不同 Bean 定义一种手段
  - Bean 定义属性上下文 - `setAttribute(String,Object)` - 临时存储于当前 Bean 定义相关的属性，它不影响 Bean 实例化/初始化，然而可以辅助 Bean 初始化





### BeanFactory 接口

子接口 

- ListableBeanFactory - 在 BeanFactory 基础上，主要职责是扩展对 BeanDefinition 管理，以及 Bean 对象集合返回
- HierarchicalBeanFactory - 层次性的 BeanFactory ，类似于 ClassLoader 双亲委派
- ConfigurableBeanFactory - 可配置（可写）BeanFactory，相对于其他 BeanFactory 只读的特性



## 常见问题



### BeanFactory 和 FactoryBean 的区别和联系？

`FactoryBean` -  创建特定 Bean 的工厂，其对象是 BeanFactory 一个普通 Bean

主要特性

- 指定 Bean 类型 - `getObjectType()` 方法
- 获取/创建 Bean - `getObject()` 方法
- 创建 Bean 是否为单例 - `isSingleton()` 方法



举例 - `UserFactoryBean` 创建 `User` Bean，通常应用关心 `User` Bean ，而非 `UserFactoryBean`  Bean



`BeanFactory` 是 Spring Bean 容器（接口），也管理 `FactoryBean` Bean 集合。