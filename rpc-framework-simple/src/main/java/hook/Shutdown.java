package hook;

import ch.qos.logback.core.hook.ShutdownHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CuratorUtils;


/**
 * 实现关闭服务后自动注销所有服务
 * 单例模式
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public class Shutdown {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);


    private volatile static  Shutdown instance;
    private Shutdown() {
    }


    public static Shutdown getInstance() {
        if (instance == null) {
            synchronized (Shutdown.class) {
                if (instance == null) {
                    instance = new Shutdown();
                }
            }
        }
        return instance;
    }


    public void addClearAllServiceHook() {
        //增加一个hook，当关闭服务器后自动注销注册中心的服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("注销服务");
            CuratorUtils.clearRegistry();
        }));
    }

}
