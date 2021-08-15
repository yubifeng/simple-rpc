package demo;

import annotation.RpcServiceScan;
import provider.ServiceProvider;
import provider.impl.ZkServiceProvider;
import server.RpcServer;
import server.impl.NettyRpcServer;


/**
 * 服务提供者
 * 测试
 *
 * @author fanfanli
 * @date 2021/8/12
 */
@RpcServiceScan
public class Provider {


    public static void main(String[] args) {
        //服务实现类
        UserServiceImpl userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        //暴露服务
        ServiceProvider serviceProvider = new ZkServiceProvider("127.0.0.1",8894);
        //可以通过注解实现自动注册（nettyServer中）
//        serviceProvider.publishService(userService);
//        serviceProvider.publishService(blogService);
        //启动Socket服务器
//        RpcServer rpcServer = new SocketRpcServer(serviceProvider);
//        rpcServer.start(8889);

        //启动netty服务器
        RpcServer rpcServer = new NettyRpcServer(serviceProvider);
        rpcServer.start(8894);
    }
}
