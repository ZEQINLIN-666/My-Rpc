package pers.zeqinlin.rpc.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.RequestHandler;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;
import pers.zeqinlin.rpc.registry.DefaultServiceRegistry;
import pers.zeqinlin.rpc.registry.ServiceRegistry;

/**
 * Netty中处理RpcRequest的处理器
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static{
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try{
            logger.info("服务器接收到请求：{}",msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = requestHandler.handler(msg, service);
            ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(result));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程有错误发生");
        cause.getStackTrace();
        ctx.close();
    }
}
