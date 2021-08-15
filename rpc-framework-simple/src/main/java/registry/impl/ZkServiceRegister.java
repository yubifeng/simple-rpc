package registry.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import util.CuratorUtils;

import javax.swing.*;
import java.net.InetSocketAddress;

/**
 * zookeeper注册实现类
 *
 * @author fanfanli
 * @date 2021/8/14
 */
public class ZkServiceRegister implements ServiceRegistry {
    //curator的zookeeper客户端
    private CuratorFramework client;


    public static Logger logger = LoggerFactory.getLogger(ZkServiceRegister.class);

    public ZkServiceRegister() {
//        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
//        //使用心跳监听状态
//        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
//                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
//        this.client.start();
        this.client = CuratorUtils.getZkClient();
        logger.info("zookeeper 连接成功");

    }
    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            //服务名为永久节点，地址为临时节点
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + serverAddress.getHostName()+":"+serverAddress.getPort();
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            //保存到Curator的set中，以实现自动注销服务
            CuratorUtils.REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            logger.error("此服务已存在");
        }
    }


    public void clearService(String serviceName, InetSocketAddress serverAddress) {
        try {

            String path = "/" + serviceName + "/" + serverAddress.getHostName()+":"+serverAddress.getPort();
            if(client.checkExists().forPath(path)!=null){
                client.delete().forPath(path);
            }
            if(client.checkExists().forPath("/"+serviceName) == null) {
                client.delete().forPath("/"+serviceName);
            }
        } catch (Exception e) {
            logger.error("服务删除失败");
        }



    }
}
