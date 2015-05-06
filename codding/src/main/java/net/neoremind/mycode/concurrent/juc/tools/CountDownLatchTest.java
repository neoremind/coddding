package net.neoremind.mycode.concurrent.juc.tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * ClassName: CountDownLatchTest <br/>
 * Function: CountDownLatch类是一个同步计数器,构造时传入int参数,该参数就是计数器的初始值， 每调用一次countDown()方法，计数器减1,计数器大于0
 * 时，await()方法会阻塞程序继续执行。CountDownLatch可以看作是一个倒计数的锁存器，当计数减至0时触发特定的事件。利用这种特性，可以让主线程等待子线程的结束。
 * <p/>
 * A synchronization aid that allows one or more threads to wait until a set of operations being performed in other
 * threads completes.
 * 
 * @author Zhang Xu
 */
public class CountDownLatchTest {

    /**
     * 可以想象1个选手拿着接力棒，其他N个选手依次从他手里拿走接力棒后，这个选手再开始跑
     * 
     * @throws InterruptedException
     */
    @Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("主线程开始...");
        new Thread(new Runner(latch)).start();
        System.out.println("等待Runner子线程完成中...");
        latch.await();
        latch.await(5, TimeUnit.MINUTES); // 已经countdown的再await就没用了
        System.out.println("主线程结束");
    }

    class Runner implements Runnable {

        private CountDownLatch latch;

        public Runner(CountDownLatch latch) {
            super();
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Runner子线程开始跑...");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println("Runner子线程跑完！");
        }

    }

}
