package pers.zeqinlin.test;


import pers.zeqinlin.rpc.transport.RpcClientProxy;
import pers.zeqinlin.rpc.api.HelloObject;
import pers.zeqinlin.rpc.api.HelloService;

import pers.zeqinlin.rpc.transport.socket.client.SocketClient;

/**
 * Socket方式的测试客户端
 */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 8000);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject requestObject = new HelloObject(115, "hello Server~~");
        String res = helloService.hello(requestObject);
        System.out.println(res);
    }

}
