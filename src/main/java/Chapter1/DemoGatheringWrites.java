package Chapter1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static Utils.ByteBufferUtil.debugAll;

/*
* 多个buffer写入一个文件
* */
public class DemoGatheringWrites {
    public static void main(String[] args) {
            ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
            ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
            ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");
        try (FileChannel channel = new RandomAccessFile("data3.txt","rw").getChannel()){
            channel.write(new ByteBuffer[]{b1,b2,b3});
        }catch (IOException e){

        }

    }
}