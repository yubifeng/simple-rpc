package serializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.RpcRequest;
import common.enums.SerializerCode;
import exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.MySerializer;

import java.io.IOException;

/**
 * @author fanfanli
 * @date 2021/8/13
 */
public class JsonSerializer implements MySerializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);

            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
                logger.error("进入判断处理");
            }
            logger.error("进入判断处理");
            logger.info("{}",obj.toString());
            return obj;
        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    /*
        这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
        需要重新判断处理
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParams()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParams()[i]);
                rpcRequest.getParams()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }


    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
