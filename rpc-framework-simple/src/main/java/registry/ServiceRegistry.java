package registry;

import java.net.InetSocketAddress;
import java.rmi.registry.Registry;

/**
 * 服务注册接口
 * 两大基本功能，注册：保存服务与地址。 查询：根据服务名查找地址
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public interface ServiceRegistry {
    //
    void register(String serviceName,InetSocketAddress serverAdderss);


}
