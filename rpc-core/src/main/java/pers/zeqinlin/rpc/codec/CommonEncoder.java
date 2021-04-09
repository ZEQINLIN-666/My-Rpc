package pers.zeqinlin.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.enumeration.PackageType;
import pers.zeqinlin.rpc.serializer.CommonSerializer;

/**
 * 通用的编码拦截器
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(MAGIC_NUMBER);
        if(msg instanceof RpcRequest){
            byteBuf.writeInt(PackageType.REQUEST_PACKAGE.getCode());
        }else{
            byteBuf.writeInt(PackageType.RESPONSE_PACKAGE.getCode());
        }
        byteBuf.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
