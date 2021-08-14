package proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author fanfanli
 * @date 2021/8/12
 */
public class ProxyFactory  {

    public static String type_jdk = "jdk";
    public static String type_cglib = "cglib";

    public static  <T> T getProxy(Class<T> clazz ){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},new JdkProxyHandle());
    }
}

