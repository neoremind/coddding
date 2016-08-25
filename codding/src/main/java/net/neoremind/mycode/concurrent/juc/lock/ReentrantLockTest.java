package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http://www.javaworld.com/article/2078848/java-concurrency/java-concurrency-java-101-the-next-generation-java
 * -concurrency-without-the-pain-part-2.html?page=2
 */
public class ReentrantLockTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final ReentrantLock rl = new ReentrantLock();

        class Worker implements Runnable {
            private String name;

            Worker(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                rl.lock();
                try {
                    if (rl.isHeldByCurrentThread()) {
                        System.out.printf("Thread %s has entered its critical section.%n", name);
                    }
                    System.out.printf("Thread %s is performing work for 2 seconds.%n", name);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.printf("Thread %s has finished working.%n", name);
                } finally {
                    rl.unlock();
                }
            }
        }

        executor.execute(new Worker("A"));
        executor.execute(new Worker("B"));
        executor.execute(new Worker("C"));
        executor.execute(new Worker("D"));

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        executor.shutdownNow();
    }
}
