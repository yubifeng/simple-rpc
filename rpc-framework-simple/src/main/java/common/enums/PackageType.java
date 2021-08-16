package common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 包类型
 *
 * @author ziyang
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 请求包
     */
    REQUEST_PACK(0),
    /**
     * 响应包
     */
    RESPONSE_PACK(1);

    private final int code;

}
