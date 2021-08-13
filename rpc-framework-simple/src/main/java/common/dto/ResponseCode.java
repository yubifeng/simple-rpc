package common.dto;

import lombok.*;

/**
 * 响应码
 *
 * @author fanfanli
 * @date 2021/8/12
 */

@Getter
@ToString
@AllArgsConstructor
public enum  ResponseCode  {

    /**
     * 成功响应
     */
    SUCCESS(200),

    /**
     * 处理错误
     */
    ERROR500(500),

    /**
     * 404找不到出错
     */
    ERROR404(404),
    ;

    private Integer value;

}
