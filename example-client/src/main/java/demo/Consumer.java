package demo;



import client.RpcClient;
import client.impl.NettyRpcClient;
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
 * @author: fanfanli
 * @date: 2021/8/12
 */
public class Consumer {
    public static void main(String[] args) {

        //1.获取代理类
//        IUserService userService = ProxyFactory.getProxy(IUserService.class);
        RpcClient rpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy("127.0.0.1",8888,rpcClient);
        IUserService userService = rpcClientProxy.getProxy(IUserService.class);
        //2.触发InvocationHandler,进行远程代理
        User user = userService.findById(2L) ;
        System.out.println(user);

    }
}