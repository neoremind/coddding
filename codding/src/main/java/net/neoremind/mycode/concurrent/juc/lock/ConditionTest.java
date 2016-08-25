package net.neoremind.mycode.concurrent.juc.lock;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link ConditionTest2}的变种。
 * <p>
 * Console prints out:
 * <pre>
 * A produced by producer.===
 * A consumed by consumer.
 * E produced by producer.===
 * E consumed by consumer.
 * F produced by producer.===
 * F consumed by consumer.
 * ....
 * </pre>
 * <p>
 * 26个字母。
 * 启动26个生产者thread，26个消费者thread。
 * 首先不一定那个生产者先执行，因为线程调度是竞争式的，所以看到A后面不一定跟着B，而是E。比如E先执行了，此时打印日志。
 * 然后signal消费者，由于之前消费者一直在await，现在终于被唤醒了，消费者去共享内存Shared里面取char，打印日志。
 * 然后再signal回给生产者，生产下一个字母。
 * <p>
 * 所以看到的现象是，字母肯定是成对出现的。单是对和对之间的顺序不一定。
 */
public class ConditionTest {

    public static void main(String[] args) {
        Shared s = new Shared();

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            final char c = ch;
            new Thread() {
                @Override
                public void run() {
                    s.setSharedChar(c);
                }
            }.start();
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            new Thread() {
                @Override
                public void run() {
                    s.getSharedChar();
                }
            }.start();
        }
    }
}

class Shared {

    final Random random = new Random();

    // Fields c and available are volatile so that writes to them are visible to
    // the various threads. Fields lock and condition are final so that they're
    // initial values are visible to the various threads. (The Java memory model
    // promises that, after a final field has been initialized, any thread will
    // see the same [correct] value.)

    private volatile char c;
    private volatile boolean available;
    private final Lock lock;
    private final Condition condition;
    private final Condition condition2;

    Shared() {
        c = '\u0000';
        available = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
        condition2 = lock.newCondition();
    }

    char getSharedChar() {
        lock.lock();
        try {
            while (!available) {
                try {
                    condition.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            available = false;
            //sleep(random.nextInt(10));
            System.out.println(c + " consumed by consumer.");
            condition2.signal();
        } finally {
            lock.unlock();
            return c;
        }
    }

    void setSharedChar(char c) {
        lock.lock();
        try {
            while (available) {
                try {
                    condition2.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            this.c = c;
            available = true;
            //sleep(random.nextInt(10));
            System.out.println(c + " produced by producer.===");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void sleep(int timeInMs) {
        try {
            Thread.sleep(timeInMs * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}