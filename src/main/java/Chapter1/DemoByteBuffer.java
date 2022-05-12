package Chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
* byteBuffer演示
* channel 和 buffer演示
* */
@Slf4j
public class DemoByteBuffer {
    public static void main(String[] args) {
        //FileChannel
        //1.输出输出流 2.RandomAccessFile
        try(FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while(true){
                // 从 channel 读取数据，向buffer写入
                int readlen = channel.read(buffer);
                log.debug("读取到的字节 {}",readlen);
                if(readlen == -1){ //没有内容了
                    break;
                }
                //打印buffer的内容
                buffer.flip(); //切换到读模式
                while(buffer.hasRemaining()){ //是否还有剩余未读的数据
                    byte b = buffer.get();
                    log.debug("实际字节 {}",(char) b);
                }
                //切换为写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
