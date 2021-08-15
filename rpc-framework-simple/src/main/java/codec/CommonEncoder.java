package codec;

import common.dto.RpcRequest;
import common.enums.PackageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.CommonSerializer;


/**
 * 自定义的编码器
 *
 * @author ziyang
 */
@AllArgsConstructor
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;
    private static final Logger logger = LoggerFactory.getLogger(CommonEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {

        //写入魔术
        out.writeInt(MAGIC_NUMBER);

        //写入消息类型
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }

        //写入序列化方式
        out.writeInt(serializer.getCode());

        //得到序列化字节数组
        byte[] bytes = serializer.serialize(msg);
        //写入长度
        out.writeInt(bytes.length);
        //写入序列化字节数组
        out.writeBytes(bytes);

        logger.info("编码结果{}",out.toString());

    }

}
