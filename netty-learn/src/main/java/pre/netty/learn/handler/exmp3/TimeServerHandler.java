package pre.netty.learn.handler.exmp3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * 范例三：在连接处于激活状态后，返回客户端当前时间戳，并关闭连接
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/12 - 3:50 下午
 **/
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf time = ctx.alloc().buffer(4);
        long timestamp = (System.currentTimeMillis() / 1000);
        time.writeInt((int) timestamp);
        ChannelFuture channelFuture = ctx.writeAndFlush(time);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if(channelFuture==future){
                ctx.close();
            }
        });
    }


    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
