package net.neoremind.mycode.juc.collections.lbq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhangxu
 */
public class BlockingQueueExample {

    public static void main(String[] args) throws Exception {
        BlockingQueue queue = new LinkedBlockingQueue<>(1024);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();

        Thread.sleep(4000);
    }
}
