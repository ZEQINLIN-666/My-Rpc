package pers.zeqinlin.test;


import pers.zeqinlin.rpc.serializer.ProtostuffSerializer;
import pers.zeqinlin.rpc.transport.RpcClient;
import pers.zeqinlin.rpc.transport.RpcClientProxy;
import pers.zeqinlin.rpc.api.HelloObject;
import pers.zeqinlin.rpc.api.HelloService;
import pers.zeqinlin.rpc.transport.netty.client.NettyClient;

/**
 * Netty实现的测试用消费者
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtostuffSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }

}
