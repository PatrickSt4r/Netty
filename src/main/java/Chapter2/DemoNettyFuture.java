package Chapter2;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/*
* netty 线程池 Future
 * */
@Slf4j
public class DemoNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();

        Future<Integer> future = eventLoop.submit(() -> {
            log.debug("进行计算");
            Thread.sleep(1000);
            return 70;
        });

//        log.debug("等待结果");
//        log.debug("结果是 {}",future.get());
        future.addListener(fu -> {
            log.debug("接收结果:{}",fu.getNow());
        });

    }
}
