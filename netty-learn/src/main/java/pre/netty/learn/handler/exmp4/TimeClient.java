package pre.netty.learn.handler.exmp4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/12 - 4:42 下午
 **/
public class TimeClient {
    private static final int PORT=Integer.parseInt(System.getProperty("port","8007"));
    private static final String HOST=System.getProperty("host","127.0.0.1");

    public static void main(String[] args) {
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TimeDecoder());
                            pipeline.addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture sync = client.connect(HOST, PORT).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

}
