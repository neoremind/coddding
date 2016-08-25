package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://www.javaworld.com/article/2078848/java-concurrency/java-concurrency-java-101-the-next-generation-java
 * -concurrency-without-the-pain-part-2.html?page=2
 * <p>
 * Console prints out:
 * <pre>
 * A produced by producer.
 * A consumed by consumer.
 * B produced by producer.
 * B consumed by consumer.
 * C produced by producer.
 * C consumed by consumer.
 * ....
 * </pre>
 * <p>
 * 启动2个线程，一个是生成者，在Shared共享内存中设置char值，一个是消费者去get char值输出。
 * 要求每个单词一个个按生产者设置的顺序依次消费，消费一个才允许生产下一个。因此字母是成对出现的。
 * <p>
 * 重要要理解Condition的用法，一个lock可以生成多个Condition，lock只是类似synchronized关键词在代码上
 * 加锁，线程排队执行代码。Condition做线程间的通信，再没有生产者生产的时候，消费者应该等待await，直到
 * 生产者signal给一个信号，那么消费者才消费，获取这个字母返回，此时在signal给生产者一个信号，可以继续生产了。
 * 直到我打印完毕了，进入下一次循环再循环这个过程唤醒你。
 */
public class ConditionTest2 {

    public static void main(String[] args) {
        Shared2 s = new Shared2();
        new Thread() {
            @Override
            public void run() {
                for (char ch = 'A'; ch <= 'Z'; ch++) {
                    s.setSharedChar(ch);
                    System.out.println(ch + " produced by producer.");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                char ch;
                do {
                    ch = s.getSharedChar();
                    System.out.println(ch + " consumed by consumer.");
                }
                while (ch != 'Z');
            }
        }.start();
    }

}

class Shared2 {

    // Fields c and available are volatile so that writes to them are visible to
    // the various threads. Fields lock and condition are final so that they're
    // initial values are visible to the various threads. (The Java memory model
    // promises that, after a final field has been initialized, any thread will
    // see the same [correct] value.)

    private volatile char c;
    private volatile boolean available;
    private final Lock lock;
    private final Condition condition;

    Shared2() {
        c = '\u0000';
        available = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
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
            condition.signal();
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
                    condition.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            this.c = c;
            available = true;
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



