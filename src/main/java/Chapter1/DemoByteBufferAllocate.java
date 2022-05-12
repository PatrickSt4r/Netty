package Chapter1;

import java.nio.ByteBuffer;

public class DemoByteBufferAllocate {

    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
        /*
        *   class java.nio.HeapByteBuffer  -java 堆内存，读写效率较低，收到GC影响
            class java.nio.DirectByteBuffer -直接内存，读写效率高(少一次拷贝)，不会受GC影响，分配的效率低
        * */
    }
}
