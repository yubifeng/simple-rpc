package demo;

import server.RpcServer;

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
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(userService, 8888);
    }
}
