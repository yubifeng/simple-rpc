package codec;

import common.dto.RpcRequest;
import common.dto.RpcResponse;
import common.enums.PackageType;
import common.enums.RpcErrorMessage;
import exception.RpcException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.CommonSerializer;


import java.util.List;

/**
 * 自定义的解码器
 *
 * @author fanfanli
 */
public class CommonDecoder extends ReplayingDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //获取魔术
        int magic = in.readInt();
        logger.error("魔术: {}", magic);
        if (magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcErrorMessage.UNKNOWN_PROTOCOL);
        }
        //获取消息类型
        int packageCode = in.readInt();
        logger.info("消息类型: {}", packageCode);
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcErrorMessage.UNKNOWN_PACKAGE_TYPE);
        }
        //根据类型得到相应的序列化器
        int serializerCode = in.readInt();
        logger.error("序列化器类型: {}", serializerCode);
        CommonSerializer serializer = CommonSerializer.getSerializerByCode(serializerCode);
        if (serializer == null) {
            logger.error("不识别的序列化器: {}", serializerCode);
            throw new RpcException(RpcErrorMessage.UNKNOWN_SERIALIZER);
        }
        //读取数据序列化后的字节长度
        int length = in.readInt();
        logger.info("包长度: {}", length);
        //读取序列化后的字节数组
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        //用对应的序列化器解码字节数组
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }

}
