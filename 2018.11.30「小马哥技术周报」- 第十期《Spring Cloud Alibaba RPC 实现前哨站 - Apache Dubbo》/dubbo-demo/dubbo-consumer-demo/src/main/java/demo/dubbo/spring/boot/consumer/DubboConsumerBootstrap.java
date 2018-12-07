package demo.dubbo.spring.boot.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import demo.dubbo.spring.boot.api.EchoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class DubboConsumerBootstrap {

    @Reference(
            version = "${echo.service.version}",
            application = "${dubbo.application.id}",
            url = "dubbo://localhost:12345"
    )
    private EchoService echoService;

    @GetMapping
    public String echo(@RequestParam String message){
        return echoService.echo(message);
    }

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerBootstrap.class,args);
    }
}
