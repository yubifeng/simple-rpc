package loadBalance;

import java.util.List;

/**
 * 给服务器地址列表，根据不同的负载均衡策略选择一个
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public interface LoadBalance {
    String selectServiceAddress(List<String> addressList);
}
