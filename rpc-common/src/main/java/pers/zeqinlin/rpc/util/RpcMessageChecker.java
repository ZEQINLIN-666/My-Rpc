package pers.zeqinlin.rpc.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;
import pers.zeqinlin.rpc.enumeration.ResponseCode;
import pers.zeqinlin.rpc.enumeration.RpcError;
import pers.zeqinlin.rpc.exception.RpcException;

/**
 * 检查响应和请求
 */
@NoArgsConstructor
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){

        if(rpcResponse == null){
            logger.error("调用服务失败,serviceName:{}",rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME+":"+rpcRequest.getInterfaceName());
        }

        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH,INTERFACE_NAME+":"+rpcRequest.getInterfaceName());
        }
        if(rpcResponse.getStatusCode()  == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME+":"+rpcRequest.getInterfaceName());
        }


    }
}
