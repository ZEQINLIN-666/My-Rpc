package pers.zeqinlin.test;


import pers.zeqinlin.rpc.registry.DefaultServiceRegistry;
import pers.zeqinlin.rpc.registry.ServiceRegistry;
import pers.zeqinlin.rpc.server.RpcServer;

/**
 * 测试用的服务端
 */
public class TestServer {
    public static void main(String[] args) {
         HelloServiceImpl helloService = new HelloServiceImpl();
         ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
         serviceRegistry.registry(helloService);
         RpcServer rpcServer = new RpcServer(serviceRegistry);
         rpcServer.start(8000);
    }
}
