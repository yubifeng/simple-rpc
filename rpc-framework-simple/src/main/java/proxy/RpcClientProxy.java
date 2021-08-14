package proxy;

import client.RpcClient;
import client.impl.NettyRpcClient;
import client.impl.SocketRpcClient;
import common.dto.RpcRequest;
import common.dto.RpcResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.impl.JsonSerializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author fanfanli
 * @date 2021/8/13
 */
@AllArgsConstructor
public class RpcClientProxy  implements InvocationHandler  {
    private String host;
    private int port;
    private  RpcClient rpcClient ;
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    @SuppressWarnings("unchecked")
    public   <T> T getProxy(Class<T> clazz ){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        //1.要调用的类、方法、参数
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .build();


        //2.建立远程连接
        Object result = rpcClient.sendRequest(rpcRequest, host, port);

        //处理泛型json反序列化错误的问题
        if (!result.getClass().isAssignableFrom(method.getReturnType())) {
           byte[]  sub =  new JsonSerializer().serialize(result);
           result = new JsonSerializer().deserialize(sub,method.getReturnType());
           logger.info("json泛型反序列化处理");

        }
        return  result;
    }






}
