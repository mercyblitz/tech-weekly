> ### 主要内容
> + Java ThreadPoolExecutor 
>     - 参数的使用场景
>     - 动态参数设置
>     - 线程预热
>     - 状态获取
> + Java Threading 
>     - 可观测性
>     - Code Review 一段代码
> + 问答互动（如果时间允许）
>     - 关于接口幂等性设计
>     - 关于虚拟线程
>



## Java ThreadPoolExecutor
### 参数的使用场景
##### 参数列表
+ <font style="color:#080808;background-color:#ffffff;">int corePoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">int maximumPoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">long keepAliveTime</font>
+ <font style="color:#080808;background-color:#ffffff;">TimeUnit unit</font>
+ <font style="color:#080808;background-color:#ffffff;">BlockingQueue workQueue</font>
+ <font style="color:#080808;background-color:#ffffff;">ThreadFactory threadFactory</font>
+ <font style="color:#080808;background-color:#ffffff;">RejectedExecutionHandler handler</font>



### 动态参数设置
ThreadPoolExecutor 提供了部分 setter 方法以及部分调整内部状态的方法：

+ <font style="color:#080808;background-color:#ffffff;">setCorePoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">setMaximumPoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">setKeepAliveTime</font>
+ <font style="color:#080808;background-color:#ffffff;">setThreadFactory</font>
+ <font style="color:#080808;background-color:#ffffff;">setRejectedExecutionHandler</font>
+ <font style="color:#080808;background-color:#ffffff;">allowCoreThreadTimeOut : 是否允许核心线程超时</font>



以上方法均为线程安全的实现。



问题列表：

+ 如何动态地调整 BlockingQueue？
+ 如何监控单个任务执行时间
+ 如何告警超时或者拒绝策略
    - 告警通知
        * Chat
        * Email
        * Voice Call



> 可以通过 Wrapper 或者装饰器模式来实现（静态代理）
>





### 状态获取
ThreadPoolExecutor 提供了状态获取的方法：

+ <font style="color:#080808;background-color:#ffffff;">getCorePoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">getMaximumPoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">getPoolSize</font>
+ <font style="color:#080808;background-color:#ffffff;">getActiveCount</font>
+ <font style="color:#080808;background-color:#ffffff;">getCompletedTaskCount</font>
+ <font style="color:#080808;background-color:#ffffff;">getTaskCount</font>
+ <font style="color:#080808;background-color:#ffffff;">...</font>

### 线程预热
ThreadPoolExecutor 默认状态是通过 execute 方法提交一个任务后，来创建工作线程执行，创建线程需要一定的启动成本，需要提升运行时间，最好 ThreadPoolExecutor 提供线程预热：

+ <font style="color:#080808;background-color:#ffffff;">prestartAllCoreThreads</font>
+ <font style="color:#080808;background-color:#ffffff;">prestartCoreThread</font>

<font style="color:#080808;background-color:#ffffff;">预热核心线程，因为 Max 线程需要依赖 BlockingQueue 的状态。</font>

## Java Threading
### 可观测性
#### Java Thread 状态
java.lang.Thread.State

#### Java ThreadPoolExecutor 状态
> JVM 应用如何有效地管理 ThreadPoolExecutor
>
> + Spring 应用 - 线程池扩展
>     - org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
>         * <font style="color:#080808;background-color:#ffffff;">TaskDecorator</font>
> + Spring Boot 应用 并使用 <font style="color:#080808;background-color:#ffffff;">TaskExecutor 作为 Spring Bean</font>
>     - <font style="color:#080808;background-color:#ffffff;">如果整合 Micrometer，它将自动地装配 Metrics 信息</font>
>

##### Micrometer 与 ThreadPoolExecutor 整合
API - io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics



##### Netflix/Servo 与 ThreadPoolExecutor 整合
[https://github.com/Netflix/servo](https://github.com/Netflix/servo)



##### JMX 与 ThreadPoolExecutor 整合
Tomcat 线程池 JMX Bean



#### Java Threading Native 状态
##### JDK 工具 - jstack 
+ Java Threads
+ JVM Threads
    - GC Threads
    - C1/C2 Threads



### Code Review 一段代码
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1723537749046-69e952f9-98d8-467e-b687-b8637a0b4764.png)

#### 优化点
+ <font style="color:#080808;background-color:#ffffff;">不应该使用 ThreadMXBean#getAllThreadIds() 方法，它约等于执行一次 jstack，也会发生停顿</font>
+ <font style="color:#080808;background-color:#ffffff;">如果需要获取系统负载的信息，可以通过其他途径</font>
    - <font style="color:#080808;background-color:#ffffff;">已有的监控系统 Zabbix 等</font>
    - <font style="color:#080808;background-color:#ffffff;">Prometheus Metrics</font>
        * <font style="color:#080808;background-color:#ffffff;">Micrometer + ThreadPoolExecutor 整合</font>
    - <font style="color:#080808;background-color:#ffffff;">JMX API com.sun.management.OperatingSystemMXBean</font>
        * <font style="color:#080808;background-color:#ffffff;">JMX API 获取 java.lang.management.OperatingSystemMXBean，转型到 com.sun.management.OperatingSystemMXBean</font>



#### 获取 ThreadInfo 信息
+ 尽可能地使用批量方法 <font style="color:#080808;background-color:#ffffff;">getThreadInfo(long[]) ，除非查询独立线程信息</font>



#### 获取 JVM 线程信息
+ 推荐使用 <font style="color:#080808;background-color:#ffffff;">Metrics 系统中的数据</font>
    - <font style="color:#080808;background-color:#ffffff;">Micrometer + ThreadPoolExecutor 整合</font>



#### 关联问题
##### 线上执行 jstack 会出现什么的问题或影响？
    - 会发生停顿，Safe-Point 停顿（STW）

##### 如何动态地调整 BlockingQueue？
场景是相对狭隘的

Wrapper （静态代理）

> 动态代理尽量使用在低频操作场景
>

```java
public class DelegatingBlockingQueue<E> implements BlockingQueue<E> {

    private final BlockingQueue<E> delegate;

    public DelegatingBlockingQueue(BlockingQueue<E> delegate) {
        this.delegate = delegate;
    }

    ...
}
```

##### 如何监控单个任务执行时间？
###### 普通 Java 应用
+ 方法一：扩展 <font style="color:#080808;background-color:#ffffff;">ThreadPoolExecutor 执行会方法</font>
    - <font style="color:#080808;background-color:#ffffff;">beforeExecute</font>
    - <font style="color:#080808;background-color:#ffffff;">afterExecute</font>
+ 方法二：包装 ThreadFactory 实现
    - 通过反射的方式来获取 Worker 对象
    - 通过再次包装 Worker Runnable 对象



###### Spring 应用
+ 方法一：<font style="color:#080808;background-color:#ffffff;">ThreadPoolTaskExecutor 使用场景</font>
    - <font style="color:#080808;background-color:#ffffff;">实现 TaskDecorator</font>

##### 如何告警超时或者拒绝策略
+ 告警通知
    - Chat
    - Email
    - Voice Call



建议可以和监控系统整合，Java 应用提供数据指标给平台。

## 问答互动
### 关于接口幂等性设计
#### 幂等性 
来自于数学函数原则，当输入不变时，重复操作得到的结果是恒定。

比如 HTTP GET 方法属于幂等方法。



消息系统：购买系统到交易系统发送一条交易消息，消息在指定的时间内未被消费，MQ 重新发送消息，交易系统可能受到多条相同的消息，这时需要系统提供幂等性保护。



控制幂等性的粒度，本质上输入的内容（条件）。



数据存储幂等性设计

+ 数据库幂等性设计
    - 应用策略：存在性判断
        * 优点：性能与可维护性
        * 缺点：存在漏洞
    - 存储兜底：唯一性约束
        * 优点：平台和语言无关性
        * 缺点：性能与可维护性

MQ 幂等性设计

+ 强烈依赖于 MQ 实现



幂等性与一致性不是一类概念

#### 防重复提交
属于应用层面防护，通常采用集中式存储来判断请求是否重复，比如：Redis、MySQL 等。



CSRF 防请求伪造

防重复提交 Token，不等于 CSRF Token，它也不能作为幂等性的保护

通过用户主导，比如按钮或者重新加载等手段，通过某种客户端来完成。



#### 平台是否允许重复交易？
属于业务领域的范畴（规则）。

平台允许重复交易，重复交易同一个商品，其实是不同的视图。



### 关于虚拟线程


## 关联资源
### 技术文章：[美团《Java线程池实现原理及其在美团业务中的实践》](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
### 开源项目：[Dromara/DynamicTp](https://gitee.com/dromara/dynamic-tp)




> [https://github.com/microsphere-projects/microsphere-devops](https://github.com/microsphere-projects/microsphere-devops)
>











