package mercyblitz.tech.weekly.dubbo.arthas;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import mercyblitz.tech.weekly.dubbo.arthas.api.CalculateService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableAutoConfiguration
@RestController
public class ServiceConsumerBootstrap {

    static {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>("localhost:8848",
                "Sentinel:Demo",
                "com.alibaba.csp.sentinel.demo.flow.rule",
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceConsumerBootstrap.class)
                .run(args);
    }

    @Reference(version = "1.0.0",filter = "sentinel.dubbo.consumer.filter")
    private CalculateService calculateService;

    @GetMapping("/divide")
    public int divide(@RequestParam int a, @RequestParam int b) {
        return calculateService.divide(a, b);
    }
}
