package util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.impl.ZkServiceProvider;
import registry.ServiceRegistry;
import registry.impl.ZkServiceRegister;

import javax.xml.ws.Service;
import java.net.InetSocketAddress;
import java.rmi.registry.Registry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fanfanli
 * @date 2021/8/15
 */
public class CuratorUtils {

    public static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;
    // zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final Logger logger = LoggerFactory.getLogger(CuratorUtils.class);

    public static void clearRegistry() {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                zkClient.delete().forPath(p);

            } catch (Exception e) {
                logger.error("注销该服务时失败 path [{}]", p);
            }
        });
        logger.info("所有服务已注销[{}]", REGISTERED_PATH_SET.toString());
    }

    //获取唯一的zookeeper客户端
    public static CuratorFramework getZkClient() {
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        //使用心跳监听状态
        zkClient = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        zkClient.start();
        return zkClient;
    }
}
