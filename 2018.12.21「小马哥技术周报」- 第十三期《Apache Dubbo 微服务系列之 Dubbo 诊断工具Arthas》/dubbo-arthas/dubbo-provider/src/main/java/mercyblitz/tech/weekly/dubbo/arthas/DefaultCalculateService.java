package mercyblitz.tech.weekly.dubbo.arthas;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import mercyblitz.tech.weekly.dubbo.arthas.api.CalculateService;

@Service(version = "1.0.0")
public class DefaultCalculateService implements CalculateService {

    @Override
    public int divide(int a, int b) {
        int result = a / b;
        System.out.printf(
                "[port : %s] CalculateService.divide( a= %d, b = %d ) = %d\n",
                RpcContext.getContext().getLocalPort(), a, b, result
        );
        return result;
    }
}
