package pers.zeqinlin.rpc;

import pers.zeqinlin.rpc.entity.RpcRequest;

/**
 * 客户端类通用接口
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
