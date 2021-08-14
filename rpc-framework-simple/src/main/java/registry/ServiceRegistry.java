package registry;

/**
 * 服务注册接口
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public interface ServiceRegistry {
    <T> void register(T service);
    Object getService(String serviceName);

}
