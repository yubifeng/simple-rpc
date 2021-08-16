package server.impl;

import annotation.RpcService;
import annotation.RpcServiceScan;
import codec.MyDecoder;
import codec.MyEncoder;
import common.enums.RpcErrorMessage;
import exception.RpcException;
import hook.Shutdown;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import serializer.impl.ProtostuffSerializer;
import server.RpcServer;
import server.handler.NettyServerHandler;
import util.RpcReflectUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fanfanli
 * @date 2021/8/13
 */

public class NettyRpcServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);
    private ServiceProvider serviceProvider;
    private Channel channel;

    public NettyRpcServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        //自动扫描服务
        scanServices();
    }

    @Override
    public void start(int port) {
        //创建两个线程组 boosGroup、workerGroup boss负责建立连接， work负责具体的请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数 启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //启动netty服务器
            serverBootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 开启 TCP 底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //增加编解码器
                            pipeline.addLast(new MyEncoder(new ProtostuffSerializer()));
                            pipeline.addLast(new MyDecoder());
                            //心跳包处理
                            pipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
                            //给pipeline管道设置处理器
                            pipeline.addLast(new NettyServerHandler(serviceProvider));
                        }
                    });
            //增加一个钩子，以实现关闭服务器时自动注销服务
            Shutdown.getInstance().addClearAllServiceHook();
            //绑定端口号，启动服务端
            ChannelFuture future = serverBootstrap.bind(port).sync();
            channel = future.channel();
            //对通道进行死循环监听
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {

            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            //释放线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    @Override
    public void stop() {
        this.channel.close();
    }

    /**
     * 自动扫描加上RpcService注解的实现类进行自动注册
     */
    public void scanServices() {
        String mainClassName = RpcReflectUtils.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(RpcServiceScan.class)) {
                logger.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcErrorMessage.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            logger.error("出现未知错误");
            throw new RpcException(RpcErrorMessage.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(RpcServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = RpcReflectUtils.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        serviceProvider.publishService(obj);
                    }
                } else {
                    serviceProvider.publishService(obj);
                }
            }
        }
    }

}
