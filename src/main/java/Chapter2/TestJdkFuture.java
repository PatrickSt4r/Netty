package Chapter2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.*;

/*
* jdk Future演示
* */
@Slf4j
public class TestJdkFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.线程池
        ExecutorService service = newFixedThreadPool(2);

        //2.提交任务
        Future<Integer> future = service.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 50;
        });


        //3.主线程通过future 来获取结果
        log.debug("等待结果");
        log.debug("结果是 {}",future.get());

    }
}
