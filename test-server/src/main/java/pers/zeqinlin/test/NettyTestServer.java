package pers.zeqinlin.test;

import pers.zeqinlin.rpc.transport.netty.server.NettyServer;
import pers.zeqinlin.rpc.registry.DefaultServiceRegistry;

/**
 * 测试用Netty服务端
 */
public class NettyTestServer  {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry registry = new DefaultServiceRegistry();
        registry.registry(helloService);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(9000);
    }
}
