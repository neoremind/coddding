package net.neoremind.mycode.concurrent.juc.mythreadpool;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.junit.Test;

/**
 * 测试自己的线程池
 *
 * @author zhangxu
 */
public class App {

    @Test
    public void test() throws InterruptedException {
        final MyThreadPool myThreadPool = new MyThreadPool(5);

        // 打印线程池的一些状态，例如：
        // maxWorkerNum=5
        // workCount=4
        // runningWorkerNum=0
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myThreadPool.monitorInfo();
            }
        }, 200, 2000);

        Thread.sleep(1000);

        Callable<String> task = () -> {
            Thread.sleep(4000);
            System.out.println(Thread.currentThread().getName() + " is running");
            return "abc";
        };
        MyFuture<String> f = myThreadPool.submit(task);
        System.out.println(f.get());
        myThreadPool.submit(task);
        myThreadPool.submit(task);
        myThreadPool.submit(task);

        Thread.sleep(20000);
        myThreadPool.shutdown();

        Thread.sleep(5000);
    }

}
