package pers.zeqinlin.test;


import pers.zeqinlin.rpc.registry.DefaultServiceRegistry;
import pers.zeqinlin.rpc.registry.ServiceRegistry;
import pers.zeqinlin.rpc.socket.server.SocketServer;


/**
 * Socket方法~测试用的服务端
 */
public class SocketTestServer {
    public static void main(String[] args) {
         HelloServiceImpl helloService = new HelloServiceImpl();
         ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
         serviceRegistry.registry(helloService);
         SocketServer socketServer = new SocketServer(serviceRegistry);
         socketServer.start(8000);
    }
}
