package common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化和反序列化器
 *
 * @author ziyang
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    /**
     * Kyro
     */
    KRYO(0),
    /**
     * json
     */
    JSON(1),
    /**
     * hessian2
     */
    HESSIAN(2),
    /**
     * fst
     */
    FST(3),
    /**
     * protostuff
     */
    PROTOSTUFF(4);

    private final int code;

}
