package util;

import common.dto.RpcRequest;
import common.dto.RpcResponse;
import common.enums.ResponseCode;
import common.enums.RpcErrorMessage;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查请求和响应
 *
 * @author fanfanli
 * @date 2021/8/15
 */
public class RpcMessageChecker {
    private static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if (rpcResponse == null) {
            logger.error("调用服务失败，返回响应为空");
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAILURE, "interfaceName" + ":" + rpcRequest.getInterfaceName());
        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getResponseId())) {
            logger.error("响应与请求的请求号不同");
            throw new RpcException(RpcErrorMessage.RESPONSE_NOT_MATCH, "interfaceName" + ":" + rpcRequest.getInterfaceName());
        }
        if(rpcResponse.getCode() == null || !rpcResponse.getCode().equals(ResponseCode.SUCCESS.getValue())){
            logger.error("调用服务失败，serviceName:{}，RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAILURE, "interfaceName" + ":" + rpcRequest.getInterfaceName());
        }

    }
}
