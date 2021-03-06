package pers.zeqinlin.rpc.transport.socket.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zeqinlin.rpc.registry.NacoServiceRegistry;
import pers.zeqinlin.rpc.registry.ServiceRegistry;
import pers.zeqinlin.rpc.serializer.CommonSerializer;
import pers.zeqinlin.rpc.transport.RpcClient;
import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.entity.RpcResponse;
import pers.zeqinlin.rpc.enumeration.ResponseCode;
import pers.zeqinlin.rpc.enumeration.RpcError;
import pers.zeqinlin.rpc.exception.RpcException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * Socket进行远程方法调用的客户端
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private CommonSerializer serializer;
    private final ServiceRegistry serviceRegistry;
    public SocketClient() {
       this.serviceRegistry = new NacoServiceRegistry();
    }

    public Object sendRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket()) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            if (rpcResponse == null) {
                logger.error("服务调用失败,servive:{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "serivice:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败,service:{},response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();

        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时发生错误：", e);
            throw new RpcException("服务调用失败:", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
