package pers.zeqinlin.rpc.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消费方发送给服务提供者的请求对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcRequest  implements Serializable {
    /**
     * 待调用的接口名
     */
    private String interfaceName;
    /**
     * 待调用的方法名
     */
    private String methodName;
    /**
     * 调用方法的参数
     */
    private Object[] paras;
    /**
     * 调用方法的参数类型
     * 由于方法重载的缘故，我们还需要这个方法的所有参数的类型
     */
    private Class<?>[] parasTypes;
}
