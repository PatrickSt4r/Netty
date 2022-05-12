package Chapter1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static Utils.ByteBufferUtil.debugAll;

/*
* byteBuffer读取演示
* */
@Slf4j
public class DemoByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0x61);
        debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63,0x64});
        debugAll(buffer);
        //默认模式get的时候buffer的position是4，所以get的为0
        System.out.println(buffer.get());
        buffer.flip(); //切换为读模式
        //切换为读模式之后position重置为0，limit置为之前position位置，这时候第一个get的是97(61存储的是16进制，转换后打印的是十进制)
        System.out.println(buffer.get());
        //上一个get获取了下标为0的61，compact之后前面读的就清除，未读的部分往前移，position变为之前limit的位置，limit为正常限制容量的大小
        buffer.compact();
        debugAll(buffer);
        //切换为写模式，继续写入则会从position之后的位置(4)开始继续写入
        buffer.put(new byte[]{0x65,0x6f});
        debugAll(buffer);
    }
}
