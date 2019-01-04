# 2019.01.04「小马哥技术周报」- 第十五期《Apache Dubbo 微服务系列之 Dubbo 与 Spring Cloud 整合》







## Spring Cloud Alibaba 整体回顾 



### Nacos 作为 Spring Cloud 注册中心



Consumer(8080) -> HTTP/REST -> Provider( Spring Web MVC : 0)

Consumer(8080)  -> REST Provider( Dubbo REST :7070)