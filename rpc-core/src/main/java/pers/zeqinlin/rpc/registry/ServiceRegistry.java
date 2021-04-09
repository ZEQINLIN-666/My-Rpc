package pers.zeqinlin.rpc.registry;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册至注册表中
     * @param service 待注册的服务实体
     * @param <T>   服务实体类
     */
    <T> void registry(T service);

    /**
     * 根据服务的名称获取服务实体
     * @param serviceName 服务名称
     * @return  服务实体
     */
    Object getService(String serviceName);
}
