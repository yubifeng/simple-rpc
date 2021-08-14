package proxy;

import common.dto.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author fanfanli
 * @date 2021/8/12
 */
public class JdkProxyHandle implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //1.要调用的类、方法、参数

        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramTypes(method.getParameterTypes())
                .build();

        //2.建立远程连接
        Socket socket = new Socket("127.0.0.1", 8888);

        //3.传输类信息，请求远程执行结果
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(rpcRequest);
        outputStream.flush();

        //4.接收返回的结果
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object object = inputStream.readObject();

        inputStream.close();
        outputStream.close();
        socket.close();
        return object;


//        // 1、获得服务信息
//        String serviceName = this.clazz.getName();
//
//        List<Service> services = serviceDiscoverer.getServices(serviceName);
//
//        if (services == null || services.isEmpty()) {
//            throw new LeisureException("No provider available!");
//        }
//
//        // 随机选择一个服务提供者（软负载均衡）
//        Service service = services.get(random.nextInt(services.size()));
//
//        // 2、构造request对象
//        RpcRequest req = new RpcRequest();
////        req.setServiceName(service.getName());
//        req.setMethodName(method.getName());
//        req.setParamTypes(method.getParameterTypes());
//        req.setParams(args);
//
//        // 3、协议层编组
//        // 获得该方法对应的协议
//        MessageProtocol protocol = supportMessageProtocols.get(service.getProtocol());
//        // 编组请求
//        byte[] data = protocol.marshallingRequest(req);
//
//        // 4、调用网络层发送请求
//        byte[] repData = netClient.sendRequest(data, service);
//
//        // 5解组响应消息
//        LeisureResponse rsp = protocol.unmarshallingResponse(repData);
//
//        // 6、结果处理
//        if (rsp.getException() != null) {
//            throw rsp.getException();
//        }
//        return rsp.getReturnValue();
//        return "dsf";
    }
}



