package serializer;

import serializer.impl.*;

/**
 * 通用的序列化反序列化接口
 *
 * @author fanfanli
 * @date 2021/8/13
 */
public interface MySerializer {

    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer FST_SERIALIZER = 3;

    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

    /**
     * 根据序号取出序列化器
     */
    static MySerializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            case 3:
                return new FstSerializer();
            case 4:
                return new ProtostuffSerializer();
            default:
                return null;
        }
    }

    /**
     * 把对象序列化成字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 从字节数组反序列化成消息, 使用java自带序列化方式不用messageType也能得到相应的对象
     * 其它方式需指定消息格式，再根据message转化成相应的对象
     */
    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
