package pre.netty.learn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import pre.netty.learn.handler.exmp1.EchoServerHandler;
import pre.netty.learn.handler.exmp2.ServerHandler;
import pre.netty.learn.handler.exmp4.TimeEncoder;
import pre.netty.learn.handler.exmp4.TimeServerHandler;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/4/12 - 10:09 上午
 **/
public class ExampleServer {

    private static final int PORT=Integer.parseInt(System.getProperty("port","8007"));


    public static void main(String[] args) {
        exampleFour();
    }

    private static void exampleOne(){
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new EchoServerHandler());
            }
        };
        basicBootstrap(channelInitializer);
    }

    private static void exampleTwo(){
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new ServerHandler());
            }
        };
        basicBootstrap(channelInitializer);
    }

    private static void exampleThree(){
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new TimeServerHandler());
            }
        };
        basicBootstrap(channelInitializer);
    }

    private static void exampleFour(){
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addFirst(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new TimeEncoder());
                pipeline.addLast(new TimeServerHandler());
            }
        };
        basicBootstrap(channelInitializer);
    }

    private static void basicBootstrap(ChannelInitializer<SocketChannel> channelChannelInitializer){
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    /**
                     * handler:启动时执行一次
                     * childHandler:每来一次请求执行一次
                     */
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelChannelInitializer);
            ChannelFuture result = bootstrap.bind(PORT).sync();
            result.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
