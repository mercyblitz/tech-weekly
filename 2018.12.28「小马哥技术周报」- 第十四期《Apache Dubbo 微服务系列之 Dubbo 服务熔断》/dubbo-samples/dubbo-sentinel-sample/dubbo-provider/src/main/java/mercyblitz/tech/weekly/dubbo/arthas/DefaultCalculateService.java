package mercyblitz.tech.weekly.dubbo.arthas;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import mercyblitz.tech.weekly.dubbo.arthas.api.CalculateService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Service(version = "1.0.0", protocol = {"dubbo", "rest"})
@Path("demo")
public class DefaultCalculateService implements CalculateService {

    @Override
    @GET
    @Path("/divide")
    public int divide(@QueryParam("a") int a, @QueryParam("b") int b) {
        int result = a / b;
        System.out.printf(
                "[port : %s] CalculateService.divide( a= %d, b = %d ) = %d\n",
                RpcContext.getContext().getLocalPort(), a, b, result
        );
        return result;
    }
}
