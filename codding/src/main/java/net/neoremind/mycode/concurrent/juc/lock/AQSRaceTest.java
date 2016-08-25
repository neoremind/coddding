package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 验证AQS是一个基于CLH的FIFO的队列
 *
 * @author zhangxu
 */
public class AQSRaceTest {

    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock rl = new ReentrantLock();
        CountDownLatch cdl = new CountDownLatch(1);

        class Worker implements Runnable {
            private String name;

            Worker(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rl.lock();
                try {
                    if (rl.isHeldByCurrentThread()) {
                        System.out.printf("Thread %s has entered its critical section.%n", name);
                    }
                    System.out.printf("Thread %s is performing work for 2 seconds.%n", name);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.printf("Thread %s has finished working.%n", name);
                } finally {
                    rl.unlock();
                }
            }
        }

        // 做一个CDL，让A1-A5充分竞争，谁都有可能先获得锁而执行。
        // 如果这句改为ReentrantLock rl = new ReentrantLock(true);构造公平锁，则可以保障顺序
        AsyncThread.run(new Worker("A1"));
        AsyncThread.run(new Worker("A2"));
        AsyncThread.run(new Worker("A3"));
        AsyncThread.run(new Worker("A4"));
        AsyncThread.run(new Worker("A5"));

        cdl.countDown();

        // 下面的顺序一般是有保障的，因为AQS是内部的队列是按照从插入的顺序，从头找下一个Thread去唤醒的
        AsyncThread.run(new Worker("B"));
        AsyncThread.run(new Worker("C"));
        AsyncThread.run(new Worker("D"));
        AsyncThread.run(new Worker("E"));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
