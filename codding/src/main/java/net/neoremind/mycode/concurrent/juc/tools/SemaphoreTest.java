package net.neoremind.mycode.concurrent.juc.tools;

import java.util.concurrent.Semaphore;

/**
 * ClassName: SemaphoreTest <br/>
 * Function: Semaphore可以控同时访问的线程个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 * <p/>
 * a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then
 * takes it. Each release() adds a permit,
 * 
 * @author Zhang Xu
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        int N = 10; // 工人数
        Semaphore semaphore = new Semaphore(5); // 机器数目
        for (int i = 0; i < N; i++)
            new Worker(i, semaphore).start();
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
