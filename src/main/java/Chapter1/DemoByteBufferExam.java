package Chapter1;


import java.nio.ByteBuffer;

import static Utils.ByteBufferUtil.debugAll;

/*
* 黏包半包
* */
public class DemoByteBufferExam {
    public static void main(String[] args) {
        /*
        * 网络上有多条数据发送给服务器，数据之间用\n进行分割
        * 但由于某种原因这些数据在接受时，被进行了重新组合，例如原始数据有3条为
        * Hello,world \n
        * I'm zhangsan \n
        * How are you? \n
        * 变成了下面两个Bytebuffer(黏包，半包)
        * Hello,world\nI'm zhangsan\nHo
        * w are you?\n
        * 现在要求你编写程序将错乱的数据恢复成原始的按\n分割的数据
        * */
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    public static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            //找到一条完整的消息
            if(source.get(i) == '\n'){
                int length = i + 1 - source.position();
                //把这条完整消息存入新的byteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                //从source读，向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }

        source.compact();
    }
}
