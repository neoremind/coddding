package net.neoremind.mycode.concurrent.lbq;

import java.util.concurrent.BlockingQueue;

/**
 * @author zhangxu
 */
public class Consumer implements Runnable {

    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
