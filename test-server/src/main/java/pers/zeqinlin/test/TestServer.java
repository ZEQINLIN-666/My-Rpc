package pers.zeqinlin.test;


import pers.zeqinlin.rpc.server.RpcServer;

/**
 * 测试用的服务端
 */
public class TestServer {
    public static void main(String[] args) {
         HelloServiceImpl helloService = new HelloServiceImpl();
         RpcServer rpcServer = new RpcServer();
         rpcServer.register(helloService,8000);
    }
}
