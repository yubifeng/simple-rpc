package serializer.impl;

import common.enums.SerializerCode;

import org.nustaq.serialization.FSTConfiguration;
import serializer.MySerializer;

/**
 * @author fanfanli
 * @date 2021/8/14
 */
public class FstSerializer implements MySerializer {


    private final ThreadLocal<FSTConfiguration> conf = ThreadLocal.withInitial(() -> {
        FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
        return conf;
    });



    @Override
    public byte[] serialize(Object obj) {
        return conf.get().asByteArray(obj);
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        return conf.get().asObject(bytes);
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("FST").getCode();
    }





}
