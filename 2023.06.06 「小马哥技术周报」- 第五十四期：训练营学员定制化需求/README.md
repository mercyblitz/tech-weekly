<a name="dsfDn"></a>
# 议程安排
<a name="m3GJ5"></a>
## 面试题解
<a name="Ru2Yz"></a>
### 实现所谓分代缓存，参考 Tomcat EL ConcurrentCache
<a name="gzVfi"></a>
#### EL - JSP 扩展，表达式语言
Spring EL
<a name="d3WIt"></a>
##### 内核设计
上下文对象<br />JSP 来自于 JSP 规范，JSP 9个内建对象，这个上下文在 Spring IoC 扩展也存在<br />Application - ServletContext(Servlet)<br />Session - HttpSession(Servlet)<br />Request - HttpServletRequest(Servlet) ，可以覆盖多个页面，比如在 JSP 存在 include，forward<br />Page - PageContext(JSP)，Spring MVC 利用 Model 对象替代
<a name="pHkeg"></a>
##### 使用场景
<a name="g9Xlk"></a>
###### JSP 场景
${user.name} = User#getName() -> Java Beans 内省<br />如果 user 对象存在于页面，等效写法是：${pageContext.user.name} = ${user.name}<br />user 对应的类是 User，需要被 Java Beans 内省
<a name="QnZ86"></a>
###### JSF 场景
#{user.walk}


UserInfo 使用是尽可能避免（屏蔽）掉 Object 类，因为里面存在 #getClass() 

Spring 安全漏洞：JDK 9+（模块化） + Spring 对应版本 + Servlet 容器部署 JSP 应用

<a name="s4idH"></a>
### Feed 流的设计（深度分页）
阿里巴巴 来往，马老师（马云）M12（他的老婆 M13）对抗腾讯小马哥（马化腾）<br />扎堆（类似于微信朋友圈），数据库分页，逐渐变慢<br />缓存动态列表，限制用户只能看到前 1000 个最新动态<br />Redis ZSet，Tair 不支持<br />LDB，插入时排序，类似于 TreeMap<br />查询已排序


阿里的 P10，游戏排行榜<br />我的好友排名，好友（1w），微信：5000 - 10000<br />全网排名: 200 w 游戏用户<br />实时，主管 C++<br />200 ms - 500ms<br />优化：25 ms - 75ms<br />< 10 ms

分布式 Fork-Join，Map Reduce

200 台，一次任务，Master -> Slave，3-10 分钟左右


<a name="mgPha"></a>
## 线上问题排查
<a name="U32KW"></a>
### Spring Boot Loader 并发加载 Jar 资源时遇到的线程 Blocked 问题
JStack Dump 线程运行状态，JVM 线程快照（瞬间）<br />![915389a2d25a09e9f310d5ad60a2883.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1686060069643-f00240bf-2b48-475c-81a9-b22a9d2aee49.png#averageHue=%230b0806&clientId=uad82addc-2f96-4&from=paste&height=534&id=ucbe3eef4&originHeight=668&originWidth=1674&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=86245&status=done&style=none&taskId=udcfee418-b66c-4e9c-a3e0-b9102e71dc2&title=&width=1339.2)<br />Spring Boot FAT-JAR 部署之后，它会存在与这样形式：<br />BOOT-INF/lib/abc.jar -> JarFile 对象

LocalVariableTableParameterNameDiscoverer 会去读取 Java 类所在的 Class 字节码资源来解析方法参数上的名称，如 Class User
```java
public class User {


    public void setName(String name){
        
    }

```
setName 方法上有一个参数名为 name，因此，它能被 LocalVariableTableParameterNameDiscoverer 读取到，并且作为数组返回：
```java
	@Nullable
	private String[] doGetParameterNames(Executable executable) {
		Class<?> declaringClass = executable.getDeclaringClass();
		Map<Executable, String[]> map = this.parameterNamesCache.computeIfAbsent(declaringClass, this::inspectClass);
		return (map != NO_DEBUG_INFO_MAP ? map.get(executable) : null);
	}
```
依赖于该 Class 被 ClassLoader 读取，ClassLoader 又是 Spring Boot 定制的，所以在并发场景下，仅有一个线程读取到 JarFile，其他线程不得不 Blocked。

Spring Boot Loader 覆盖了 "jar" protocol URL 流的获取逻辑，未使用 JDK 标准实现。是因为，Spring Boot 需要支持新的 "jar" protocol，如：jar://home/admin/user-service.jar/!BOOT-INF/lib/abc.jar<br />![f20adada8b25f807aca23a80b3a6d43.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1686060076543-4b4c3510-dd2a-4e79-b9e8-f1ba28f1d0f1.png#averageHue=%230a0806&clientId=uad82addc-2f96-4&from=paste&height=534&id=ua4cfbb5a&originHeight=667&originWidth=1605&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=80005&status=done&style=none&taskId=u75b209c0-44dd-403e-9668-5596fee690b&title=&width=1284)

建议：**不要直接使用 LocalVariableTableParameterNameDiscoverer，推荐使用 DefaultParameterNameDiscoverer**

LocalVariableTableParameterNameDiscoverer 利用 Class（已经加载的类对象）读取 Resource，Class#getReousceAsStream 通过流的方式打开，Class -> ClassLoader -> ClassLoader#getResourceAsStream，通常而言，Java 应用的 ClassLoader 是 URLClassLoader，去调用 LaunchedURLClassLoader#findResource 方法<br />DefaultParameterNameDiscoverer 利用 Class 获取 Method 反射对象，进一步获取方法参数名称：
```java
	public DefaultParameterNameDiscoverer() {
		// TODO Remove this conditional inclusion when upgrading to Kotlin 1.5, see https://youtrack.jetbrains.com/issue/KT-44594
		if (KotlinDetector.isKotlinReflectPresent() && !NativeDetector.inNativeImage()) {
			addDiscoverer(new KotlinReflectionParameterNameDiscoverer());
		}
		addDiscoverer(new StandardReflectionParameterNameDiscoverer());
		addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
	}
```
该类会将 StandardReflectionParameterNameDiscoverer 作为第一优先发现实现，而它是基于 Java 反射实现，又因为 Class 已被加载，故大多数场景，LocalVariableTableParameterNameDiscoverer 不会被执行到。<br />当 StandardReflectionParameterNameDiscoverer 去获取接口方法参数时会获取不到，**前提是 JDK 小于 8，或者 Java 8 以上未增加 -parameters 参数。**<br />在 Spring Boot FAT-JAR 场景下，Class 的加载也会使用到 Spring Boot org.springframework.boot.loader.jar.JarURLConnection 来解析 Class 资源所对应的流，因此在该问题场景下，Class 已经被加载（验证、解析）过了，当 Class 对象能被获取到时，该 Class 对象已被加载。
> Spring Boot 覆盖了传统的JDK 对 "jar" 协议的实现，采用 org.springframework.boot.loader.jar.Handler 来代替传统 JDK sun.net.www.protocol.jar.Handler，因此，对应的 URLConnection 实现也发生变化，即 Spring Boot 使用了自身提供的 org.springframework.boot.loader.jar.JarURLConnection


URLClassLoader
```java
 protected Class<?> findClass(final String name)
        throws ClassNotFoundException
    {
    	...
                        String path = name.replace('.', '/').concat(".class");
                        Resource res = ucp.getResource(path, false);
                        if (res != null) {
                            try {
                                return defineClass(name, res);
    	...
        return result;
    }
```
<a name="fdDAa"></a>
## 下一次讨论：浅谈 GraalVM 
<a name="rbt41"></a>
## 连麦讨论

