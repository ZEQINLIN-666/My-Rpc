package pers.zeqinlin.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.handler.RequestHandler;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;

import pers.zeqinlin.rpc.util.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;

/**
 * Netty中处理RpcRequest的处理器
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;

    static{
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        threadPool.execute(() ->{
            try{
                logger.info("服务器接收到请求：{}",msg);
                Object result = requestHandler.handler(msg);
                ChannelFuture channelFuture = ctx.writeAndFlush(RpcResponse.success(result,msg.getRequestId()));
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }finally {
                ReferenceCountUtil.release(msg);
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程有错误发生");
        cause.getStackTrace();
        ctx.close();
    }
}
