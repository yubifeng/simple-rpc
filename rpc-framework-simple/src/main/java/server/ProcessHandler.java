package server;

import common.dto.RpcRequest;
import common.dto.RpcResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author: fanfanli
 * @date: 2021/8/12
 */
@AllArgsConstructor
public class ProcessHandler implements Runnable {

    private  Socket socket;
    private  Object service;


    @Override
    public void run()  {
        try {
            //1.接收所有的参数
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();
            Class clazz = null;

            //2.服务注册，找到具体的实现类
//            if (rpcRequest.getInterfaceName().equals(IUserService.class.getName())){
//                clazz = UserServiceImpl.class;
//            }

            //3.反射执行UserServiceImpl的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            Object result = method.invoke(service,rpcRequest.getParams());


            //4.返回结果给客户端
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(RpcResponse.success(result));
            outputStream.flush();


            //5.关闭连接
            outputStream.close();
            inputStream.close();

            socket.close();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }





    }
}
