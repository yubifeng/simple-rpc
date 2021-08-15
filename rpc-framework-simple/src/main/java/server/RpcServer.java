package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务的接口
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public interface RpcServer {
    void start(int port);
    void stop();
}
