package net.neoremind.mycode.concurrent.juc.mythreadpool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自己依据理解根据{@link java.util.concurrent.ThreadPoolExecutor}写的一个fixedThreadNumber简单的线程池
 *
 * @author zhangxu
 */
public class MyThreadPool {

    private int maxWorkerNum;

    private BlockingQueue<MyFuture> queue = new LinkedBlockingDeque<>();

    private AtomicInteger workCount = new AtomicInteger(0);

    private Set<Worker> workers = new HashSet<>();

    public MyThreadPool(int maxWorkerNum) {
        this.maxWorkerNum = maxWorkerNum;
    }

    public <T> MyFuture<T> submit(Callable<T> c) {
        if (workCount.incrementAndGet() < maxWorkerNum) {
            addWorker();
        }
        MyFuture<T> f = new MyFuture(c);
        queue.offer(f);
        return f;
    }

    public void shutdown() {
        for (Worker worker : workers) {
            worker.stop();
        }
    }

    private void addWorker() {
        Worker w = new Worker();
        workers.add(w);
        w.t.start();
    }

    public void monitorInfo() {
        System.out.println("===============================");
        System.out.println("maxWorkerNum=" + maxWorkerNum);
        System.out.println("workCount=" + workCount.get());
        System.out.println("runningWorkerNum=" + workers.stream().filter(worker -> worker.isRunning == true).count());
    }

    class Worker implements Runnable {

        volatile boolean isRunning;

        volatile boolean stop;

        Thread t;

        public Worker() {
            this.t = new Thread(this);
        }

        @Override
        public void run() {
            try {
                MyFuture f;
                while ((f = queue.take()) != null) {
                    isRunning = true;
                    f.run();
                    isRunning = false;
                }
            } catch (InterruptedException e) {
                if (stop) {
                    System.out.println("Destroying... " + t.getName());
                } else {
                    // recover??? whether that is the right to handle???
                    Thread.interrupted();
                    run();
                }
            }
        }

        void stop() {
            stop = true;
            t.interrupt();
        }
    }

}
