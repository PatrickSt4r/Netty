package Chapter1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static Utils.ByteBufferUtil.debugAll;

/*
* 字符串和byteffer互转
* */
public class DemoByteBufferString {
    public static void main(String[] args) {
        //1.将字符串转化为ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());
        debugAll(buffer);

        //2.charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);

        //3.wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        //4.转为字符串
        buffer.flip();
        String str1 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str1);
    }
}
