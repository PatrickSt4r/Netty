package Chapter2;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/*
* 循环对象 EventLoopGroup
* */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2); //io 事件，普通任务，定时任务
//        EventLoopGroup group = new DefaultEventLoop(); //普通任务，定时任务
        //2.获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //3. 执行普通任务
//        group.next().submit(()->{
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.debug("ok");
//        });

        //4.执行定时任务
        group.next().scheduleAtFixedRate(()->{
            log.debug("ok");
        },0,1, TimeUnit.SECONDS);
        log.debug("main ok");
    }
}