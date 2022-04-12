package pre.netty.learn.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * 范例二：将接收到的信息打印在控制台，并将接收到的内容返回给客户端
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/12 - 11:20 上午
 **/
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("received msg:"+msg);
        ctx.writeAndFlush(msg);
    }
}
