package pers.zeqinlin.test;

import pers.zeqinlin.rpc.api.HelloService;
import pers.zeqinlin.rpc.serializer.ProtostuffSerializer;
import pers.zeqinlin.rpc.transport.netty.server.NettyServer;


/**
 * 测试用Netty服务端
 */
public class NettyTestServer  {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1",9966);
        nettyServer.setSerializer(new ProtostuffSerializer());
        nettyServer.publishService(helloService, HelloService.class);
    }
}
