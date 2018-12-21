package mercyblitz.tech.weekly.dubbo.arthas;

import com.alibaba.dubbo.config.annotation.Reference;
import mercyblitz.tech.weekly.dubbo.arthas.api.CalculateService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class ServiceConsumerBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceConsumerBootstrap.class)
                .run(args);
    }

    @Reference(version = "1.0.0")
    private CalculateService calculateService;

    @GetMapping("/divide")
    public int divide(@RequestParam int a, @RequestParam int b) {
        return calculateService.divide(a, b);
    }
}
