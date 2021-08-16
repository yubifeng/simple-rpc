package server.handler;

import common.dto.RpcRequest;
import common.dto.RpcResponse;
import io.netty.channel.*;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;


/**
 * Netty中处理RpcRequest的Handler
 *
 * @author ziyang
 */
@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);


    private static RequestHandler requestHandler;
    private  ServiceProvider serviceProvider;

    static {
        requestHandler = new RequestHandler();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            logger.info("服务器接收到请求: {}", msg);

            if(msg.isHeartBeat()){
                logger.info("接收到客户端心跳包……");
                return;
            }

            //得到服务名
            String interfaceName = msg.getInterfaceName();
            //得到服务端相应服务实现类
            Object service = serviceProvider.getServiceImp(interfaceName);
            // 反射调用方法
            RpcResponse rpcResponse = requestHandler.handle(msg, service);
            //设置响应id和请求id一致
            rpcResponse.setResponseId(msg.getRequestId());
            logger.info("服务器发回消息{}",rpcResponse);
            if (ctx.channel().isActive()&&ctx.channel().isWritable()) {
                ChannelFuture future = ctx.writeAndFlush(rpcResponse);
            }
            else {
                logger.error("channel不活跃或不可写");
            }

            //设置心跳机制 不能关闭通道
            //添加一个监听器到channelfuture来检测是否所有的数据包都发出，然后关闭通道
//            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE){
                logger.info("长时间未收到心跳包，断开连接……");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
