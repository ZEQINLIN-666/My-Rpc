package pers.zeqinlin.test;


import pers.zeqinlin.rpc.api.HelloObject;
import pers.zeqinlin.rpc.api.HelloService;
import pers.zeqinlin.rpc.client.RpcClientProxy;

/**
 * 测试客户端
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 8000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject requestObject = new HelloObject(115, "hello Server~~");
        String res = helloService.hello(requestObject);
        System.out.println(res);
    }

}
