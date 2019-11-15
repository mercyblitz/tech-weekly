# 2019.11.15 「小马哥技术周报」- 第三十六期《阿里开源工程 Spring WebMVC 扩展之内容协商》





### 客户端请求

 https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation 

```
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3
```



### 服务端响应

```
content-type: text/html; charset=utf-8
```



```
content-type: application/xml; charset=utf-8
```





## Spring Web MVC 视图处理



### 视图模板

`@Controller` 

HandlerMethod 方法返回的内容视图的地址

### REST 处理

`@RestController` = `@Controller` + `@ResponseBody`

HandlerMethod 方法返回的 Body 内容

`ResponseEntity` = Body + Header





HandlerMethod = public 方法标注 `@RequestMapping`





### 架构特色

Spring Web MVC 允许多套视图处理器（ViewResolver）并存



#### 模板渲染

##### 媒体类型(Accept 请求)

text/html -> Velocity

> text/*
>
> */*

text/json -> themleaf

text/xml -> JSP

##### 请求后缀(URL)

xxx.html -> Velocity

xxx.json -> themleaf

xxx.xml -> JSP





URL : http://acme.com/abc.json?format=application/xml

Accept : text/html





