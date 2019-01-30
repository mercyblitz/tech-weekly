# 2019.01.18「小马哥技术周报」- 第十七期《Apache Dubbo 微服务系列之 Dubbo Spring Cloud 实现（下）》



### Feign Dubbo 服务接口匹配分析

服务端应用名称：`spring-cloud-alibaba-dubbo`

Dubbo 服务

- 接口：`org.springframework.cloud.alibaba.dubbo.service.EchoService`

- 版本：1.0.0
- 协议：dubbo 以及 rest

```java
@Service(version = "1.0.0", protocol = {"dubbo", "rest"})
@RestController
@Path("/")
public class DefaultEchoService implements EchoService {
    @Override
    @GetMapping(value = "/echo")
    @Path("/echo")
    @GET
    public String echo(@RequestParam @QueryParam("message") String message) {
        return RpcContext.getContext().getUrl() + " [echo] : " + message;
    }
}
```





客户端应用应用：spring-cloud-alibaba-dubbo

Feign 接口：

```java
@FeignClient("spring-cloud-alibaba-dubbo")
public interface FeignEchoService {

    @GetMapping(value = "/echo")
    String echo(@RequestParam("message") String message);
}
```

指定的 Spring Cloud 应用名称：`spring-cloud-alibaba-dubbo`

REST URI：`/echo`

Params ： `message`





`spring-cloud-alibaba-dubbo` -> 

配置信息 ID： `spring-cloud-alibaba-dubbo-rest-metadata.json`

内容：

```json
[
  {
    "name": "providers:org.springframework.cloud.alibaba.dubbo.service.EchoService:1.0.0",
    "meta": [
      {
        "configKey": "EchoService#echo(String)",
        "method": "GET",
        "url": "/echo?message={message}",
        "headers": {},
        "indexToName": {
          "0": [
            "message"
          ]
        }
      },
      ...
    ]
  }
]
```



Feign.Builder 

- 默认实现：`feign.Feign.Builder`
- Hystrix: `feign.hystrix.HystrixFeign.Builder`
- Sentinel:`org.springframework.cloud.alibaba.sentinel.feign.SentinelFeign`



`feign.Feign.Builder` -> Wrapper -> build()





`FactoryBean<FeignEchoService>` getType() == `FeignEchoService`



