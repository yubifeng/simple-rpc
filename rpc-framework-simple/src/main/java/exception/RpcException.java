package exception;

import common.enums.RpcErrorMessage;

/**
 * 自定义Rpc异常
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorMessage rpcErrorMessage, String detail) {
        super(rpcErrorMessage.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessage rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }



}
