> <font style="color:rgb(24, 25, 28);">作业地址：https://github.com/mercyblitz/java-training-camp/issues</font>
>

<font style="color:rgb(24, 25, 28);"></font>

<font style="color:rgb(24, 25, 28);"></font>

# <font style="color:rgb(31, 35, 40);">第四期 作业四：基于 Spring 6.0+ HTTP Interface 实现通用同区域优先以及 Zone 多活架构 </font>[#36](https://github.com/mercyblitz/java-training-camp/issues/36)
## Spring 6.0+ HTTP Interface


## 作业点评
### [小伙伴作业一](https://github.com/mercyblitz/java-training-camp/issues/36#issuecomment-1910518583)
利用 Spring 6.0+ HTTP Interface 桥接到 RestTemplate，RestTemplate 它本身就具备负载均衡能力。

同区域优先以及多活架构也是兼容 Spring Cloud LoadBalancer。

Multiactive 架构

+ 支持 Spring Cloud LoadBalancer，也就是支持了
    - Spring Cloud OpenFeign
    - Spring Cloud @LoadBalanced RestTemplate
    - Spring Cloud Gateway
    - Spring 6.0+ HTTP Interface
+ 支持 Netflix OSS：
    - Eureka
    - Ribbon
+ 支持 Apache Dubbo

### [小伙伴作业二](https://github.com/mercyblitz/java-training-camp/issues/36#issuecomment-1911581830)
参考 Spring Cloud OpenFeign 实现

# <font style="color:rgb(31, 35, 40);">第四期 作业五：基于MySQL Replication Connection 实现通用同区域优先以及 Zone 多活架构 </font>[#38](https://github.com/mercyblitz/java-training-camp/issues/38)


## 作业点评
### [小伙伴作业一](https://github.com/mercyblitz/java-training-camp/issues/38#issuecomment-1980367318)
利用 MySQL 驱动 com.mysql.cj.jdbc.ha.BalanceStrategy 与 Mutliactive io.microsphere.multiple.active.zone.ZonePreferenceFilter



### [小伙伴作业二](https://github.com/mercyblitz/java-training-camp/issues/38#issuecomment-1985390219)
<font style="color:rgb(31, 35, 40);">可直接使用 ReplicationConnectionProxy 多主多从策略，将同区域MySQL实例配置为SOURCE，其他区域MySQL实例配置为REPLICA)，配置方式如下：  
</font><font style="color:rgb(31, 35, 40);">jdbc:mysql:replication://root:root123456@(host=localhost,port=3309,type=SOURCE),root:root123456@(host=localhost,port=3310,type=SOURCE),root:root123456@(host=localhost,port=3311,type=REPLICA),root:root123456@(host=localhost,port=3312,type=REPLICA)/my_schema?ha.loadBalanceStrategy=random</font><font style="color:rgb(31, 35, 40);">  
</font><font style="color:rgb(31, 35, 40);">参考代码：com.mysql.cj.conf.ConnectionUrlParser</font>

<font style="color:rgb(31, 35, 40);">优点：无入侵，完全使用MySQL驱动原生实现和配置；可在实现多区域多活、同区域优先的前提下，支持LoadBalance策略。  
</font><font style="color:rgb(31, 35, 40);">缺点：功能仅限于多区域多活、同区域优先，如果需要指定区域优先级等定制策略，还是需要自定义扩展。</font>

