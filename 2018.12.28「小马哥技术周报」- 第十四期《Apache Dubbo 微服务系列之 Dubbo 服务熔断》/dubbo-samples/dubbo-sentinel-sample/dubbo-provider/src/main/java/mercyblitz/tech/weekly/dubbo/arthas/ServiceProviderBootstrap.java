package mercyblitz.tech.weekly.dubbo.arthas;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableAutoConfiguration
public class ServiceProviderBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceProviderBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
