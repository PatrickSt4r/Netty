package Chapter1.NIO;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static Chapter1.DemoByteBufferExam.split;
import static Utils.ByteBufferUtil.debugRead;

/*
* 非阻塞模式服务器
* */
@Slf4j
public class DemoNIOServerSelector {
    public static void main(String[] args) throws IOException {
        //1.创建selector，管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); //切换成非阻塞模式

        //2.建立selector和channel的联系（注册）
        //SelectionKey就是将来事件发生后，通过它可以知道事件和哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //key 只关注 accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}",sscKey);

        ssc.bind(new InetSocketAddress(8080));

        while(true){
            //3. select 方法,没有事件发生，线程阻塞，有事件，线程才会恢复运行
            //select 在事件未处理时，它不会阻塞，事件发生后，要么取消，要么处理，不能置之不理
            selector.select();

            //4.处理事件,selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator(); //accept , read
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                //处理key时，要从selectedKyes集合中删除，否则下次处理就会有问题
                iter.remove();
                log.debug("key:{}",key);
                //5.区分事件类型
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}",sc);
                }else if(key.isReadable()){
                    try {
                        SocketChannel channel =(SocketChannel) key.channel(); //拿到触发事件的channel
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        int read = channel.read(buffer); //如果是正常断开，read 方法的返回值是-1
                        if(read == -1){
                            key.cancel();
                        }else{
                            split(buffer);
//                            buffer.flip();
//                            System.out.println(Charset.defaultCharset().decode(buffer).toString());
//                            debugRead(buffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); //因为客户端断开了，因此需要将key 取消（从selector 的key集合中真正删除key）

                    }
                }

                //key.cannel();

            }
        }

    }
}
