package pers.zeqinlin.test;

import pers.zeqinlin.rpc.RpcClient;
import pers.zeqinlin.rpc.RpcClientProxy;
import pers.zeqinlin.rpc.api.HelloObject;
import pers.zeqinlin.rpc.api.HelloService;
import pers.zeqinlin.rpc.netty.client.NettyClient;
import pers.zeqinlin.rpc.socket.client.SocketClient;

/**
 * Netty实现的测试用消费者
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9000);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);

    }

}
