package demo.eurekaclientdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EurekaClientDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientDemoApplication.class, args);
	}
}
