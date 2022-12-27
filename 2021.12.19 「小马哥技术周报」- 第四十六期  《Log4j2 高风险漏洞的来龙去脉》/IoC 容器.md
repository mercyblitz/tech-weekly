IoC 容器

● 类型：核心
● 时间：60 分钟
● 主要内容
  ○ IoC 与 DI ：介绍 IoC（控制反转）原理、以及 依赖注入（DI）
  ○ Bean ：Bean 的命名、Bean 的构造、Bean 的作用域、Bean 的生命周期，以及 Bean 的定义
  ○ 配置：XML 配置方式、Annotation 配置方式以及 Java 配置方式
  ○ 组件管理：组件扫描和管理，介绍@Component和其 stereotype Annotation
  ○ Environment 抽象：介绍Environment、PropertySource接口和@PropertySource注解，以及占位符的运用
  ○ ApplicationContext：介绍 Spring 应用上下文ApplicationContext以及其派生接口使用场景
  ○ BeanFactory：介绍 Spring Bean 管理接口 BeanFactory



Spring Boot （FAT JAR/WAR 启动）开始以 Spring ApplicationContext 来构建

Spring Web （Servlet 容器启动）驱动 Spring ApplicationContext



Spring Web MVC

Spring WebFlux(Reactor)



Spring Data

Spring Security

Spring Session





A -> B -> C

   -> D



需求 -> 接口





组件（内和外之别）：容器级别（基础设施）组件（内）与容器之外（应用）组件（外）

组件角色（role）：

ROLE_APPLICATION

ROLE_INFRASTRUCTURE



组件作用域（Scope）：Bean Scope

- 单例：是在单个上下文中的单一对象，上下文 BeanFactory，ID 或者名称来标识（Bean Name）

FastDateFormat（线程安全）可以唯一对象

- 原型（多例）：复制品，在某个线程或某次执行是唯一，独立

SimpleDateFormat（线程不安全）

Request

Response

- request
- session
- application：一个 Servlet 应用（可以包含多个 Spring 应用上下文）
- 自定义 scope



@Autowired



@Resource -> JNDI



## 依赖注入



### 注入类型

#### 构造器注入

A 注入 B 

阶段？

- A 在实例化阶段来依赖注入 B
- B 是一个已初始化组件
  - B 实例化
  - B 初始化
    - 属性设置
    - 生命周期回调



#### Setter 注入

A 注入 B 

阶段：

- （已完成）A 实例化
- （正在进行）A 初始化
  - 属性设置 - populateBean
    - 非 Bean 属性（String name）
    - 注入 B
  - 初始化 - initializeBean 
    - 回调接口（InitializingBean）



#### 参数注入

参数注入和 Setter 注入，均属于方法注入

参考：

- @Autowired
- AutowiredAnnotationBeanPostProcessor.AutowiredMethodElement





#### 字段注入

A -> B(Early) -> A(Early)



### Bean 生命周期



@Lazy

Early

A -> B -> C -> D（50%）

容器启动完成

C -> D



### Bean 定义

Bean 类型

Bean 角色

Bean 作用域

Bean 依赖注入方法

Bean 生命周期方法

- 方式
  - 显性，指定 init-method=""
  - 隐形，实现 InitializingBean 接口
- 类型
  - 初始化
  - 销毁



帮助 IoC 容器根据 Bean 定义（配置），控制 Bean 行为



### Spring 配置

spring.application.name

spring.profiles.active = prod