package provider.impl;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import registry.ServiceRegistry;
import registry.impl.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的服务注册表，保存服务端本地服务
 *
 * @author fanfanli
 * @date 2021/8/15
 */
@Data
public class ZkServiceProvider implements ServiceProvider {

    private  ConcurrentHashMap<String, Object> interfaceProvider;

    private  ServiceRegistry serviceRegistry;

    private  String host;
    private  int port;
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceProvider.class);

    public ZkServiceProvider(String host, int port){
        // 需要传入服务端自身的服务的网络地址
        this.host = host;
        this.port = port;
        this.serviceRegistry = new ZkServiceRegister();
        this.interfaceProvider = new ConcurrentHashMap<String, Object>();
    }

    @Override
    public <T> void publishService(T service) {

        Class<?>[] interfaces = service.getClass().getInterfaces();
        logger.error("注册");
        for (Class clazz : interfaces) {
            //本机的映射表
            interfaceProvider.put(clazz.getName(), service);
            //在注册中心注册服务
            serviceRegistry.register(clazz.getName(), new InetSocketAddress(host,port));
            logger.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(),service);
        }

    }

    @Override
    public Object getServiceImp(String serviceName) {
        return interfaceProvider.get(serviceName);
    }
}
