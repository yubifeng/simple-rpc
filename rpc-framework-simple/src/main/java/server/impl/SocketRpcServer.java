package server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import server.handler.ScocketServerHandlerThread;
import server.RpcServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author fanfanli
 * @date 2021/8/13
 */
public class SocketRpcServer implements RpcServer{
    private ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private final ServiceRegistry serviceRegistry;

    public  SocketRpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;

        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    @Override
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new ScocketServerHandlerThread(socket, serviceRegistry));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生：", e);
        }
    }
}
