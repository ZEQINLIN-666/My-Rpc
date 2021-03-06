package pers.zeqinlin.rpc.serializer;

/**
 * 通用的序列化与反序列化接口
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);
    Object deserialize(byte[] bytes,Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code){
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessionSerializer();
            case 3:
                return new ProtostuffSerializer();
            default:
                return null;
        }
    }
}
