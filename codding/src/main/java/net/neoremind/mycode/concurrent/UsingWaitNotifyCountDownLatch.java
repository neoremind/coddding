package net.neoremind.mycode.concurrent;

import java.util.Random;

/**
 * 自己实现的一个用wait/notify做的闭锁，和{@link java.util.concurrent.CountDownLatch}功能类似
 *
 * @author zhangxu
 * @see http://www.javamex.com/tutorials/synchronization_wait_notify_4.shtml
 */
public class UsingWaitNotifyCountDownLatch {

    // volatile保证可见性
    private volatile int count;

    public UsingWaitNotifyCountDownLatch(int noThreads) {
        this.count = noThreads;
    }

    /**
     * 下面是javadoc里面的标准模板实现
     * <pre>
     *     synchronized (obj) {
     *         while (&lt;condition does not hold&gt;)
     *             obj.wait();
     *         ... // Perform action appropriate to condition
     *     }
     * </pre>
     */
    public synchronized void awaitZero() throws InterruptedException {
        while (count > 0) {
            wait();  // 阻塞线程挂起，随时有人完成就notify，判断count>0继续挂起，相比CDL性能略低
        }
    }

    public synchronized void countDown() {
        if (--count <= 0) {
            notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final UsingWaitNotifyCountDownLatch startGate = new UsingWaitNotifyCountDownLatch(1);
        final UsingWaitNotifyCountDownLatch endGate = new UsingWaitNotifyCountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " waiting to begin");
                        startGate.awaitZero();
                        System.out.println(Thread.currentThread().getName() + " starts");
                        try {
                            Thread.sleep(1000 * (new Random()).nextInt(8));
                        } finally {
                            endGate.countDown();
                        }
                        System.out.println(Thread.currentThread().getName() + " ends");
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        System.out.println("Ready to start");
        startGate.countDown();
        endGate.awaitZero();
        long end = System.nanoTime();
        System.out.println("End using " + (end - start) + "ns");
    }

}
