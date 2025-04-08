最近本人在网上看到了几个 Java 老八股的问题解析，觉得挺有意思，并参与评论，有评论区小伙伴希望我深入讨论一下，借此机会，作为「小马哥技术周报」第七十七期的议题一起探讨，主要议题如下：

原议题一：[排查了半天问题的同事问我：“类初始化的方法如果有异常，那这个类还在吗？”](https://www.bilibili.com/video/BV1GBXuYWEqZ)

延伸议题：

+ 为什么 Catch 异常类初始化，Java 进程会错误退出？
    - 程序仅 Catch Exception，没有 Catch Error
+ <font style="color:rgb(24, 25, 28);">为什么线程池 submit 方法去执行错误类初始化没有异常堆栈？</font>
    - <font style="color:rgb(24, 25, 28);">因为 Runnable 会被包装成 FutureTask，其 run 方法被回调时，会 catch Throwable 类型异常，这样就覆盖了当前 Error，并且将其转化为 Exception，设置到 Future 对象状态，所以看不到堆栈信息</font>
    - <font style="color:rgb(24, 25, 28);">ThreadPoolExecutor 中仅有 execute(Runnable) 方法是启动线程，并且执行任务</font>
    - <font style="color:rgb(24, 25, 28);">submit 方法中的 Runnable -> Callable, Callable，最终执行传入 Runnable 任务</font>
+ <font style="color:rgb(24, 25, 28);">为什么换成 线程池 execute 方法就能输出异常堆栈呢？</font>
    - <font style="color:rgb(24, 25, 28);">与 submit 与 execute 方法不同，因为 execute 主动 Catch Throwable 异常，并且重新抛出 Throwable 异常</font>
    - <font style="color:#080808;background-color:#ffffff;">execute Runnable 没有正确地捕获异常，会导致线程池不断地创建新线程，当新任务提交</font>
+ <font style="color:rgb(24, 25, 28);">类初始化异常，到底会有哪些具体影响？</font>
    - <font style="color:rgb(24, 25, 28);">类无法再初始化成功，并不意味着一直失败，有可能由于外部条件变化，可以尝试成功</font>
        * <font style="color:rgb(24, 25, 28);">类的结构没有问题，编译时不存在异常，但是运行时失败</font>
    - <font style="color:rgb(24, 25, 28);">抛出 java.lang.NoClassDefFoundError</font>

<font style="color:rgb(24, 25, 28);"></font>

<font style="color:rgb(24, 25, 28);">原议题二：</font>[线程池的核心线程数为什么那么小？](https://www.bilibili.com/video/BV1ojXvYYE96/)

延伸议题：

+ 一台 8 核的机器，为什么 Tomcat 线程可以达到 200 ？
    - 无论机器配置如何，Tomcat 线程池默认核心线程数：10 ，最大数量：200
    - 无法确定 Tomcat Web 服务属于哪种类型
        * 例如：Alibaba 主要两类业务 Java 应用：RPC 服务与 Web 服务
            + RPC 服务主要 CPU 计算来自于 RPC 调用，而非 Web 请求，Tomcat 对于 RPC 来说是一个“额外”负担（Dubbo Spring Boot 提供了非 Web 应用实现）
+ Tomcat 线程池的核心线程数是否为 200 ？
    - 默认核心数：10，最大线程数：200
+ Tomcat 线程池的核心线程数能否直接到 200？
    - 不建议，也不应该，需要评估一下服务器 QPS/TPS 数量
    - 如果 200 核心线程预启动，在 Linux 64 机器上，至少占用 200MB 栈空间（单个 OS 线程占用 1MB 栈空间），不计算在 JVM 堆内存，占用 Sys 预留空间，极端情况下，可能会导致 JVM 进程 OOM 或者 Crash
+ Tomcat 线程池默认核心线程数是否没有意义？
    - Tomcat 线程池核心线程是有意义的，它与 J.U.C 线程池一脉相承
    - Tomcat 线程池默认核心线程会预启动
    - <font style="color:#080808;background-color:#ffffff;">submittedCount 提交请求数量</font>
+ J.U.C <font style="color:rgb(24, 25, 28);">线程池要先入队，Tomcat 线程池没有入队的过程，这样的说法对吗？</font>
    - <font style="color:rgb(24, 25, 28);">错误的结论，Tomcat 线程池也有入队的过程</font>
+ <font style="color:rgb(24, 25, 28);">I/O 密集型和 CPU 密集型线程数是如何推荐使用？</font>
    - <font style="color:rgb(24, 25, 28);">CPU 密集型线程数推荐：N+1</font>
    - <font style="color:rgb(24, 25, 28);">I/O 密集型：2N</font>
    - <font style="color:rgb(24, 25, 28);">比较合理的设置：通过压测，全链路压测，了解应用大概的类型偏好，监控来确定</font>

<font style="color:rgb(24, 25, 28);"></font>

<font style="color:rgb(24, 25, 28);">原议题三：</font>[手写java分布式 日志链路trace id！逼死面试官](https://www.bilibili.com/video/BV1BKXkYWEgR)

延伸议题：

+ 为什么说 UP 主的方案有些重，重在哪里？
    - 拦截器实现方法
        * 静态拦截：通过 SPI 来扩展，优势：性能以及健壮性，缺点：需要开发者对 SPI 熟悉
        * 字节码提升：ASM、CGLIB 以及 ByteBuddy 等框架，优势：性能，无侵入性，缺点：实现成本高，兼容性需要长期维护
            + 往往采用扩展子类，第三方框架无法直接修改，存放在 read-only artifact
            + 可能存在风险：
                - 扩展子类实现容易产生状态读取的问题
                - 父类属于 final class
                    * JPA 约束 Domain Entity，保留默认构造器，并且是非 final 类
                - 如果高版本的 Java，存在 sealed class
        * Java 反射：优势：实现简单，侵入性低，缺点：性能存在一定的开销
+ 为什么 Java 反射真的很慢吗？
    - Java 反射不算慢，但是不是最优解，可以通过 Java Compiler 或者 MethodHandle 来提升
        * 基本结论：直接调用 >MethodHandle(static final) > Java Compiler > LambdaMetadataFactory > Java 反射
        * 测试基准数据：
            + directAccess              avgt   60  0.347 ± 0.002  ns/op
            + invokeExactMethodHandle   avgt   60  3.295 ± 0.017  ns/op
            + invokeMethod              avgt   60  0.582 ± 0.002  ns/op
            + invokeMethodHandle        avgt   60  3.297 ± 0.031  ns/op
            + invokeStaticMethodHandle  avgt   60  0.352 ± 0.003  ns/op
+ 为什么在 Servlet 场景下 UP 主的方案可能重复生成 trace id？
    - Servlet service 方法调用需要考虑存在 forward 或者 include 请求
+ 什么是日志框架 MDC，什么场景下要使用到它？
+ 还有哪些方案能够当前优化分布式日志链路 trace id？
    - 方案一：基于 Servlet Filter 来实现
        * 优点：自义定 tracing 的覆盖范围，通过 <font style="color:#080808;background-color:#ffffff;">urlPatterns</font> 或者 <font style="color:#080808;background-color:#ffffff;">dispatcherTypes</font>
        * <font style="color:#080808;background-color:#ffffff;">缺点：理解相对复杂，尤其是理解 dispatcherTypes，对于优先级高 Filter 而言，它们不会接触到 trace id：N Filters + 1 Servlet</font>
    - 方案二：基于 Servlet <font style="color:#080808;background-color:#ffffff;">ServletRequestListener</font> 来实现
        * 优点：全面覆盖 tracing，不会被 <font style="color:#080808;background-color:#ffffff;">dispatcherTypes 干扰，不会产生重复方法调用</font>
        * <font style="color:#080808;background-color:#ffffff;">缺点：无法自定义 </font>tracing 的覆盖范围
    - 方案三：基于 Spring WebMVC HandlerInterceptor
        * 优点：根据 Spring WebMVC DispatcherServlet 的 URL 的映射来控制 tracing 的范围
        * 缺点：收到高优先级 HandlerInterceptor 的影响，高优先级的 HandlerInterceptor 不会接触到 <font style="color:#080808;background-color:#ffffff;"> trace id。绑定在 Spring WebMVC 框架。</font>

以上三个方法均属于：静态方法拦截。



如果是异步 Servlet，可以基于 <font style="color:#080808;background-color:#ffffff;">AsyncContext 与 AsyncListener 配合实现</font>

<font style="color:#080808;background-color:#ffffff;">如果是异步 Spring WebMVC，需要基于 Spring WebMVC 中的 AsyncHandlerInterceptor</font>





Java 高版本学习路径，资料。



Java 高版本：

+ 高性能：GC、反射调用优化、ClassLoading、I/O
+ 语言现代化：Less Code, Do More，可以使用 Kotlin\ Groovy\ Scala 来替换
+ 高度封装性：Java 9 模块化、反射限制、sealed 类型、record 类型（不变性）





AI 编程

N 卡 CUDA

C++/C 代码

JVM JNI 调用 C++/C 代码

JDK 22





Java 对象类型，是值传递还是引用传递？

答：“值传递”，但是 Java 的引用是语言的特例，与 C++ 不同。

Java 对象类型是 Java 原始类型（int）与 Object 类自由组合



Java 对象（应用对象），是一个容器，包含状态（原始类型与对象类型组合）

Java 类 Layout：

元信息：

+ 成员
    - 字段
        * Java 原始类型（int）与 Object 类自由组合
            + 类型存在自身空间占用
    - 构造器
    - 方法
+ 容器信息
    - Class
    - Package
    - ClassLoader

有状态 Java 对象是一种数据容器的体现，每个字段关联的对象或类型一般在 Heap 占据一定的空间（寻址空间，起始地址和结束地址）

关联采用“地址”引用，而这个引用有一种“数值”。



Heap -> String("Hello,World") 起始地址：#1，结束地址：#10

经过一次 Full GC，请问这个 起始地址和结束地址会发生变化吗？

答案：可能会，可能不会。

