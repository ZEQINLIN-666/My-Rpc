package pers.zeqinlin.rpc.transport.socket.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.transport.RequestHandler;
import pers.zeqinlin.rpc.registry.ServiceRegistry;
import pers.zeqinlin.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket方式进行远程方法调用的服务端
 */
public class SocketServer {


    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int  KEEP_ALIVE_TIME= 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;
    private RequestHandler requestHandler = new RequestHandler();

    /**
     * 在创建 SocketServer 对象时，传入一个 ServiceRegistry 作为这个服务的注册表。
     * @param serviceRegistry
     */
    public SocketServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);

    }

    
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务端启动...");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
               logger.info("消费者连接:{}:{}",socket.getInetAddress(),socket.getPort());
               threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry,serializer));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.error("服务器启动时有错误发生:",e);
        }
    }
}
