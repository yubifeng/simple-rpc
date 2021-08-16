package proxy;

import client.RpcClient;
import common.dto.RpcRequest;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.impl.JsonSerializer;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author fanfanli
 * @date 2021/8/16
 */
@AllArgsConstructor
public class CglibProxy implements MethodInterceptor {

    private RpcClient rpcClient ;
    private static final Logger logger = LoggerFactory.getLogger(JdkProxy.class);

    // 定义代理的生成方法,用于创建代理对象
    public   <T> T getProxy(Class<T> clazz ){
        //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        // 为代理对象设置父类，即指定目标类
        enhancer.setSuperclass(clazz);
        /**
         * 设置回调接口对象 注意，只所以在setCallback()方法中可以写上this，
         * 是因为MethodIntecepter接口继承自Callback，是其子接口
         */
        enhancer.setCallback(this);

        // create用以生成CGLib代理对象
        return (T) enhancer.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //1.要调用的类、方法、参数
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(objects)
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
