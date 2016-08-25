package net.neoremind.mycode.concurrent.juc.lock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自己实现的阻塞有界数组队列。数组靠指针移动处理。
 * <p>
 * 从日志中可以看出Condition确实是可以让出lock的，其他线程可以争夺执行。
 * <p>
 * 多线程put，多线程take，是按照顺序输出的。
 */
public class BoundedBuffer {

    private static final Logger logger = LoggerFactory.getLogger(BoundedBuffer.class);

    final Lock lock = new ReentrantLock();

    final Condition notFull = lock.newCondition();

    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[2]; // 阻塞队列

    int putptr, takeptr, count;
    
    private void log(String info) {
        logger.info(Thread.currentThread().getName() + " - " + info);
    }

    public void put(Object x) throws InterruptedException {
        log(x + ",执行put");
        lock.lock();
        log(x + ",put lock.lock()");
        try {
            while (count == items.length) { // 如果队列满了，notFull就一直等待
                log(x + ",put notFull.await() 队列满了");
                notFull.await(); // 调用await的意思取反，及not notFull -> Full
            }
            items[putptr] = x; // 终于可以插入队列
            if (++putptr == items.length) {
                putptr = 0; // 如果下标到达数组边界，循环下标置为0
            }
            ++count;
            log(x + ",put成功 notEmpty.signal() 周知队列不为空了");
            notEmpty.signal(); // 唤醒notEmpty
        } finally {
            log(x + ",put lock.unlock()");
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        log("执行take");
        lock.lock();
        Object x = null;
        log("take lock.lock()");
        try {
            while (count == 0) {
                log("take notEmpty.await() 队列为空等等");
                notEmpty.await();
            }
            x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            log(x + ",take成功 notFull.signal() 周知队列有剩余空间了");
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
            log(x + ",take lock.unlock()");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final BoundedBuffer bb = new BoundedBuffer();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (char i = 'A'; i < 'F'; i++) {
            final char t = i;
            executor.execute(() -> {
                try {
                    bb.put(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        List<Character> res = new LinkedList<>();
        for (char i = 'A'; i < 'F'; i++) {
            executor.execute(() -> {
                try {
                    char c = (char) bb.take();
                    res.add(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        logger.info(res.toString());
        executor.shutdownNow();
    }
}