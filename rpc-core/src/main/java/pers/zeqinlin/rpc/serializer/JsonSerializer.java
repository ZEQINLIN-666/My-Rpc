package pers.zeqinlin.rpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.enumeration.SerializerCode;

import java.io.IOException;

/**
 * 使用Json格式的序列化器
 */

public class JsonSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时发生错误:{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes,clazz);
            if(obj instanceof RpcRequest){
                obj = handleRequest(obj);
            }
            return obj;
        }catch (IOException e){
            logger.error("反序列化时发生错误:{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 这里由于使用Json序列化和反序列化Object数组，无法保证反序列化之后仍然为原实例的类型，所以需要重新判断处理
     * @param obj
     * @return
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParasTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParasTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParas()[i].getClass())){
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParas()[i]);
                rpcRequest.getParas()[i] = objectMapper.readValue(bytes,clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
