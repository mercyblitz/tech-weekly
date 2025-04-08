<a name="dr73e"></a>
# Java 21 主要特性变化
[Java 21 核心特性](https://openjdk.org/projects/jdk/21/)<br />[Java 21 发布日志](https://jdk.java.net/21/release-notes)
<a name="izj8S"></a>
## Java 虚拟线程
<a name="iyjxp"></a>
### 文章解读

- [When Quarkus meets Virtual Threads](https://quarkus.io/blog/virtual-thread-1/)
- [VIRTUAL THREAD SUPPORT REFERENCE](https://quarkus.io/guides/virtual-threads)
- [Hello, Java 21](https://spring.io/blog/2023/09/20/hello-java-21)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1695438753243-a17f4677-1773-42ec-93c2-26fa7aee6db6.png#averageHue=%23fdfdfd&clientId=u658aa789-b939-4&from=paste&height=815&id=ue573f2f9&originHeight=896&originWidth=1105&originalType=binary&ratio=1.100000023841858&rotation=0&showTitle=false&size=68679&status=done&style=none&taskId=u15d18f44-08b8-4798-8627-4be307a677e&title=&width=1004.5454327725187)
<a name="qi7Ui"></a>
### Java 语法变化
<a name="gKjST"></a>
### Java 升级的影响
评估 JDK 移除的 API，类似于 Unsafe、XML API 等<br />评估 JDK 字节码兼容<br />评估 JVM GC 变化对 API 影响，比如 ZGC Bug 等影响线程栈空间<br />评估新 API 对老框架影响，比如虚拟线程和 Spring 事务关系，ThreadFactory 决定线程创建的方式，ThreadLocal 可能绑定的 Thread 对象是否为虚拟线程
<a name="a4CY6"></a>
# 「小马哥 Java 训练营第四期」课程大纲<br /><br />
