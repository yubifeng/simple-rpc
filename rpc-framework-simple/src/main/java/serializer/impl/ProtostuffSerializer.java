package serializer.impl;

import common.enums.SerializerCode;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import serializer.CommonSerializer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fanfanli
 * @date 2021/8/15
 */
public class ProtostuffSerializer implements CommonSerializer {

    /**
     * 避免每次序列化都重新申请Buffer空间,用来存放对象序列化之后的数据
     * 如果你设置的空间不足，会自动扩展的，但这个大小还是要设置一个合适的值，设置大了浪费空间，设置小了会自动扩展浪费时间
     */
    private LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);


    /**
     * 缓存类对应的Schema，由于构造schema需要获得对象的类和字段信息，会用到反射机制
     *这是一个很耗时的过程，因此进行缓存很有必要，下次遇到相同的类直接从缓存中get就行了
     */
    private Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    @Override
    public byte[] serialize(Object obj) {


        Class clazz = obj.getClass();
        //如果schemaCache中有缓存 使用缓存
        Schema schema = schemaCache.get(clazz);
        if (Objects.isNull(schema)) {
            schema = RuntimeSchema.getSchema(clazz);
            schemaCache.put(clazz, schema);
        }
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);

        }finally {
            //使用完清空buffer
            buffer.clear();
        }
        return data;
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        //如果schemaCache中有缓存 使用缓存
        Schema schema = schemaCache.get(clazz);
        if (Objects.isNull(schema)) {
            schema = RuntimeSchema.getSchema(clazz);
            schemaCache.put(clazz, schema);
        }
        Object obj = schema.newMessage();
        //反序列化操作，将字节数组转换为对应的对象
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("PROTOSTUFF").getCode();
    }
}
