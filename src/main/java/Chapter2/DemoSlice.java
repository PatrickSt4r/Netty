package Chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static Chapter2.DemoByteBuf.log;

/*
* 切片演示
* */
public class DemoSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});
        log(buf);

        //在切片的过程中并没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        f1.retain();
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        log(f1);
        log(f2);

        System.out.println("释放原有 byteBuf 内存");
        buf.release();
        log(f1);

        f1.release();
        f2.release();
//        f1.setByte(0,'b');
//        log(f1);
//        log(buf);
    }
}
