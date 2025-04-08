## 问答互动
### 问题一：<font style="color:rgb(47, 48, 52);">小马哥，openfeign 如何动态化，就是A服务要调用其他服务，但是一开始我是不知道要调用谁的，所以一开始我没法写好feign接口给A服务用。 怎么做到运行时，调用任意服务的任意接口呢？有点像泛化调用。</font>
答：

解决方案一：的确是泛化调用，这个需要设计一个特殊的接口，可以参考Dubbo 泛化接口设计，也可以把这个功能放到网关上

解决方案二：搞个 API 网关，OpenFeign 接口去调用网关



前提：具备一个服务提供接口目录，提供具体 Web Service URI，请求参数（HTTP Request -> 具体方法参数），通过契约/约定的方式让方法参数转换成 HTTP Request

建议：RestTemplate、WebClient



### 问题二：小马哥，请问rpc调用时的熔断时长和超时时长之间有什么关系吗，如果有关系谁大谁小呢，面试被问到这个问题懵了一下
答：一般熔断时间可以参考超时时间，不过没有必然联系。熔断是为了保护服务上游，而超时实际是最大可接受的时限。两者都会影响吞吐量，通常熔断时间小于超时时间

熔断不是必须的，属于服务容错性、韧性

超时时间客户端最大可接受的处理时间，经验值或默认值



### 问题三：小马哥，腾讯云这种故障，配置什么样的灰度发布才能预防，如何识别出流量特征把各种类型的流量都引一点来灰度？灰度发布之前有哪些工作可以做？如何做自动化兼容测试？
> [腾讯云 4 月 8 日故障复盘及情况说明](https://www.oschina.net/news/287629)
>

答：<font style="color:rgb(47, 48, 52);">要具体看基础设施，一般通过流量标识来控制请求理由，凡是数据交互的点基本上会涉及灰度，比如RPC，网关，数据库以及消息等，通常数据持久层可以没有特殊灰度物理设施，采用逻辑控制</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">预防的前提是单元测试充分</font>

<font style="color:rgb(47, 48, 52);">灰度发布需要结合 Tracing 系统来看本次功能点流量是否覆盖，会有流量标签来识别是否为灰度</font>

<font style="color:rgb(47, 48, 52);">grayFlag 、gray</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">Team-1 ：Dev-1</font>

<font style="color:rgb(47, 48, 52);">Team-2 ： Dev-2</font>

<font style="color:rgb(47, 48, 52);">Git Branch：release-001</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">Staging : release-001</font>

<font style="color:rgb(47, 48, 52);">Dev-1(4.19) > release-001</font>

<font style="color:rgb(47, 48, 52);">Dev-2(5.1) > release-001</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">Prod</font>

<font style="color:rgb(47, 48, 52);">Rebuild Staging -> Dev-1(4.19) > release-001</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">Test -> Staging -> Prod</font>

<font style="color:rgb(47, 48, 52);"></font>

### <font style="color:rgb(47, 48, 52);">问题四：马哥，急救，最近遇到了一个问题。SpringBoot+Freemarker，模版总是第一次访问就很慢，第二次访问就快多了，有什么优化思路嘛。我已经使用了freemarkerConfiguration.getTemplate去预加载，且配置了缓存FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();resolver.setCache(true);但是发现第一次访问还是很慢，trace看，耗时都在“Render xxx”里面，长达好几秒甚至十几秒</font>
答：类似于 <font style="color:rgb(47, 48, 52);">Freemarker 模板引擎，在第一次渲染时，将模板文件转化成可执行代码，通过 Java 反射去查找部分方法，getter 方法或者部分可执行方法。同时，由于 JVM JIT 关系，它需要一点时间来处理一些字节码。</font>



ApplicationRunner 去 load 那些页面，提前让其处理，预览地址 URI ，通过 RestTemplate 访问一下



Spring WebMVC 中，@Controller 处理方法返回值即为模板资源相对路径，ViewResolver#<font style="color:#080808;background-color:#ffffff;">resolveViewName 来定位 View，然后再通过 View#render 方法渲染，通常实现为委派给模板引擎处理。</font>

```java
@Controller
public class DemoController {

    @GetMapping("/demo")
    public String demo(Model<String,?> model){
        // prefix : classpath:
        // suffix: .flt
        // classpath:/demo.flt
        model.put("xxx",someBean);
        return "/demo";
    }
}
```

<font style="color:rgb(90, 92, 102);background-color:rgb(247, 249, 250);"></font>

<font style="color:rgb(90, 92, 102);background-color:rgb(247, 249, 250);"></font>

### <font style="color:rgb(47, 48, 52);">问题五：马哥，如何模拟tomcat  java.net.SocketException: Broken pipe 异常？</font>
答：<font style="color:rgb(47, 48, 52);">Broken pipe 异常出现条件：当客户端访问服务器发生超时，客户端 Socket 被关闭，服务器仍在响应请求，但是发现客户端 Socket 已经关闭，所以会抛出异常 java.net.SocketException: Broken pipe。可以默认超时自动关闭 Socket 连接的方式，你这类模拟没有达到超时关闭的条件。</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);"></font>

### <font style="color:rgb(47, 48, 52);">问题六：小马哥，给我画出一个支持 1W qps高并发，高可用的架构图，一到四期的课程完全没有部署架构图，我强烈要求补充！</font>
答：这个问题存在问题。



1W QPS 这是一套系统性能指标，缺少上下文，如：用户场景、数据库选型、数据量、服务调用链路



计算模型：Fork-Join



<font style="color:rgb(47, 48, 52);">高并发：网卡撑得住</font>

<font style="color:rgb(47, 48, 52);">HTTP/1.1 ( Request - Response )</font>

<font style="color:rgb(47, 48, 52);">域名 -> DNS -> 多网关地址列表 -> 接入网关 -> 网卡 -> 后端服务 -> 多层服务调用 -> 同步、异步</font>

<font style="color:rgb(47, 48, 52);"></font>

<font style="color:rgb(47, 48, 52);">HTTP/2 ( Request - Response + Stream ) </font>

<font style="color:rgb(47, 48, 52);"></font>

![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/222258/1713537903993-365a8a5c-95bc-4178-be8d-b9dd7e587834.jpeg)



### 问题七：
官：分库分表的分片键你们是怎么设置的？

我：订单号+业务字段

官：什么业务字段

我：忘记了

官：对于非分片键的查询你们是怎么做的？

我：目前查询条件比较固定，之前有想过冗余一份原始数据

官：分库分表后有遇到数据倾斜的问题吗？

我：不清楚，我们半年以上的数据都存到es冷冻库里。



答：

+ Sharding Key：业务唯一性条件映射到某表、某库
+ 非 Sharding 查询：ShardingSphere 框架中，它笛卡尔积查询，可以通过 Hint 优化
    - [https://shardingsphere.apache.org/document/5.1.0/en/user-manual/shardingsphere-jdbc/special-api/sharding/hint/#sharding-with-hint](https://shardingsphere.apache.org/document/5.1.0/en/user-manual/shardingsphere-jdbc/special-api/sharding/hint/#sharding-with-hint)
+ 数据倾斜的问题
    - MySQL 同步到 ES 中，离线查询



### 问题八：讲讲sdk和api的区别，应用场景
答：

SDK：Software Development Kit，软件开发套件，包含比较丰富 API 集成，简化开发，比如 gRPC、AWS

API：应用程序接口，具体到某个业务或者功能接口，作为数据交互入口





### 问题九：马哥我又来了，之前问过你一个网关里怎么去支持多注册中心的问题，你提供了一种优雅的处理方式，后来忙没去实践，最近看了你的代码（io.microsphere.netflix.eureka.client.spring.cloud.autoconfigure.MultipleEurekaClientAutoConfiguration），实践了一下，发现有问题，帮忙看下，文档整理得有点长，大致问题看前两页即可，感谢。
答：



在 Gray 灰度环境下，Trade-Service-A 服务而言，需要配置如下：

```yaml
eureka:
  instance:
    prefer-ip-address: true
    instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
    metadata-map:
      env: gray
```



如果是 Netfix Ribbon 实现一个 ServerFilter

如果是 Spring Cloud LoadBalancer 的话，需要实现一个带有灰度标签过滤的 <font style="color:#080808;background-color:#ffffff;">ServiceInstanceListSupplier 实现</font>

