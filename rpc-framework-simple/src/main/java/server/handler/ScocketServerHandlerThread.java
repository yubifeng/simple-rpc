package server.handler;

import common.dto.RpcRequest;
import common.dto.RpcResponse;
import common.enums.ResponseCode;
import lombok.AllArgsConstructor;
import provider.ServiceProvider;
import registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 负责解析得到的request请求，执行服务方法，返回给客户端
 * @author: fanfanli
 * @date: 2021/8/12
 */
@AllArgsConstructor
public class ScocketServerHandlerThread implements Runnable {

    private  Socket socket;
    private final ServiceProvider serviceProvider;
    private final static RequestHandler requestHandler;
    static {
        requestHandler = new RequestHandler();
    }

    @Override
    public void run()  {
        try ( ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())){
            //1.接收所有的参数
            RpcRequest rpcRequest = (RpcRequest) inputStream.readObject();

            //2.服务注册，找到具体的实现类
//            if (rpcRequest.getInterfaceName().equals(IUserService.class.getName())){
//                clazz = UserServiceImpl.class;
//            }

            //3.反射执行UserServiceImpl的方法
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceProvider.getServiceImp(interfaceName);
            RpcResponse rpcResponse = requestHandler.handle(rpcRequest, service);

            //4.返回结果给客户端
            outputStream.writeObject(rpcResponse);
            outputStream.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
