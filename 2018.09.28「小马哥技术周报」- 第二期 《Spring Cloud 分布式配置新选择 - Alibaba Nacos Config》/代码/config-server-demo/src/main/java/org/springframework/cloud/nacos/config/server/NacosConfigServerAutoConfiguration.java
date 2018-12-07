package org.springframework.cloud.nacos.config.server;

import com.alibaba.nacos.config.server.controller.ConfigController;
import com.alibaba.nacos.config.server.controller.ConfigServletInner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.config.ConfigServerConfiguration;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.nacos.config.server.environment.NacosEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Nacos Config Server Auto-Configuration
 */
@ConditionalOnClass(EnableConfigServer.class)
@ComponentScan(basePackages = {
//        "com.alibaba.nacos.config.server.monitor",
        "com.alibaba.nacos.config.server.service"
})
public class NacosConfigServerAutoConfiguration {

    @Bean
    public NacosEnvironmentRepository nacosEnvironmentRepository() {
        return new NacosEnvironmentRepository();
    }

    @Bean
    public ConfigController configController() {
        return new ConfigController();
    }

    @Bean
    public ConfigServletInner configServletInner() {
        return new ConfigServletInner();
    }

}
