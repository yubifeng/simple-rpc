package serializer.impl;

/**
 * 基于Hessian协议的序列化器
 *
 * @author fanfanli
 * @date 2021/8/13
 */

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import common.enums.SerializerCode;
import exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.MySerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements MySerializer {

    private static final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);

    @Override
    public byte[] serialize(Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            //序列化
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        } finally {
            if (hessianOutput != null) {
                try {
                    hessianOutput.close();
                } catch (IOException e) {
                    logger.error("关闭流时有错误发生:", e);
                }
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            //反序列化
            hessianInput = new HessianInput(byteArrayInputStream);
            return hessianInput.readObject();

        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        } finally {
            if (hessianInput != null) {
                hessianInput.close();
            }
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("HESSIAN").getCode();
    }
}
