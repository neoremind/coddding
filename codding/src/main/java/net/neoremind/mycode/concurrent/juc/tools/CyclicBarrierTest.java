package net.neoremind.mycode.concurrent.juc.tools;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: CyclicBarrierTest <br/>
 * Function:
 * 实现让一组线程等待至某个状态之后再全部同时执行。叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。我们暂且把这个状态就叫做barrier，当调用await()方法之后，线程就处于barrier了
 * 。一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)
 * <p/>
 * A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point.
 * CountDownLatch 是计数器, 线程完成一个就记一个, 就像 报数一样, 只不过是递减的. <br/>
 * 而CyclicBarrier更像一个水闸, 线程执行就想水流, 在水闸处都会堵住, 等到水满(线程到齐)了, 才开始泄流.*
 * 
 * @author Zhang Xu
 */
public class CyclicBarrierTest {

    /**
     * 可以想象三个选手脚都绑到了一起，只有三个人都说ready准备好后才可以一起撤掉脚带，开始各自的奔跑
     * 
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        // 如果将参数改为4，但是下面只加入了3个选手，这永远等待下去
        // Waits until all parties have invoked await on this barrier.
        CyclicBarrier barrier = new CyclicBarrier(3, new TotalTask());

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Thread(new Runner(barrier, "1号选手")));
        executor.submit(new Thread(new Runner(barrier, "2号选手")));
        executor.submit(new Thread(new Runner(barrier, "3号选手")));

        executor.shutdown();
    }

}

class Runner implements Runnable {

    private CyclicBarrier barrier;

    private String name;

    public Runner(CyclicBarrier barrier, String name) {
        super();
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000 * (new Random()).nextInt(8));
            System.out.println(name + " 准备好了...");
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(name + " 起跑！");
    }
}

/**
 * 主任务：汇总任务
 */
class TotalTask implements Runnable {

    public void run() {
        // 等到所人都准备好后，再开始
        System.out.println("=======================================");
        System.out.println("开始一起跑啦！");
    }
}
