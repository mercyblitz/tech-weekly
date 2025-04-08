<a name="Ezb6q"></a>
# 主要议题
<a name="b2v7O"></a>
## 内容来源
![568168f97ac8a924424bde761d47d86.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1694176305699-47905e31-73a1-4779-b12f-de78aa33b05d.png#averageHue=%23fefefe&clientId=u4d680587-0053-4&from=paste&id=u63c7f72e&originHeight=857&originWidth=779&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=207942&status=done&style=none&taskId=ua5b211e3-d433-4af7-80ba-81ec53e5178&title=)


<a name="UFSFs"></a>
## 异地多活功能
<a name="qbq1H"></a>
### 同区域优先
Spring Cloud 注册中心<br />Spring Cloud OpenFeign 调用<br />Apache Dubbo 2.7.x + Spring Cloud 注册中心<br />JDBC DataSource 同区域存储<br />Redis<br />Zookeeper<br />HTTP based Service : ES<br />~~MQ~~： Kafka<br />基础设施：<br />DB<br />磁盘
<a name="f8RJv"></a>
### 故障转移
> 别名：Service Mesh，Traffic Shifting

区域 A、B、C，分摊 300 万流量，各自 100 万，各区域机器配置 100 台
> 区域：Region，Zone

当 A 故障时，B 和 C 分摊 A 流量<br />当 A 故障时，B 如果没有得到安全阈值时（计划：100，实际：50），C 会承担更多流量<br />计划：B + C 数量（权重）：200<br />实际：B(50) + C(100) 数量（权重）：150

<a name="Qb3Av"></a>
## 现状分析
<a name="D4tfZ"></a>
### 注册中心
目前：Eureka<br />未来：Nacos

<a name="cXBEE"></a>
### 服务网关
目前：ESB <br />未来：可能 Spring Cloud Gateway 或 其他网关

<a name="dfUCB"></a>
### 应用客户端 
<a name="V2BAd"></a>
#### Spring Cloud
版本：[2021.0.5](https://docs.spring.io/spring-cloud/docs/2021.0.5/reference/html/)<br />未来：

- Spring Cloud Alibaba？不会/不确定
- Nacos + Spring
- 是否要升级成高版本

参考 [Spring Cloud Alibaba 版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

<a name="WgKEq"></a>
#### Spring Boot 版本
2.7.10

<a name="cWxIu"></a>
#### 负载均衡
早期：Netflix Ribbon<br />后期：Spring Cloud LoadBalancer


<a name="aPo0l"></a>
#### 服务调用
Spring Cloud OpenFeign<br />Dubbo 版本（下掉）


<a name="sw7QL"></a>
## 需求分析
两个集群并行，平时都是集群 A，当某个系统的某个服务出现异常时，能够自动把集群B的服务返回给消费者使用。 就是要保证消费者不关心服务来自哪里，能用就行，没问题时候肯定要同集群内的优先。

Q：集群 B 是否属于集群 A 的灾备集群？<br />A：是

Q：集群 A 与 集群 B 是否互为灾备集群？<br />A：是

Q：集群 A 与 集群 B 存在同集群优先的话？<br />A：双活集群

<a name="CTweu"></a>
### 已知问题

1. 目前各个系统都是自己内部玩 Spring Cloud 的，以后一起玩时候服务名肯定会重复，要统一命名，各个系统自己改还是二次开发注册中心Client帮他改。

Q：Spring Cloud Eureka Server 是否在不同业务组是不同的服务器？<br />A：独立业务组部署

Q：假设业务组 A 是否需要访问业务组 B 的服务？<br />A：ESB 桥接服务转发

Q：假设业务组 A 和业务组 B 出现服务名重复的话，比如 user-service 出现重复，那么 A 调用 user-service  时，会访问业务组 A 服务？<br />A：是

Q：假设业务组 A 需要调用业务组 B user-service 的话，是否需要额外的辅助 ESB 转发？<br />A：ESB 转发


Q：业务组 A 和业务组 B  各自的 user-service 服务 URI 是否相同？<br />A：可能存在

<a name="fhEA6"></a>
## 设计实现
<a name="xMF2f"></a>
### 需求一：解决集群服务调用冲突
业务组 A 和业务组 B 出现服务名重复的话，比如 user-service 出现重复，那么 A 调用 user-service 时，能够区分是业务组 A 还是业务组 B 服务？
<a name="jwVRm"></a>
#### 服务调用端设计实现
假设 Spring Cloud OpenFeign 在服务接口声明上可以指定 ：
```java
@FeignClient(name="user-service")
public interface UserService {
    
}

// 如何声明会存在服务调用混乱
```
改良如下：
```java
@FeignClient(name="user-service")
public interface UserService {
    
}

@FeignClient(name="user-service")
@TargetCluster(name = "B")
public interface UserServiceB {
    
}
```
这样业务修改的代价比较大！

区别客户端 HTTP 请求的话，基本的思路：

- 请求 URI（可能相同）
- 请求头（比如添加：Cluster 或者 Region 标识来给网关识别）

参考实现：

1. 启动时，读取应用所在的区域信息
2. 将所在的区域信息通过 Spring Cloud OpenFeign 在请求调用前增加相关请求头
3. 网关接收到该请求时，通过转发到目标服务器

假设业务组 A 服务 trada-service 调用 user-service，那么， trada-service 所在的区域是固定的，比如 A，一般而言，网关将请求头信息转发到业务组 A 服务 user-service。
```java
@FeignClient(name="user-service")
public interface UserService {
    
}

@FeignClient(name="user-service")
public interface UserServiceB { // UserServiceB 方法调用时，切换请求头将 A 调整为 B
    
}
```
可通过配置来映射关系，如下所示：
```properties
feign.clusters.UserServiceB = B
```

<a name="DSlRd"></a>
#### 服务提供端设计实现
假设业务组 A 服务 user-service API 定义如下：
```java
@RestController
@RequestMapping("/user/service")
public class UserServiceController {

    @GetMapping("/list")
    public List<User> getUsers(){
        ...
    }
}

```

业务组 B  服务 user-service API 定义如下：
```java
@RestController
@RequestMapping("/user/service")
public class UserServiceControllerB {

    @GetMapping("/list")
    public List<User> getUsers(){
        ...
    }
}
```

<a name="sihRj"></a>
##### 目标结果

- 当网关 G 获取到业务组 A 和 业务组 B 所有的服务节点时，如 user-service，当时它们的服务实例（节点）的元信息 Cluster 将会出现 A 和 B。
- 当服务调用端发送请求到服务 user-service 时，网关 G 能够获取其请求头中的 Cluster 信息，通过路由可以筛选出 Cluster = A 或 B 的服务实例列表。
<a name="VB35I"></a>
##### 前提条件

- 业务组 A 服务 user-service 在服务注册阶段，需要上传当前服务节点所在的区域信息，比如 Cluster = A
- 业务组 B 服务 user-service 在服务注册阶段，需要上传当前服务节点所在的区域信息，比如 Cluster = B
- 网关 G 同时订阅业务组 A 和 B 的注册中心，涉及 Spring Cloud 多注册
   - 假设注册中心集群之间可复制状态
      - 假设注册中心是 Eureka Server 的话，可以通过将 Eureka Server A 和 Eureka Server B 开启集群复制
   - 如果注册中心集群之间无法复制状态
      - 假设注册中心是 Eureka Server 的话，需要网关接入多服务发现客户端，如接入 io.github.microsphere-projects:microsphere-spring-cloud-netflix-eureka-client



<a name="SBjU2"></a>
### 需求二：异构注册中心集群服务互通
双服务注册，各个业务系统向自己内部的 Eureka 注册时，也向公共的 Nacos 注册，内部调用时候，走自己的，跨系统时候用 Naocs 的信息。<br />双服务订阅，主要集中在网关层面

<a name="lEe7M"></a>
#### 架构设计
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1694182919752-45dc282d-980d-4449-a48b-9b93aecc5e0b.png#averageHue=%23fdfcfc&clientId=u4d680587-0053-4&from=paste&id=u31413b46&originHeight=737&originWidth=1220&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=68930&status=done&style=none&taskId=ufa4ed61a-3975-4fa4-b9b7-97ec7f7eca5&title=)
<a name="b5IoI"></a>
#### 方案一：升级 Spring Cloud 服务注册与发现客户端
<a name="ZLq83"></a>
##### 默认行为

- 服务注册：单服务注册
- 服务发现：多服务发现 - CompositeDiscoveryClient
   - 按照当前的需求，CompositeDiscoveryClient 可组合两种 DiscoveryClient 实现，次序如下：
      - Spring Cloud Alibaba Nacos Client（新）
      - Spring Cloud Netflix Eureka Client（老）

<a name="IMgAe"></a>
##### 服务升级过程
<a name="ROACx"></a>
###### 双注册/双订阅升级
以集群 A 为例，服务提供端 user-service 首先引入两种服务发现客户端，且当前服务由 3 台服务器组成，再升级过程采用滚动发布，按照次序为 1 -> 2 -> 3，在第一批发布时：

- user-service-1 发布后，它在 Eureka Server A 和 Nacos Server 中同时存在，user-service-2 和 user-service-3 仅在 Eureka Server A 存在
- 当 user-service-2 发布后，它也在 Eureka Server A 和 Nacos Server 中同时存在，此时，仅有user-service-3 仅在 Eureka Server A 存在
- 当 user-service-3 发布后，3 台机器信息同时存在于 Eureka Server A 和 Nacos Server  中。

服务调用端 trade-service 也两种服务发现客户端，且服务器数量为 2，升级过程如下：

- 当 trade-service-1 发布后，它在 Eureka Server A 和 Nacos Server 中同时存在，trade-service-2 仅在 Eureka Server A 存在
- 当 trade-service-1 发布后，2 台机器信息同时存在于 Eureka Server A 和 Nacos Server  中。

此时，集群 A 中的 user-service  和 trade-service 均已升级为双注册/双订阅。

<a name="cOxJn"></a>
###### 移除 Eureka Client 升级
以集群 A 为例，服务提供端 user-service 移除 Spring Cloud Netflix Eureka Client ，且当前服务由 3 台服务器组成，再升级过程采用滚动发布，按照次序为 1 -> 2 -> 3，在第一批发布时：

- user-service-1 发布后，它仅在Nacos Server 存在，user-service-2 和 user-service-3 在 Eureka Server A 和 Nacos Server 服务同时存在。
- 当 user-service-2 发布后，user-service-1 和 user-service-2 仅在 Nacos Server 存在，仅 user-service-3 在 Eureka Server A 和 Nacos Server 服务同时存在。
- 当 user-service-3 发布后，3 台机器信息仅在 Nacos Server  中。

服务调用端 trade-service 移除 Spring Cloud Netflix Eureka Client，且服务器数量为 2，升级过程如下：

- trade-service-1 发布后，它仅在 Nacos Server 存在（），trade-service-2 在 Eureka Server A 和 Nacos Server 服务同时存在。
- 当 trade-service-2 发布后，2 台机器信息仅在 Nacos Server  中，user-service 必须在 Nacos Server 存在，需要足够地 user-service 机器数量，避免升级过程中出现服务不稳定。

升级原则：首先升级服务提供者，再升级服务调用者。一般而言，服务提供者注册到新注册中心，要与老注册中心的注册数量保持一致，或者略少于。

<a name="ETK0Q"></a>
#### 方案二：利用网关兼容老的服务发现客户端 API
假设使用 Spring Cloud Gateway 来实现的话，可以兼容 

- [Eureka REST Operations](https://github.com/Netflix/eureka/wiki/Eureka-REST-operations)
- [Nacos Open API](https://nacos.io/zh-cn/docs/v2/guide/user/open-api.html)

<a name="MQAHH"></a>
##### 架构设计
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1694184815457-7147b91a-32fc-4832-8b24-fe5d26941fc4.png#averageHue=%23fdfcfc&clientId=uc5d23a08-2faa-4&from=paste&height=659&id=ud531fa00&originHeight=824&originWidth=1214&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=79866&status=done&style=none&taskId=u70591285-c243-4bfc-9598-f24fb65186d&title=&width=971.2)<br />新老系统只需要调整 Spring Cloud Alibaba Nacos Client 和 Spring Cloud Netflix Eureka Client 的注册中心配置到 Gateway 即可。

Gateway 可以是独立为 注册中心 Gateway 和 跨区域 Gateway 调用，相当于 Gateway 当做注册中心  + ESB。
