package server.handler;


import common.dto.RpcRequest;
import common.dto.RpcResponse;
import common.enums.ResponseCode;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射调用服务实现类的方法
 *
 * @author shuang.kou
 * @createTime 2020年05月13日 09:05:00
 */
@Slf4j
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RpcResponse handle(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            result = method.invoke(service, rpcRequest.getParams());
            logger.info("执行结果{}",result);
            return RpcResponse.success(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
            return RpcResponse.fail(ResponseCode.ERROR500, "调用时发送错误");
        } catch (NoSuchMethodException e) {
            logger.error("不存在这个方法：", e);
            return RpcResponse.fail(ResponseCode.ERROR404,"不存在该方法");
        }
    }

}
