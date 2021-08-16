package demo;



import client.RpcClient;
import client.impl.NettyRpcClient;
import client.impl.SocketRpcClient;
import model.Blog;
import model.User;
import proxy.ProxyFactory;
import proxy.RpcClientProxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * 服务消费者
 * 测试
 *
 * @author fanfanli
 * @date 2021/8/12
 */
public class Consumer {
    public static void main(String[] args) {

        //获取socket代理客户端
//        RpcClient rpcClient = new SocketRpcClient();

        //获取netty代理客户端
        RpcClient rpcClient = new NettyRpcClient();
        //获取代理类
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        //代理接口，调用远程服务
        IUserService userService = rpcClientProxy.getProxy(IUserService.class);
        User user = userService.findById(2L) ;

        System.out.println(user);

        BlogService blogService = rpcClientProxy.getProxy(BlogService.class);
        Blog blog = blogService.getBlogById(22);
        System.out.println(blog);

    }
}