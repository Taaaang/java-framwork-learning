package pre.netty.learn.handler.exmp4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/12 - 5:52 下午
 **/
public class TimeEncoder extends MessageToByteEncoder<TimePOJO> {


    @Override
    protected void encode(ChannelHandlerContext ctx, TimePOJO msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getTime());
    }
}
