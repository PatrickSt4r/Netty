package Chapter1.NIO;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static Chapter1.DemoByteBufferExam.split;

/*
* 消息长度过大
* */
@Slf4j
public class DemoNIOServerMessage {
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
                    //将一个byteBuffer 作为附件关联到selector上
                    ByteBuffer buffer = ByteBuffer.allocate(16); //attachment 附件
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}",sc);
                }else if(key.isReadable()){
                    try {
                        SocketChannel channel =(SocketChannel) key.channel(); //拿到触发事件的channel

                        //获取 selectionKey上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer); //如果是正常断开，read 方法的返回值是-1
                        if(read == -1){
                            key.cancel();
                        }else{
                            split(buffer);
                            if(buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() *2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); //因为客户端断开了，因此需要将key 取消（从selector 的key集合中真正删除key）

                    }
                }

            }
        }

    }
}
