package pers.zeqinlin.rpc.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;
import pers.zeqinlin.rpc.enumeration.ResponseCode;
import pers.zeqinlin.rpc.provider.ServiceProvider;
import pers.zeqinlin.rpc.provider.ServiceProviderImpl;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 进行过程调用的处理器
 */
public class RequestHandler  {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handler(RpcRequest rpcRequest){
        Object result = null;
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        try{
            result = invokeTargetMethod(rpcRequest,service);
            logger.info("服务{}成功调用方法:{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        }catch (IllegalAccessException| InvocationTargetException e){
            logger.error("调用或发送时有错误发生：",e);
        }
        return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try{
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParasTypes());
        }catch (NoSuchMethodException e){
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD,rpcRequest.getRequestId());
        }
        return method.invoke(service, rpcRequest.getParas());
    }



}
