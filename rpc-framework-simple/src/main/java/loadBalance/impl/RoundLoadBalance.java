package loadBalance.impl;

import loadBalance.LoadBalance;

import java.util.List;

/**
 * 轮询负载均衡 只能实现一个客户端多次调用轮询
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public class RoundLoadBalance implements LoadBalance {
    private volatile int choose = -1;

    @Override
    public String selectServiceAddress(List<String> addressList) {
        choose++;
        choose = choose % addressList.size();
        System.out.println("负载均衡选择了" + choose + "号服务器");
        return addressList.get(choose);
    }

}
