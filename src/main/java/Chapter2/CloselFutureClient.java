package Chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/*
* netty channelFutrue 客户端
* */
@Slf4j
public class CloselFutureClient {
    public static void main(String[] args) throws InterruptedException {
        String i = "123";
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override //在连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1.连接到服务器
                //异步非阻塞,main 发起了调用，真正执行connect 是 nio线程
                .connect(new InetSocketAddress("localhost", 8080));
        Channel channel = channelFuture.sync().channel();
        log.debug("{}",channel);
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while(true){
                String line = scanner.nextLine();
                if("q".equals(line)){
                    channel.close(); // close 异步操作 1s之后
//                    log.debug("处理关闭之后的操作");// 不能再这里配置
                    break;
                }
                channel.writeAndFlush(line);
            }
        },"input").start();

        //获取 ClosedFuture 对象, 1)同步处理关闭 2)异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
//        log.debug("waiting close...");
//        closeFuture.sync();
//        log.debug("处理关闭之后的操作");
        closeFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            log.debug("处理关闭之后的操作");
            group.shutdownGracefully();
        });
    }


    @Data
    @AllArgsConstructor
    static class Student{
        private String name;
    }
}
