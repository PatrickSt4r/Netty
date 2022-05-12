package Chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/*
* netty channelFutrue 客户端
* */
@Slf4j
public class ChannelFutureClient {
    public static void main(String[] args) throws InterruptedException {
        //2.带有 Futrue，Promise 的类型都是和 异步方法配套使用，用来正确处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override //在连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1.连接到服务器
                //异步非阻塞,main 发起了调用，真正执行connect 是 nio线程
                .connect(new InetSocketAddress("localhost", 8080));
        //2.1使用sync方法同步处理结果
//        channelFuture.sync();//阻塞住当前线程，直到nio线程建立完毕
//        //无阻塞向下执行获取 channel
//        Channel channel = channelFuture.channel();
//        log.debug("{}",channel);
//        channel.writeAndFlush("hello,world");

        //2.2 使用addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override //在 nio线程 连接建立好之后，会调用
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                log.debug("{}",channel);
                channel.writeAndFlush("hello,world");
            }
        });
    }
}
