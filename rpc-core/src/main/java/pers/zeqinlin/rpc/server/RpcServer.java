package pers.zeqinlin.rpc.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 远程方法调用的服务端
 */
public class RpcServer {

    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer(){
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;

        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public void register(Object service,int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务端正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("客户端已连接！IP为:"+socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket,service));
            }

        }catch (IOException e){
            logger.error("连接时发生错误:",e);
        }
    }
}
