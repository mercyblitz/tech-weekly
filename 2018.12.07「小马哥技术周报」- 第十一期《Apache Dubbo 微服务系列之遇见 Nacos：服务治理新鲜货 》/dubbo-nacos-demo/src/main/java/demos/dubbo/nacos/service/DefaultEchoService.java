package demos.dubbo.nacos.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;

@Service(version = "${demo.service.version}")
public class DefaultEchoService implements EchoService {

    public String echo(String message) {
        RpcContext rpcContext = RpcContext.getContext();
        return String.format("Echo [port : %d] :  %s",
                rpcContext.getLocalPort(),
                message);
    }
}
