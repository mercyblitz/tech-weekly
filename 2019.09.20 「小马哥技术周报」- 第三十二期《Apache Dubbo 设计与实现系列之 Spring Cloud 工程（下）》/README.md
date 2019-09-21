# 2019.09.20 「小马哥技术周报」- 第三十二期《Apache Dubbo 设计与实现系列之 Spring Cloud 工程（下）》





## 预备知识



元 - Meta

元数据 - Metadata



业务使用

SQL

id: 1

name: 张三

age : 9



框架使用

id : int AUTO_INCRMENT

name : VARCHAR(10) , desc = 姓名

age : int , desc = 年龄



SELECT id

SELECT ID



- 反射
- 配置 System#getProperties() `-Duser.timezone=`
- 字节码（JVM 解释执行）
- SQL Metadata





### Java 元数据



#### 配置类型

- 配置文件





#### SQL 类型

- JDBC 连接 - Connection -> DriverManager
- 数据集合  - ResultSet
  - 数据 - getXXX(int)
  - 元数据 - getMetadata() - ResultSetMetaData







## Dubbo 中的元数据



### Dubbo 服务注册/发现元信息 - org.apache.dubbo.common.URL



URL : dubbo://192.168.1.1:20880/com.xxx.XxxService&version=1.0.0&group=test&dubbo.version=2.0.0





#### 传统  Dubbo 在 Zookeeper 注册中心

- /com.xxx.XxxService/${version}/
  - dubbo://192.168.1.1:20880/com.xxx.XxxService&version=1.0.0&group=test&dubbo.version=2.0.0
  - dubbo://192.168.1.2:20880/com.xxx.XxxService&version=1.0.0&group=test&dubbo.version=2.0.0
  - dubbo://192.168.1.3:20880/com.xxx.XxxService&version=1.0.0&group=test&dubbo.version=2.0.0



如果 Dubbo 应用 A 存在 10 接口，那么一台服务实例在 Zookeeper 中将有 10 个节点

[1...10]

Dubbo 应用对注册中心的压力大与小，来自暴露服务的多与寡



#### Dubbo Spring Cloud 在 Zookeeper 注册中心

Dubbo 应用无论暴露多少 Java 服务，仅有一条数据（服务实例）- 云原生

```
dubbo://192.168.0.108:20880/org.springframework.cloud.alibaba.dubbo.service.DubboMetadataService?anyhost=true&application=spring-cloud-alibaba-dubbo-provider&bind.ip=192.168.0.108&bind.port=20880&default.deprecated=false&default.dynamic=false&default.register=true&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&group=spring-cloud-alibaba-dubbo-provider&interface=org.springframework.cloud.alibaba.dubbo.service.DubboMetadataService&methods=getAllServiceKeys,getServiceRestMetadata,getExportedURLs,getAllExportedURLs&pid=5728&register=true&release=2.7.1&revision=1.0.0&side=provider&timestamp=1568985689509&version=1.0.0
```



如果通过一个元数据服务收集当前应用暴露的所有服务，那么 Zookeeper 注册中心就只需要一条数据



元数据服务是内建服务，其他服务属于业务服务



元数据服务 1 -> N 个业务服务



1. 面向服务实例注册
2. Dubbo 服务不再直接注册到注册中，而是放置在服务实例的元信息
3. Dubbo 服务注册到元数据服务中（DubboMetadataService，替换过去的注册中心，去中心化，服务分担到各个服务提供者）
4. DubboMetadataService 是 Dubbo 服务消费者数据订阅来源







#### Dubbo Spring Cloud 常见问题

- 什么情况才会导致元数据服务更新

当某个服务在某个时间点动态地暴露时，这时会更新元数据服务。服务订阅方如何感知这个变化，Dubbo Spring Cloud 只有一种选择，重启当前服务消费者。

绝大多数情况，Dubbo 服务暴露放在应用注册到注册中心之前，换言之，在应用启动过程中会处理掉。

- 假设消费服务应用 C 订阅服务提供者 P 的某一个接口 I，并且 P 应用暴露五种服务，因此 DubboMetadataService 会返回 5 个 URL，是否应用 C 会保存其他四个无需服务元信息？

答案：不会的，经过服务订阅元信息过滤。

- 当服务提供方应用 P，需要服务升级（增加、减少），通常采用分批发布，新服务批次 B 1，老服务批次 B2（未发布），这时候，需要 Dubbo Spring Cloud 使用者确保 Java 服务接口的方法签名兼容



1. V1 EchoService 接口

```java
public interface EchoService {
    public String echo(String message); // V1
}
```



2. 升级到 V2 EchoService 接口

```java
public interface EchoService {
    
    public String echo(String message); // V1
    
    public int hashCode(); // V2 新增方法
}
```



假设服务提供应用 P，存在 10 台服务实例，V1 EchoService（老）部署 8 台，V2 EchoService 正在部署 2 台



10 -> 8 V1 + 2 V2



服务消费应用 C，订阅 P V1 (8) + P V2 (2)

服务消费应用 E，订阅 P V2

V2  删除了 V1 的方法的话，C 在调用时，有可能调用到错误 P V2 机器上（不存在 V1 服务方法）

这个限制在现发布的 Dubbo Spring Cloud 中一直存在，Dubbo Cloud Native 得到很好地解决







