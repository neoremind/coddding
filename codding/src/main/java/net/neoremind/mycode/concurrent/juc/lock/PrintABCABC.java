package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABCABC {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();
        Thread t1 = new Thread(new PrintThread2(lock, cond, 0));
        Thread t2 = new Thread(new PrintThread2(lock, cond, 1));
        Thread t3 = new Thread(new PrintThread2(lock, cond, 2));
        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    static class PrintThread2 implements Runnable {

        // no need to be volatile since guard by lock
        private static int count = 0;

        private final Lock lock;

        private final Condition cond;

        private int threadNo;

        public PrintThread2(Lock lock, Condition cond, int threadNo) {
            this.lock = lock;
            this.cond = cond;
            this.threadNo = threadNo;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    while (count % 3 != this.threadNo) {
                        if (count >= 101) {
                            break;
                        }
                        try {
                            cond.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (count >= 101) {
                        break;
                    }
                    System.out.println("thread-" + this.threadNo + ":" + count);
                    count++;

                    cond.signalAll();

                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
