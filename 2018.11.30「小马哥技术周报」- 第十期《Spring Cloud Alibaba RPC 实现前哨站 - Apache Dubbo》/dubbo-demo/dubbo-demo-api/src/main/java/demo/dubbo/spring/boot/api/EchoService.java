package demo.dubbo.spring.boot.api;


// EchoService -> /demo/dubbo/spring/boot/api/echo-service/echo

// Dubbo REST JAX-RS 2
// Dubbo Spring MVC

public interface EchoService {

    String echo(String message);
}
