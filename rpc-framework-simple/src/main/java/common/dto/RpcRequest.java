package common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RCP请求实体
 *
 * @author fanfanli
 * @date 2021/8/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest  implements Serializable {
    private static final long serialVersionUID = 5661720043123218215L;
//    /**
//     * 请求id
//     */
//    private String id;

    /**
     * 请求接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数数组
     */
    private Object[] params;

    /**
     * 参数类型数组
     */
    private Class<?>[] paramTypes;

//    /**
//     * 版本号
//     */
//    private String version;
    /**
     * 是否未心跳包
     */
    private boolean heartbeat;
}
