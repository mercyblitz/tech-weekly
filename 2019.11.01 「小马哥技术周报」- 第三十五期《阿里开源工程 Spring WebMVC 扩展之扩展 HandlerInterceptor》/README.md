# 「小马哥技术周报」- 第三十五期《阿里开源工程 Spring WebMVC 扩展之扩展 HandlerInterceptor 》





## Spring Web 



> Struts 1.x 
>
> ActionMapping
>
> ActionForward



### 处理器（Handler）映射 - HandlerMapping

绝大多数实现类继承 AbstractHandlerMapping

包含多个有序 interceptors

- HandlerInterceptor（不带）
- MappedInterceptor（自带映射规则）

> MappedInterceptor 会被适配成 HandlerInterceptor



### 处理器（Handler）

- @RequestMapping 标注方法
- handleRequest 实现方法



### 处理器（Handler）执行链 - HandleExecutionChain

- 包含多个  HandlerMapping 以及一个 Handler 对象
- 委派集合执行
  - HandlerExecutionChain#applyPreHandle
  - HandlerExecutionChain#applyPostHandle
  - HandlerExecutionChain#triggerAfterCompletion



### 处理器（Handler）拦截器 - HandlerInterceptor



#### HandlerInterceptor 与 Filter 的区别？

Filter 是拦截 Servlet，一旦被拦截（中断），后续 Servlet 不会被执行

Filter 前置和后置处理需要开发人员自己去管理

Filter 拦截请求形式 - DispatcherType：

- Request
- Forward
- Include
- Error
- Async

Filter 映射关系和 Servlet 无关，单独部署

> Filter1 -> FIlter2 -> Filter3 -> Servlet
>
> FilterChain -> N  *  Filter + 1 Servlet



HandlerExecutionChain -> N * HandlerInterceptor  + 1 Handler

HandlerInterceptor  它的映射关系与 DispatcherServlet 有关系（子关系）



```xml
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/abc</url-pattern>
</servlet-mapping>

<filter>
    <filter-name>myFilter</servlet-name>
    <filter-class>com.acme.MyFilter</filter-class>
</filter>


<filter-mapping>
    <filter-name>dispatcher</filter-name>
    <url-pattern>/myfilter/</url-pattern>
</filter-mapping>
<!-- SCWCD ->
```



#### HandlerInterceptor 与 MappedInterceptor 的区别

MappedInterceptor  是存在自己的 URL 判断映射关系的，URL Pattern 属于特殊映射逻辑，默认 - AntPathMatcher，可以自定义 PathMatcher



#### HandlerInterceptor   与  WebRequestInterceptor 的区别

WebRequestInterceptor 是通用拦截器，被拦截的请求对象是 WebRequest

Web 三种规范

- Servlet
- Portlet
- JSF

>  Web Reactive : 



### 处理器（Handler）拦截器注册中心 - InterceptorRegistry

注册一到多个有序 HandlerInterceptor，它为 AbstractHandlerMapping 和 HandleExecutionChain 提供数据来源





## Spring Web MVC



### Handler 到底是什么？

- HandlerMethod
- Controller 实现类



Handler 来源于 `HandlerMapping#getHandler(HttpServletRequest)` 方法实现。



### 如何区分 REST 处理和视图渲染处理？





### 视图渲染



### REST 处理







### AnnotatedHandlerMethodHandlerInterceptorAdapter