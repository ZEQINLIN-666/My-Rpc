package pers.zeqinlin.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zeqinlin.rpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * 服务提供方回送消息给消费者的结果对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {


    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态的备注信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T Data;

    /**
     * 响应成功信息
     */
    public static<T> RpcResponse<T> success(T Data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());  //将状态码封装到ResponseCode对象中
        response.setData(Data);
        return response;
    }

    /**
     * 响应失败信息
     */
    public static<T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
