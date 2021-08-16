package proxy;

import client.RpcClient;
import client.impl.NettyRpcClient;

/**
 * @author fanfanli
 * @date 2021/8/12
 */
public class ProxyFactory  {

    public static String type_jdk = "jdk";
    public static String type_cglib = "cglib";

    public static  <T> T getProxy(RpcClient rpcClient, Class<T> clazz, String type){
        if (type.equals(type_jdk)) {
            return  new JdkProxy(rpcClient).getProxy(clazz);

        } else if (type.equals(type_cglib)) {
            return new CglibProxy(rpcClient).getProxy(clazz);
        } else {
            return new JdkProxy(rpcClient).getProxy(clazz);
        }
    }

    public static  <T> T getProxy(RpcClient rpcClient, Class<T> clazz){
        return new JdkProxy(rpcClient).getProxy(clazz);
    }

    public static  <T> T getProxy(Class<T> clazz){
        return new JdkProxy(new NettyRpcClient()).getProxy(clazz);
    }
}

