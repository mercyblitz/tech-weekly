package demos.dubbo.nacos.bootstrap;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import demos.dubbo.nacos.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@EnableDubbo
//@PropertySource(value = "classpath:/config/consumer.properties")
@EnableNacosConfig
@NacosPropertySource(dataId = "nacos-consumer.properties")
public class ConsumerBootstrap {

    static {
        System.setProperty("nacos.server-addr","127.0.0.1:8848");
    }

    @Reference(version = "${demo.service.version}")
    private EchoService echoService;

    @Value("${dubbo.application.name}")
    private String appName1;

    @NacosValue(value = "${dubbo.application.name}",autoRefreshed = true)
    private String appName2;

    @NacosConfigListener(dataId = "nacos-consumer.properties")
    public void onChange(Properties properties){
        System.out.println("onChange(Properties) : "+properties);
    }

    @NacosConfigListener(dataId = "nacos-consumer.properties")
    public void onChange(String properties){
        System.out.println("onChange(String) : "+properties);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 10; i++) {
            System.out.println(echoService.echo("您好，小马哥"));
        }

//        new Thread(new Runnable() {
//            public void run() {
//                while(true){
//                    System.out.println("appName1 : "+appName1);
//                    System.out.println("appName2 : "+ appName2);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 @Configuration Class
        context.register(ConsumerBootstrap.class);
        // 启动当前上下文
        context.refresh();
        System.out.println("服务消费者已启动...");
        System.in.read();
    }
}
