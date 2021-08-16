package client.synResponse;

import common.dto.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步获取响应
 *
 * @author fanfanli
 * @date 2021/8/16
 */
public class UnprocessedRequests {
    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>> unprocessedRequests = new ConcurrentHashMap<>();

    public static void put(String requestId, CompletableFuture<RpcResponse> future) {
        unprocessedRequests.put(requestId, future);
    }

    public static void remove(String requestId) {
        unprocessedRequests.remove(requestId);
    }

    public static void complete(RpcResponse rpcResponse){
        //请求完成了，把请求从未完成的请求中移除
        CompletableFuture<RpcResponse> future = unprocessedRequests.remove(rpcResponse.getResponseId());
        if(null != future){
            //把响应对象放入future中
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }
}
