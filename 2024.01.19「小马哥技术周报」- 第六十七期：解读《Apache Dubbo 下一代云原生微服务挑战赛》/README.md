# 官方信息
> 原文：[Apache Dubbo 下一代云原生微服务挑战赛启动报名！五大赛题50万奖金池等你来战](https://cn.dubbo.apache.org/zh-cn/blog/2024/01/18/apache-dubbo-%e4%b8%8b%e4%b8%80%e4%bb%a3%e4%ba%91%e5%8e%9f%e7%94%9f%e5%be%ae%e6%9c%8d%e5%8a%a1%e6%8c%91%e6%88%98%e8%b5%9b%e5%90%af%e5%8a%a8%e6%8a%a5%e5%90%8d%e4%ba%94%e5%a4%a7%e8%b5%9b%e9%a2%9850%e4%b8%87%e5%a5%96%e9%87%91%e6%b1%a0%e7%ad%89%e4%bd%a0%e6%9d%a5%e6%88%98/)

## 赛题解读
我们期待参赛团队在高性能 Triple(HTTP/3) 协议设计、完善的 Benchmark 验收体系、零信任解决方案、Service Mesh架构 等方向持续探索，共同定义下一代云原生微服务体系，为开源社区和企业用户在性能、安全等方面带来收益。
本赛事一共包含五道赛题：

- 基于 HTTP/3 的高性能传输协议(Java)
- 自动化的 Dubbo 框架与协议性能基准 Benchmark 机制与平台(语言不限)
- 设计并实现一套零信任安全机制（含 Java/Golang SDK 适配与证书管理）
- 面向云原生的下一代微服务集群监测机制，涵盖Kubernetes、Nacos 等(Golang)
- 一种跨集群Kubernetes、传统VM微服务集群的互通方案与实现(Golang)
### 1 基于HTTP/3 的高性能传输协议(Java)
#### 1.1 赛题背景与目标
本赛题的主要目标是将 Dubbo 中的 triple 协议适配到 HTTP/3 之上，兑现 triple 协议规范中的所有核心能力。
关于 Dubbo3 triple 协议使用的相关示例请参考（请注意阅读 README 中链接指向的其他相关示例）：[https://github.com/apache/dubbo-samples/tree/master/1-basic](https://github.com/apache/dubbo-samples/tree/master/1-basic)，通过运行示例，可以了解当前 triple 协议的功能。
以下是 Triple 协议规范：[https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/triple-spec/](https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/triple-spec/)
#### 1.2 赛题说明
请基于 Apache Dubbo Java 实现的 3.3 分支进行开发，代码仓库链接如下： [https://github.com/apache/dubbo/tree/3.3](https://github.com/apache/dubbo/tree/3.3)
triple 协议相关实现源码：

- [https://github.com/apache/dubbo/tree/3.2/dubbo-rpc/dubbo-rpc-triple](https://github.com/apache/dubbo/tree/3.2/dubbo-rpc/dubbo-rpc-triple)
- [https://github.com/apache/dubbo/tree/3.3/dubbo-remoting/dubbo-remoting-http12](https://github.com/apache/dubbo/tree/3.3/dubbo-remoting/dubbo-remoting-http12)

开发者可根据自己的理解修改 Dubbo源码实现，比如在 remoting 层做 http3 适配，赛题实现不受限制，可以依赖如 Netty 等任意网络库或框架。
#### 1.3 参考链接

- [https://github.com/grpc/proposal/blob/master/G2-http3-protocol.md](https://github.com/grpc/proposal/blob/master/G2-http3-protocol.md)
- [https://devblogs.microsoft.com/dotnet/http-3-support-in-dotnet-6/](https://devblogs.microsoft.com/dotnet/http-3-support-in-dotnet-6/)
- [https://github.com/netty/netty-incubator-codec-http3](https://github.com/netty/netty-incubator-codec-http3)
- [https://webtide.com/jetty-http-3-support/](https://webtide.com/jetty-http-3-support/)
### 2 自动化的 Dubbo 框架与协议性能基准 Benchmark 机制与平台(语言不限)
#### 2.1 赛题背景与目标
作为一款 RPC 框架，Apache Dubbo 内置了两款高性能通信协议：

- triple 协议，基于 TCP 的高性能通信协议。
- dubbo 协议，基于 HTTP 的高性能通信协议，支持 Streaming 通信模型。

本赛题计划建设的 benchmark 平台理论需具备持任意协议测试的能力，实际产出可以 dubbo、triple 协议为主。
#### 2.2 赛题说明

- 建设一套支持 dubbo benchmark 验收的机制与流程，如当前有支持 java 语言的 [https://github.com/apache/dubbo-](https://github.com/apache/dubbo-samples)benchmark 可供参考
- 给出 dubbo、triple 协议的性能指标，以直观可视化的方式展示
- 找出性能瓶颈点、给出优化建议或实现
#### 2.3 参考资料

- [https://github.com/benchmark-action/github-action-benchmark](https://github.com/benchmark-action/github-action-benchmark)
- [https://grpc.io/docs/guides/benchmarking/](https://grpc.io/docs/guides/benchmarking/)
- triple协议规范 [https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/triple-spec/](https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/triple-spec/)
- dubbo协议规范 [https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/tcp/](https://cn.dubbo.apache.org/zh-cn/overview/reference/protocols/tcp/)
- 了解 dubbo 的基本使用方式 [https://github.com/apache/dubbo-samples](https://github.com/apache/dubbo-samples)
### 3 设计并实现一套零信任安全机制（含Java/Golang SDK 适配与证书管理）
#### 3.1 赛题背景与目标
本赛题的直接目标是为 Dubbo RPC 数据通信提供 TLS/mTLS 支持，但仅有相应的 API 与底层 tcp 链路是不够的，为了打造一套微服务整体解决方案，我们期望建立如下的一套零信任安全体系：
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1705654037605-77c8d23f-c52f-41ac-a8b8-b27cfaf10cc3.png#averageHue=%23fcfbf9&clientId=u6573977a-5dbc-4&from=paste&id=uf9b1fcd0&originHeight=1754&originWidth=2408&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=ua428082f-dd5e-4f5f-8a50-07e681b94ea&title=)
赛题涉及到控制面到数据面（Dubbo SDK）的数据推送，如证书更新、认证规则、鉴权规则等，我们计划在这个推送链路使用 xDS 协议。
针对架构设计中的几个核心组件，其主要职责是：

1. 控制面组件
   1. 作为 CA 机构负责证书签发与轮转，负责认证/鉴权规则的转换，提供 xDS Server 将证书与规则下发到数据面
   2. 接收认证/鉴权规则，一套定义良好的认证与鉴权规则，支持控制 TLS/mTLS 行为，如什么场景下启用mTLS等；支持配置符合匹配条件下的认证/鉴权，可通过下文参考链接了解更多内容
   3. xDS 协议，提供 xDS Server 负责证书与规则的推送。
2. 数据面组件（Dubbo SDK）
   1. Dubbo 框架适配 xDS 通道，接收证书与规则
#### 3.2 赛题说明
本赛题是取自 Dubbo 体系的微服务零信任总体方案，以上架构图描述的内容为该总体方案核心目标。
本赛题选手可以参与开发的内容分为两部分：

1. 数据面（Dubbo SDK），此部分为必做内容，即为 Dubbo SDK（Java/Go） 提供 xDS 的安全机制适配支持。如果选手决定不做控制面部分开发，则可以使用任意控制面 xDS Server 如 Istio 等辅助开发，流程与规则亦可直接使用 Istio 等，最终跑通 SDK 整体流程即可。
2. 控制面，此部分为选做内容，即通过定制 Dubbo 社区提供的控制面实现，实现一整套定制化零信任解决方案。
##### 3.2.1 数据面（Dubbo SDK）开发指南：

- 社区 Dubbo SDK 适配 xDS 的基本代码已经由官方提供，选手可直接在此之上关注安全相关 xDS 内容的进一步解析，并将接收到的证书、规则等适配到 Dubbo 框架内不
- 选手可以考虑 Dubbo 框架认证鉴权能力的可扩展性。比如为 Dubbo 提供一套通用的认证、鉴权 API，本赛题来自 xDS 控制面的安全源成为其中的一个扩展实现；未来可以扩展其他的如非 xDS 通道的证书来源等。

相关源码仓库：

- Dubbo Java 源码：[https://github.com/apache/dubbo-spi-extensions/tree/master/dubbo-xds](https://github.com/apache/dubbo-spi-extensions/tree/master/dubbo-xds)
- Dubbo Go 源码：[https://github.com/apache/dubbo-go/tree/main/xds](https://github.com/apache/dubbo-go/tree/main/xds)
##### 3.2.2 控制面开发指南：
控制面的 xDS Server 将由官方社区提供基础实现，选手们可关注安全相关部分的设计与实现。
控制面源码仓库：[https://github.com/apache/dubbo-kubernetes](https://github.com/apache/dubbo-kubernetes)
#### 3.3 参考资料
总的来说，本赛题要建设的是一套通用的零信任体系，提供以下业界实现作为参考：

- [https://istio.io/latest/docs/concepts/security/](https://istio.io/latest/docs/concepts/security/)
- [https://github.com/grpc/proposal/blob/master/A29-xds-tls-security.md](https://github.com/grpc/proposal/blob/master/A29-xds-tls-security.md)
- [https://kuma.io/docs/2.5.x/policies/mutual-tls/](https://kuma.io/docs/2.5.x/policies/mutual-tls/)
- [https://www.envoyproxy.io/docs/envoy/latest/api-docs/xds_protocol#xds-protocol-delta](https://www.envoyproxy.io/docs/envoy/latest/api-docs/xds_protocol#xds-protocol-delta)
### 4 面向云原生的下一代微服务集群监测机制，涵盖Kubernetes、Nacos (Golang)
#### 4.1 赛题背景与目标
微服务的整体可视化管控一直依赖都是一个非常重要的议题，本赛题的大背景是 Apache Dubbo 社区正在推进的 Dubbo 整体可视化项目，我们计划实现一个可以同时兼容 Zookeeper/Nacos 等传统注册中心、Kubernetes Service、Service Mesh 架构的统一的可视化控制台：

- 微服务集群数据可视化。包括集群内的应用、服务、示例等基本信息展示。
- 监控相关。借助 Dubbo SDK 上报到 Prometheus 的 Metrics 数据，控制台通过查询 Prometheus 展示相关监控指标，绘制调用链路图与依赖关系图等。
- 流量管控。支持流量管控规则，支持规则可视化编辑、预览、下发。
##### 4.1.1 总体目标
相比之前老版本的 Dubbo Admin 实现，我们期望新版本的控制台在部署架构、功能丰富度上都实现全面升级。
因此参赛选手可重点关注以下几个方向：

- 提供一个通用的微服务适配层抽象（如包含 Application、Servcice、Instance 等概念），作为新版控制台的统一数据源，屏蔽底层 Nacos、Kubernetes、Service Mesh 架构的差异。目前官方仓库提供了一个基本参考设计与实现。
- 设计控制台功能并提供完整实现。目前官方提供了参考交互效果图。

从用户 GUI 视角看，如可做菜单设计如下：

- 首页大盘
- 资源详情
   - 应用
   - 服务
   - 实例
- 流量管控
#### 4.2 赛题说明
本赛题官方已经提供了后端适配层的参考实现，同时给出了部分前端交互设计初稿。选手完全可以此为基础进行开发设计，具体评分标准参见官方报名渠道说明。

1. 后端： [https://github.com/apache/dubbo-kubernetes/tree/master/pkg/admin](https://github.com/apache/dubbo-kubernetes/tree/master/pkg/admin)
2. 前端交互图：[http://101.34.253.152/2024-1-14-v0.0.7/](http://101.34.253.152/2024-1-14-v0.0.7/)
3. 前端代码框架：https://github.com/apache/dubbo-kubernetes/tree/master/ui-vue3
#### 4.3 参考资料

- [https://github.com/kiali/kiali](https://github.com/kiali/kiali)
- [https://github.com/codecentric/spring-boot-admin](https://github.com/codecentric/spring-boot-admin)
- [https://github.com/apache/dubbo-admin](https://github.com/apache/dubbo-admin)
- [https://github.com/istio/istio](https://github.com/istio/istio)
### 5 一种跨集群Kubernetes、传统VM微服务集群的互通方案与实现(Golang)
#### 5.1 赛题背景与目标
在 Apache Dubbo 最新规划中，我们计划完整的支持以下部署架构：
##### 5.1.1 传统注册中心模式
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1705654037853-be3aac57-68d6-4ef6-b04a-4394599493c7.png#averageHue=%23fcfcfc&clientId=u6573977a-5dbc-4&from=paste&id=ua713c363&originHeight=686&originWidth=752&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=uf6f8c1b4-4bbe-48fd-86ed-3c1e4902d18&title=)
##### 5.1.2 基于 Kubernetes 调度的注册中心模式
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1705654037698-4527f9e5-84c6-4889-a388-086bb1b0007d.png#averageHue=%23fcfcfc&clientId=u6573977a-5dbc-4&from=paste&id=ufb2f38fe&originHeight=670&originWidth=820&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=uc6c04239-2c3e-46eb-98c8-9afe4a02dee&title=)
##### 5.1.3 基于 Kubernetes Service 的 Proxyless Service Mesh 模式
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1705654037674-6081055e-67ee-4cd0-b56f-1b1868ad3f24.png#averageHue=%23fbfaf8&clientId=u6573977a-5dbc-4&from=paste&id=u719cd718&originHeight=618&originWidth=740&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=u01e4e1db-f38c-4bfe-8b4f-6548b23de29&title=)
其中，控制面由 Dubbo 社区建设，仓库地址位于：[https://github.com/apache/dubbo-kubernetes](https://github.com/apache/dubbo-kubernetes)
#### 5.2 赛题解读
由于网络模型、注册中心等多个层面的原因，以上三种部署架构在很多情况下是相互隔离的，要实现这几个架构间的通信变的非常困难。
为了实现多集群地址、路由规则等互通，我们提供了一个跨集群部署的示例架构图，如下所示
![](https://cdn.nlark.com/yuque/0/2024/png/222258/1705654037706-d08ba5c9-9268-4e56-8ec7-53ea6929cdf3.png#averageHue=%23fcfcfc&clientId=u6573977a-5dbc-4&from=paste&id=ue24b61ed&originHeight=817&originWidth=1500&originalType=url&ratio=2&rotation=0&showTitle=false&status=done&style=none&taskId=u099eac24-b8e8-49ff-987f-372cc1d5304&title=)
其中，红色部分即为跨集群的数据流向，其中红色实现部分为控制面数据流，红色虚线部分为数据面数据流。
##### 5.2.1 为什么要跨集群通信那？
有以下场景可能会用到：

1. Kubernetes 迁移，之前用的是 Zookeeper 传统注册中心架构，期望迁移到 Kubernetes 集群部署，或者期望使用 kubernetes Service 服务模型
2. Kubernetes 多集群部署，跨多个 Kubernetes 集群 Dubbo 微服务通信
3. 混合部署，传统注册中心集群、Kubernetes 集群、Kubernetes Service（Service Mesh）架构混合部署
##### 5.2.2 期待交付用户的使用体验

1. Kubernetes Service：dubboctl install --mode=kubernetes --ingress-enabled
2. Kubernetes：dubboctl instaqll --mode=universal --ingress-enabled
3. 传统 VM：
   - `dubbo-cp run –registry-address=nacos://xxx``
   - dubbo-dp run --control-plane=xxx
4. 跨集群则需要额外部署 global 集群，使用方式与以上类似
##### 5.2.1 主要交付内容
以上图示例架构为例，参赛选手应主要关注：

1. 控制面、Ingress 的开发与建设（Dubbo SDK 侧无须关注，包括 SDK 与控制面的 xDS 通信，此部分可假设整体已经就绪），包括提供新方案中对于跨集群通信必要的组件支持等。
2. 跨集群的 global 控制面能力建设

Dubbo 官方目前提供了基础控制面实现，包括基于 xds server 的服务发现等内容，可在此基础上继续扩展跨集群方案实现。项目源码：[https://github.com/apache/dubbo-kubernetes](https://github.com/apache/dubbo-kubernetes)
#### 5.3 参考资料

1. [https://istio.io/](https://istio.io/)
2. [https://kuma.io/docs/2.5.x/production/deployment/multi-zone/](https://kuma.io/docs/2.5.x/production/deployment/multi-zone/)
## 报名方式 
### 报名链接
[开放原子开源基金会官方报名渠道](https://competition.atomgit.com/competitionInfo?id=be48a38bb1daf499bd5c98ac8a3108fd)
### 答疑群
有任何赛题疑问扫码加入以下钉钉群：

