package Chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;


/*
* netty hello world server端
* */
public class HelloServer {
    public static void main(String[] args) {
        //1.启动器 负责组装 netty组件，启动服务器
        new ServerBootstrap()
                //2. BossEvnentLoop, WorkerEventLoop(selector,thread),group 组
                .group(new NioEventLoopGroup())
                //3.选择服务器的 ServerSocketChannel实现
                .channel(NioServerSocketChannel.class) //OIO BIO(block io)
                //4.boss 负责处理连接worker（child） 负责处理读写，决定了 worker(child) 能执行哪些操作（handler）
                .childHandler(
                    //5.channel 代表和客户端进行数据读写的通道 Initializer 初始化，负责添加别的handler
                    new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        //6.添加具体handler
                        channel.pipeline().addLast(new StringDecoder()); //将 ByteBuf 转换为字符串
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){ //自定义handler
                            @Override //读事件
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换号的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                })
                //7.绑定监听端口
                .bind(8080);
    }
}
