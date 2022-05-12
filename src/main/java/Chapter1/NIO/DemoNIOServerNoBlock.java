package Chapter1.NIO;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static Utils.ByteBufferUtil.debugRead;

/*
* Selector管理channel
* */
@Slf4j
public class DemoNIOServerNoBlock {
    public static void main(String[] args) throws IOException {
        //使用nio 来理解阻塞模式
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); //切换成非阻塞模式
        //2.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        //3.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while(true){
            //4.accept 建立与客户端连接,SocketChannel 用来与客户端之间通信
            SocketChannel sc = ssc.accept();    //非阻塞 ,线程还会继续运行,如果没有连接建立,但sc返回的是null
            if(sc != null){
                log.debug("connectted.. {}" + sc);
                sc.configureBlocking(false);//非阻塞模式
                channels.add(sc);
            }
            //5.接收客户端发送的数据
            for(SocketChannel channel : channels){
                //5.读取客户端发送过来的数据
                int read = channel.read(buffer);//非阻塞，线程仍会继续运行,如果没有读到数据，read 返回0
                if(read > 0){
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("finish read... {}",channel);
                }
            }
        }

    }
}
