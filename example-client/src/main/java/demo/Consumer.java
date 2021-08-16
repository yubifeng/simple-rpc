package demo;



import client.RpcClient;
import client.impl.NettyRpcClient;
import model.Blog;
import model.User;
import proxy.JdkProxy;
import proxy.ProxyFactory;

/**
 * 服务消费者
 * 测试
 *
 * @author fanfanli
 * @date 2021/8/12
 */
public class Consumer {
    public static void main(String[] args) {

        //获取socket代理客户端
//        RpcClient rpcClient = new SocketRpcClient();

        //获取netty代理客户端
        RpcClient rpcClient = new NettyRpcClient();
        //代理接口，调用远程服务
        IUserService userService = ProxyFactory.getProxy(rpcClient,IUserService.class,ProxyFactory.type_cglib);
        User user = userService.findById(2L) ;
        User user1 = userService.findById(2L) ;
        System.out.println(user);

        BlogService blogService = ProxyFactory.getProxy(rpcClient,BlogService.class,ProxyFactory.type_jdk);
        Blog blog = blogService.getBlogById(22);
        System.out.println(blog);

    }
}