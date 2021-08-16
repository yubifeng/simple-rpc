package provider;

/**
 * @author fanfanli
 * @date 2021/8/15
 */
public interface ServiceProvider {


    <T> void publishService(T service);

    Object getServiceImp(String serviceName);

}
