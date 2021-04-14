package pers.zeqinlin.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.enumeration.RpcError;
import pers.zeqinlin.rpc.exception.RpcException;
import pers.zeqinlin.rpc.provider.ServiceProvider;
import pers.zeqinlin.rpc.provider.ServiceProviderImpl;
import pers.zeqinlin.rpc.registry.NacoServiceRegistry;
import pers.zeqinlin.rpc.registry.ServiceRegistry;
import pers.zeqinlin.rpc.serializer.CommonSerializer;
import pers.zeqinlin.rpc.transport.RpcServer;
import pers.zeqinlin.rpc.codec.CommonDecoder;
import pers.zeqinlin.rpc.codec.CommonEncoder;
import pers.zeqinlin.rpc.serializer.KryoSerializer;

import java.net.InetSocketAddress;

/**
 * Netty服务端
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacoServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host,port));
        start();
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer)); //自定义编码器
                            pipeline.addLast(new CommonDecoder()); //自定义解码器
                            pipeline.addLast(new NettyServerHandler()); //自定义处理器
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(host,port).sync();
            channelFuture.channel().closeFuture().sync();

        }catch (InterruptedException e){
            logger.error("服务器启动时发生错误: ",e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }


}
