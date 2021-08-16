package registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public interface ServiceDiscovery {


    InetSocketAddress getService(String serviceName);

}
