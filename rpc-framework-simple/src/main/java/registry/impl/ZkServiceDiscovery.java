package registry.impl;

import loadBalance.LoadBalancer;
import loadBalance.impl.RandomLoadBalancer;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceDiscovery;
import util.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zookeeper的注册中心
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public class ZkServiceDiscovery implements ServiceDiscovery {


    //curator的zookeeper客户端
    private CuratorFramework client;
    // zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";

    private final LoadBalancer loadBalancer;
    //服务的本地缓存
    private ConcurrentHashMap<String, String> serviceAddressCache;
    public static Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    public ZkServiceDiscovery() {
//        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
//        //使用心跳监听状态
//        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
//                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
//        this.client.start();
        client = CuratorUtils.getZkClient();
        logger.info("zookeeper 连接成功");
        loadBalancer = new RandomLoadBalancer();
        serviceAddressCache = new ConcurrentHashMap<>();
    }

    @Override
    public InetSocketAddress getService(String serviceName) {

        try {
            String address = null;
            if (serviceAddressCache.containsKey(serviceName)) {
                address = serviceAddressCache.get(serviceName);
                logger.info("调用本地缓存");
            }
            else {
                List<String> serverAddresses = client.getChildren().forPath("/"+serviceName);
                //进行负载均衡
                address = loadBalancer.selectServiceAddress(serverAddresses);
                serviceAddressCache.put(serviceName, address);
            }
            String[] result = address.split(":");
            return new InetSocketAddress(result[0], Integer.parseInt(result[1]));

        } catch (Exception e) {
            logger.error("获取服务失败");
            return null;
        }
    }
}
