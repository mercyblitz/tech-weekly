package mercyblitz.tech.weekly.dubbo.demo.api;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("${abc.name}")
public interface CalculateService {

    @GetMapping("/")
    int divide(int a, int b);

}

class  A {

//    @Reference
    @Autowired
    private CalculateService calculateService;
}

