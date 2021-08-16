package proxy;

import client.RpcClient;
import common.dto.RpcRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.impl.JsonSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 客户端动态代理类
 *
 * @author fanfanli
 * @date 2021/8/13
 */
@AllArgsConstructor
public class JdkProxy implements InvocationHandler  {



    private  RpcClient rpcClient ;
    private static final Logger logger = LoggerFactory.getLogger(JdkProxy.class);

    /**
     * 获取一个代理类
     */
    @SuppressWarnings("unchecked")
    public   <T> T getProxy(Class<T> clazz ){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},this);
    }

    /**
     * jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        //1.要调用的类、方法、参数
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .requestId(UUID.randomUUID().toString())
                .paramTypes(method.getParameterTypes())
                .build();


        //2.发送数据获得返回结果
        Object result = rpcClient.sendRequest(rpcRequest);

        //只用于JSON序列化：处理泛型json反序列化错误的问题 原因时json不知道Data类型
        if (result!=null&&!result.getClass().isAssignableFrom(method.getReturnType())) {
           byte[]  sub =  new JsonSerializer().serialize(result);
           result = new JsonSerializer().deserialize(sub,method.getReturnType());
           logger.info("json泛型反序列化处理");
        }
        return  result;
    }






}
