package pers.zeqinlin.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;
import pers.zeqinlin.rpc.enumeration.PackageType;
import pers.zeqinlin.rpc.enumeration.RpcError;
import pers.zeqinlin.rpc.exception.RpcException;
import pers.zeqinlin.rpc.serializer.CommonSerializer;

import java.util.List;

/**
 * 通用的解码拦截器
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> outList) throws Exception {
        int magic = byteBuf.readInt();
        if(magic != MAGIC_NUMBER){
            logger.error("不识别的协议包:",magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        int packageCode = byteBuf.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACKAGE.getCode()){
            packageClass = RpcRequest.class;
        }else if(packageCode == PackageType.RESPONSE_PACKAGE.getCode()){
            packageClass = RpcResponse.class;
        }else{
            logger.error("不识别的数据包:{}",packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        int serializerCode = byteBuf.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null){
            logger.error("不识别的(反)序列化器:{}",serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }

        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        outList.add(obj);

    }
}
