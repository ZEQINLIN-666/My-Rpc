package pers.zeqinlin.rpc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.RpcServer;
import pers.zeqinlin.rpc.codec.CommonDecoder;
import pers.zeqinlin.rpc.codec.CommonEncoder;
import pers.zeqinlin.rpc.serializer.JsonSerializer;

/**
 * Netty服务端
 */
public class NettyServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    @Override
    public void start(int port) {
        EventLoopGroup bossGruop = new NioEventLoopGroup();
        EventLoopGroup workerGruop = new NioEventLoopGroup();

        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGruop,workerGruop)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG,256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(new JsonSerializer())); //自定义编码器
                            pipeline.addLast(new CommonDecoder()); //自定义解码器
                            pipeline.addLast(new NettyServerHandler()); //自定义处理器
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

        }catch (InterruptedException e){
            logger.error("服务器启动时发生错误: ",e);
        }finally {
            bossGruop.shutdownGracefully();
            workerGruop.shutdownGracefully();
        }


    }
}
