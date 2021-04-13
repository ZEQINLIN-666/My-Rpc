package pers.zeqinlin.rpc.transport;

import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
