package net.neoremind.mycode.concurrent.juc.lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 摘自http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/LockSupport.html
 */
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {// ignore interrupts while waiting
                wasInterrupted = true;
            }

        }
        waiters.remove();
        if (wasInterrupted) {// reassert interrupt status on exit
            current.interrupt();
        }

    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);
        FIFOMutex mutex = new FIFOMutex();

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
                mutex.lock();
                try {
                    System.out.printf("Thread %s is performing work.%n", name);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.printf("Thread %s has finished working.%n", name);
                } finally {
                    mutex.unlock();
                }
            }
        }

        // 做一个CDL，让A1-A5充分竞争，谁都有可能先获得锁而执行。
        AsyncThread.run(new Worker("A1"));
        AsyncThread.run(new Worker("A2"));
        AsyncThread.run(new Worker("A3"));
        AsyncThread.run(new Worker("A4"));
        AsyncThread.run(new Worker("A5"));
        AsyncThread.run(new Worker("A6"));

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
