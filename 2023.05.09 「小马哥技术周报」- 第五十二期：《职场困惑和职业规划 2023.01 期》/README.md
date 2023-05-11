<a name="kHOgR"></a>
# 进群加 V ：mercyblitz-1985
<a name="I25oh"></a>
# 案例分析
<a name="LyDFl"></a>
## 某小伙伴的简历
> <a name="iIOFn"></a>
### 个人优势
> - 我非常擅长和团队进行交流，在业务开发中，兼具产品和工程人的思维逻辑。  
> - 对分布式、高并发有一定的经验
> - 基础能力扎实，对于主流技术有着饱满的热情，在琐碎时间善于学习，精通分布式相关知识以及微服务的相关知识， Spring Boot、Spring Cloud 、Dubbo、Nacos 通过业余的学习，自己也从零到一搭建了基础框架。 
> - 在团队中，我可以充当发动机的角色，不但可以在一线干活，还能够带动其他人的成长，学习能力强，对待工作认真负责。
> <a name="UhNyb"></a>
### 个人不足
> - 英文不太好，但是每天已经学习英语了。
> <a name="WQsfZ"></a>
### 架构能力
> - 流量网关（百万级别）-> 业务网关（十万级别）-> 系统
> - 单元化架构，机房同步偶尔延迟，（没有解决方案）
>    - G、C、Z
>    - 2016 G20
>    - 2015 年 腰斩项目
>       - PC（邮箱）和无线（手机号）
>       - 数万奖金，小团队 1w，7人
>       - 用户组 + 安全产品
>    - 2015 光纤被挖断
>       - 异地多活
>          - 用户组 Tair
>    - 异地多活
>       - 翻车：阿里、蚂蚁、微博、美团、字节跳动
> - 大规模分库分表之后，没有全局 TOP N 的需求，每个表，统计每个表的 TOP N，展示在 C 端
> - 服务端埋点（查看） + 客户端埋点（点赞，跳转到商品服务，引流）
> <a name="z3age"></a>
### 项目经验
> <a name="WWCoO"></a>
#### 项目一：XXX 项目
> <a name="R14ns"></a>
##### 基本情况
> 本人基于10万用户时入职，面临的问题是，整个的语言是 PHP。面临的是在活动的时间，出现白屏，访问异常的问题。针对于这种问题，如何去快速解决当前的问题，是我急需要面临的问题。
> <a name="doHnh"></a>
##### 短期规划
> - 去做了加缓存
> - 优化SQL，解决慢查询
> - 前段资源使用CDN加速
> <a name="yoiZc"></a>
##### 长期规划
> - 数据库上云，Polardb 读写分离，方便查询慢日志
> - 脚手架编写，快速构建新的微服务。部署自动化，做到高效
> - 核心先往 JAVA 迁移，做到稳定可控制
> <a name="WiMKL"></a>
##### 整体架构规划
> - 数据库：MySQL；
> - 缓存：Redis
> - 消息中间件：RocketMQ
> - 配置中心&注册中心：nacos
> - API网关：云原生 Envoy 网关
> - RPC 调用：Dubbo
> - 日志采集：阿里云SLS
> - 链路追踪：ARMS  
> 
思考的因素有：
> 人员素质、上手难度，
> 扩展性、还有生态等等
> <a name="kiIr3"></a>
##### 项目难点
> - 在一开始定基调时需要上云，那么很多东西，都需要调研。 serverless 可以支持流量高峰自动扩容  1. 一开始使用 SAE 内置的注册中心，使用的是 nacos 1.4.2 版本，（阿里云文档又找不到）SCA2.7.0.RELEASE 使用的是 nacos 2.0.2 版本，就会报错。SCA 的 nacos-client 降低到 1.4.2 版本 2. WAF 也会添加 EagleEye-TraceID(ARMS)， MSE网关往下传递的是 x-b3-traceId (zipkin) 就是在做的过程中需要自己不断的去踩坑，去把这些全部做起来。  RocketMQ 的消息轨迹查询，使用 CLOUD，等等。  spring boot 的健康检查堆积 K8s 的 liveiness、readiness   提出 J S 混淆 、加密
> - 关于阿里云的事情有两个事情，一个是上云，一个是阿里云迁移。在上云的过程中，自建MySQL 迁移成为PolarDB 的过程中，我们进行了停机的切换。还有阿里云迁移，是因为不知道为什么，阿里云账号绑定在了一个代理商的下边，我们需要进行切换整个阿里云的资源来走直接的优惠。
> - 关于阿里云的事情有两个事情，一个是上云，一个是阿里云迁移。在上云的过程中，自建MySQL 迁移成为PolarDB 的过程中，我们进行了停机的切换。还有阿里云迁移，是因为不知道为什么，阿里云账号绑定在了一个代理商的下边，我们需要进行切换整个阿里云的资源来走直接的优惠。
> - 关于阿里云的事情有两个事情，一个是上云，一个是阿里云迁移。在上云的过程中，自建MySQL 迁移成为PolarDB 的过程中，我们进行了停机的切换。还有阿里云迁移，是因为不知道为什么，阿里云账号绑定在了一个代理商的下边，我们需要进行切换整个阿里云的资源来走直接的优惠。
> - 20 个微服务的原因是因为当时团队的无序扩张，扩张到技术团队 50 个人。微服务的个数问题。
> 
A 是同学领导，做得不错
> B 是不合作同事，鄙视
> C 是公司老板，不说话
> 大公司：4-5 轮面试


在 Java 中，String s = new String("abc"); 生成了多少对象？<br />A. 1<br />B. 2<br />C. 3<br />D. 不确定

<a name="oZLJn"></a>
## 张同学
<a name="x2uf8"></a>
### 基本情况
机器人公司<br />现实和理想有落差<br />主营业务：

- 机器人
- 智慧医疗

能者多牢<br />半年<br />2-3 小时排查<br />总 1000 次<br />硬件故障：600+，1000 次<br />软件故障：400-， 1000 次<br />直属领导：不作为

- 项目进度
- 员工情绪

抛出问题：XXX<br />执行清单：

- XXXX-XX-XX 完成工作一


阿里云<br />RocketMQ: 2020 年实现营收 1个亿<br />阿里云教育和培训机构<br />考试认证

- 阿里云+ 考试
- 面试上云
- 阿里云入校园
   - 华东师范大学 2019

一年，营收达到 2 个亿，2019 年，1.7亿<br />50 个人，第二个 5 个亿<br />阿里云 FaaS = Function <br />Low Code / No Code<br />Java = Servlet Adapter<br />项目 Dubbo<br />Spring Cloud

<a name="jQRB0"></a>
## 李同学
学历：大专<br />211/985 -> 本科<br />期待加入三“高” 企业

找一个企业“过渡”

MyBatis<br />Zookeeper<br />Dubbo<br />Spring / Spring Boot /Spring Cloud<br />Kafka / RabbitMQ / RocketMQ

场景学习和掌握

技术：核心<br />产品：包装<br />生态：教育、IP 演化、开放平台

GPT<br />Google Guice<br />Apache Derby（Java DB2）

技术国有化：<br />竞争：民企 + 国企<br />内容：硬件 + 软件<br />Linkedin 产品退出中国（大陆）

APM<br />压测 + JVM 优化（Oracle）<br />深入到各种中间件<br />精细化

<a name="juZ6t"></a>
## 赵同学
医疗健康<br />技术的尽头是技术，还是业务？



<a name="duf2v"></a>
# 留言板
<a name="Zmaf4"></a>
## 留言一：小马哥 能讲讲如何设计一个中间件 需要哪些知识 原理方法思想？
<a name="burog"></a>
### 常见问题
在很多 Java 应用中，很多开发人员喜欢使用 INFO 级别作为日志？导致应用日志过多，可能会出现发运维警告！<br />具体问题一：修改成其他级别，修改成本高，开发不乐意，测试不同意<br />解决方案：

- 下策：修改代码 INFO -> DEBUG
- 中策：修改 Logging 配置级别， INFO -> WARN
- 上策：不修改代码和配置，能够屏蔽部分 INFO 日志，并且能够实时生效

JMX 管理

- java.util.logging.LoggingMXBean 适配所有的框架
   - log4j
   - log4j2
   - logback
   - Java Logging
- 运维后台
- 不足：依赖和整合

<a name="VyHsQ"></a>
#### Use Case 1：
```java
private static final Logger logger = LoggerFactory.getLogger("com.acme.TestLogger");

logger.info("ABC"); // logger name = "com.acme.TestLogger" - DENY
```
<a name="Gb9uN"></a>
##### 分析
logger name 可以：

- 精确匹配
- 模糊匹配
- 正则表达式匹配

组合规则（组合模式）：

- logger name  和 logger level
   - 操作符：与、或、异或
<a name="lLKJg"></a>
##### Action（行动）

- 举个目前场景找方案
   - log4j（5%）
   - log4j2（70%）
   - logback（20%)
   - Java Logging（5%）
- 优先处理
   - log4j2
   - logback
   - log4j（可选）
   - Java Logging（可选）
- 方案一般流程
   - 是否有现存方案
      - log4j2 - RegexFilter
      - logback - Filters - TurboFilter
      - Java Logging - java.util.logging.Filter
   - 整合到目标框架
   - 分析方案
      - 整合现状
         - Servlet 容器（90%）
            - Servlet 配置 - web.xml
         - Spring 容器（80%）
            - PropertySource
            - Spring Boot
               - application.properties
         - EJB 容器（5%）
            - @EJB
            - 配置
      - 框架支持程度
         - 寻找“最大公约数”
            - log4j2 - org.apache.logging.log4j.core.Filter
               - 方法 - filter
               - 参数
                  - Logger
                  - Level
                  - Marker
                  - 日志内容（String msg）
                  - 日志参数（Object[] params）
               - 返回类型 - Result
                  - ACCEPT
                  - NEUTRAL
                  - DENY
            - logback - ch.qos.logback.classic.turbo.TurboFilter
               - 方法 - decide
               - 参数
                  - Marker
                  - Logger
                  - Level
                  - 日志内容（String msg）
                  - 日志参数（Object[] params）
                  - Throwable
               - 返回类型 - FilterReply
                  - DENY
                  - NEUTRAL
                  - ACCEPT
            - Java Logging - java.util.logging.Filter
               - 方法 - filter
               - 参数 - LogRecord
                  - Level
                  - sequenceNumber
                  - sourceClassName
                  - sourceMethodName
                  - 日志内容（String message）
                  - ...
                  - 日志名称（String loggerName）
               - 返回类型 - boolean
            - log4j - org.apache.log4j.spi.Filter
               - 方法 - filter
               - 参数 - LoggingEvent
                  - 日志（Category）
                  - 日志名称（categoryName）
                  - 日志级别（Priority）
                  - 日志内容（Object message）
                  - ...
               - 返回类型 - int
                  - DENY = -1
                  - NEUTRAL = 0
                  - ACCEPT = 1
            - “最大公约数”
               - 参数
                  - 日志名称（String loggerName）
                  - 日志级别（String level）
                  - 日志内容（Object message）
               - 方法类型 - Filter.Result
            - 接口定义
```java
public interface Filter {

    /**
     * @param loggerName the logging name
     * @param level      the logging level
     * @param message    logging message or logging message pattern
     * @return {@link Result#ACCEPT} or {@link Result#NEUTRAL} or {@link Result#DENY}
     */
    Result filter(String loggerName, String level, String message);

    /**
     * The result of {@link Filter}
     */
    enum Result {

        ACCEPT,
        NEUTRAL,
        DENY
    }
}
```

   - 交付方案
      - 下策：给应用负责人日志相关的文档（折腾用户，体现不了“我们”的价值）
      - 中策：log4j2 和 logback 以及其他实现统一的逻辑（部分折腾用户）
      - 上策：定制一套规则，适配所有日志框架（不折腾用户，实现标准化）
   - 实现路径
      - 定义配置资源路径
      - 提供获取配置资源路径 API
      - 定义配置资源内容
      - 无缝植入通用 Logging Filter 代码
         - log4j2
            - Filter -> Appender
            - 如何拿到所有的 Appenders
               - N Loggers -> M Appenders -> X Filters
               - Appender -> Filter
                  - 如果 Filter == null，说明没有关联过 Filter
                     - 直接设置指定 Filter
                  - 如果 Filter != null，说明已关联过 Filter
                     - 拿到已关联的 Filter，插入到 CompositeFilter，再将指定 Filter 插入 CompositeFilter，将该 CompositeFilter 反向关联到 Appender
                        - CompositeFilter[0] = Old Filter
                        - CompositeFilter[1] = New Filter

<a name="dfKLS"></a>
## 留言二：有什么书籍推荐和阅读顺序？
<a name="JinFw"></a>
## 留言三：想看小马哥写代码


<a name="DdSnu"></a>
## 许同学
<a name="XHR8V"></a>
### 基本情况

- 非科班出身
- 面试感觉良好
- BI
- 学历不理想

自己项目来做职业敲门砖？<br />HuTool<br />MyBatis Plus<br />XXL-Jobs

<a name="hlnSn"></a>
### 行业方向

- 上层业务
- 底层技术

现状，发现问题，解决问题 -> 优雅解决问题

IoT

<a name="X59dC"></a>
## 老三
学历问题<br />IoT 专业背景



<a name="ZoZ1k"></a>
## 小马哥内容输出
<a name="XPL5H"></a>
### Java 训练营继续 - 第三期、第四期
<a name="QH6Vr"></a>
### Java 基础 - 通过场景来学习技术
和工作密切相关（常见问题）


