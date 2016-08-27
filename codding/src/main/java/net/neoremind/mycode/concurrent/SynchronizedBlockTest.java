package net.neoremind.mycode.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类来证明像Vector这种通过把所有方法都改为synchronized的方式的对象，
 * 当调用其中一个方法时候，其他所有方法都会阻塞。因为多线程下，各个线程是在同一个对象上争用锁，产生race condition。
 * <p>
 * 例如，A/B/C三个线程同时开启，doA/doB/doC都是某个类的同步方法，可以看出虽然同时启动，但是确实按照锁竞争，串行执行的。
 * <pre>
 * 2016-08-27 17:20:50,869 -Ready to start
 * 2016-08-27 17:20:50,874 -Thread-B begin to invoke doB
 * 2016-08-27 17:20:50,874 -Thread-A begin to invoke doA
 * 2016-08-27 17:20:50,874 -Thread-C begin to invoke doC
 * 2016-08-27 17:20:50,874 -doB in 5000ms
 * 2016-08-27 17:20:55,882 -Thread-B end to invoke doB
 * 2016-08-27 17:20:55,882 -doC in 11000ms
 * 2016-08-27 17:21:06,887 -Thread-C end to invoke doC
 * 2016-08-27 17:21:06,887 -doA in 12000ms
 * 2016-08-27 17:21:18,889 -Thread-A end to invoke doA
 * 2016-08-27 17:21:18,889 -End using 28021ms
 * </pre>
 *
 * @author zhangxu
 */
public class SynchronizedBlockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedBlockTest.class);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(3);
        SomeObject object = new SomeObject();
        AtomicReference<Integer> num = new AtomicReference<Integer>(0);
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        while (true) {
                            if (num.compareAndSet(0, 1)) {
                                LOGGER.info(Thread.currentThread().getName() + " begin to invoke doA");
                                object.doA(endGate);
                                LOGGER.info(Thread.currentThread().getName() + " end to invoke doA");
                                break;
                            } else if (num.compareAndSet(1, 2)) {
                                LOGGER.info(Thread.currentThread().getName() + " begin to invoke doB");
                                object.doB(endGate);
                                LOGGER.info(Thread.currentThread().getName() + " end to invoke doB");
                                break;
                            } else if (num.compareAndSet(2, 3)) {
                                LOGGER.info(Thread.currentThread().getName() + " begin to invoke doC");
                                object.doC(endGate);
                                LOGGER.info(Thread.currentThread().getName() + " end to invoke doC");
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.setName("Thread-" + Character.toString((char) ('A' + i)));
            t.start();
        }

        long start = System.currentTimeMillis();
        LOGGER.info("Ready to start");
        startGate.countDown();
        endGate.await();
        long end = System.currentTimeMillis();
        LOGGER.info("End using " + (end - start) + "ms");
    }

    static class SomeObject {

        public synchronized void doA(CountDownLatch cdl) {
            try {
                int sleepTime = 1000 * (5 + (new Random()).nextInt(8));
                LOGGER.info("doA in " + sleepTime + "ms");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }
        }

        public synchronized void doB(CountDownLatch cdl) {
            try {
                int sleepTime = 1000 * (5 + (new Random()).nextInt(8));
                LOGGER.info("doB in " + sleepTime + "ms");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }
        }

        public synchronized void doC(CountDownLatch cdl) {
            try {
                int sleepTime = 1000 * (5 + (new Random()).nextInt(8));
                LOGGER.info("doC in " + sleepTime + "ms");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                cdl.countDown();
            }
        }
    }

}

