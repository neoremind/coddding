package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AQS是基于CLH lock queue，那么什么是CLH lock queue，说复杂很复杂说简单也简单， 所谓大道至简：
 * <p>
 * CLH lock queue其实就是一个FIFO的队列，队列中的每个结点（线程）只要等待其前继释放锁就可以了。
 * <p>
 * AbstractQueuedSynchronizer是通过一个内部类Node来实现CLH lock queue的一个变种，但基本原理是类似的。
 * <p>
 * 在介绍Node类之前，我们来介绍下Spin Lock,通常就是用CLH lock queue来实现自旋锁，所谓自旋锁简单来说就是线程通过循环来等待而不是睡眠。
 * <p>
 * 上面的代码中线程巧妙的通过ThreadLocal保存了当前结点和前继结点的引用，自旋就是lock中的while循环。
 * 总的来说这种实现的好处是保证所有等待线程的公平竞争，而且没有竞争同一个变量，因为每个线程只要等待自己的前继释放就好了。 而自旋的好处是线程不需要睡眠和唤醒，减小了系统调用的开销。
 *
 * @author zhangxu
 * @see http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-clh-and-spin-lock.html
 */
class ClhSpinLock {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public ClhSpinLock() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        final Node node = this.node.get();
        node.locked = true;
        // 一个CAS操作即可将当前线程对应的节点加入到队列中，
        // 并且同时获得了前继节点的引用，然后就是等待前继释放锁
        Node pred = this.tail.getAndSet(node);
        this.prev.set(pred);
        while (pred.locked) {// 进入自旋
        }
    }

    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        this.node.set(this.prev.get());
    }

    private static class Node {
        private volatile boolean locked;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(1);
        ClhSpinLock lock = new ClhSpinLock();

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
                lock.lock();
                try {
                    System.out.printf("Thread %s is performing work.%n", name);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.printf("Thread %s has finished working.%n", name);
                } finally {
                    lock.unlock();
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

