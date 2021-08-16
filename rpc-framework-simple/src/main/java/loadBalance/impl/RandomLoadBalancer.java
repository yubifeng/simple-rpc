package loadBalance.impl;

import loadBalance.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public class RandomLoadBalancer implements LoadBalancer {


    @Override
    public String selectServiceAddress(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("负载均衡选择了" + choose + "号服务器");
        return addressList.get(choose);
    }
}
