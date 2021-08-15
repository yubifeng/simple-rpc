package server;

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
