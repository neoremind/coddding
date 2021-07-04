package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO cannot stop?
public class PrintABAB {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();
        Counter c1 = new Counter(lock, cond, "A", 0);
        Counter c2 = new Counter(lock, cond, "B", 1);
        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class Counter implements Runnable {

        private static int count = 0;

        private final Lock lock;

        private final Condition cond;

        private final String str;

        private final int threadId;

        public Counter(Lock lock, Condition cond, String str, int threadId) {
            this.lock = lock;
            this.cond = cond;
            this.str = str;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count % 2 != threadId) {
                        if (count > 20) {
                            break;
                        }
                        //System.out.println(Thread.currentThread().getName() + " wait " + count);
                        cond.await();
                    }
                    if (count > 20) {
                        break;
                    }
                    count++;
                    System.out.println(str);
                    cond.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
