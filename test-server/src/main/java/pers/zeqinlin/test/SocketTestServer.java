package pers.zeqinlin.test;


import pers.zeqinlin.rpc.api.HelloObject;
import pers.zeqinlin.rpc.api.HelloService;
import pers.zeqinlin.rpc.serializer.KryoSerializer;
import pers.zeqinlin.rpc.transport.RpcClientProxy;
import pers.zeqinlin.rpc.transport.socket.client.SocketClient;



/**
 * Socket方法~测试用的服务端
 */
public class SocketTestServer {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
