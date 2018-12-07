package demo.dubbo.spring.boot.provider;


import com.alibaba.dubbo.config.annotation.Service;
import demo.dubbo.spring.boot.api.EchoService ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * Dubbo  + Spring
 * Nacos  + Spring
 * Dubbo - Nacos
 * Spring Framework
 * Spring Application -> 分布式配置、服务注册和发现
 * Dubbo  + Nacos + Sentinel -> 服务注册和发现、分布式配置、服务熔断
 * Dubbo Spring Boot = OK
 * Dubbo Spring Cloud = Not Yet
 *
 *  @LoadBalanced // Spring Cloud 负载均衡的注解
 *     @Autowired
 *     private RestTemplate restTemplate; // 客户端
 *     // 服务端 -> 不动
 *
 *     // Spring Cloud Feign -> Dubbo
 *     @Autowired // 客户端
 *     private EchoService echoService;
 *
 *     // Spring Cloud Feign
 *     @RequestMapping("/echo")
 *     public String echo(String message){
 *         return echoService.echo(message);
 *     }
 */

@EnableAutoConfiguration // 激活自动装配
@PropertySource("classpath:/dubbo.properties")
public class DubboProviderBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboProviderBootstrap.class)
                .properties("dubbo.scan.basePackages=demo.dubbo.spring.boot.provider")
                .properties("management.endpoints.web.exposure.include=*")
        .run(args);
    }
}

@Service(
        version = "${echo.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
class DefaultEchoService implements EchoService {

    @Override
    public String echo(String message) {
        String response = "Echo : "+message;
        System.out.println(response);
        return response;
    }
}


