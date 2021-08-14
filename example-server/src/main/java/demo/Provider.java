package demo;

import registry.ServiceRegistry;
import registry.impl.DefaultServiceRegistry;
import server.RpcServer;
import server.handler.NettyServerHandler;
import server.impl.NettyRpcServer;
import server.impl.SocketRpcServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: fanfanli
 * @date: 2021/8/12
 */
public class Provider {

    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(userService);
        RpcServer rpcServer = new NettyRpcServer();
        rpcServer.start(8888);
    }
}
