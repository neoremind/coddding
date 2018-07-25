package net.neoremind.mycode.concurrent.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 实验signal和signalAll的区别
 *
 * @author xu.zx
 */
@Slf4j
public class LostSignalTest {

  private final Lock lock = new ReentrantLock();

  private final Condition done = lock.newCondition();

  private volatile boolean finished;

  public static final int TIMEOUT_IN_MS = 5000;

  private final AtomicInteger COUNTER = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    LostSignalTest test = new LostSignalTest();
    test.run();
  }

  private void run() throws InterruptedException {
    runMultipleThreads(10);

    new Thread(() -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        //omit
      }
      triggerDoneEvent();
    }).start();

    Thread.sleep(10000);
  }

  private void runMultipleThreads(int number) {
    for (int i = 0; i < number; i++) {
      new Thread(() -> {
        log.info(executeAndGetResult());
      }).start();
    }
  }

  private String executeAndGetResult() {
    if (!isDone()) {
      long start = System.currentTimeMillis();
      lock.lock();
      try {
        while (!isDone()) {
          done.await(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
          if (isDone() || System.currentTimeMillis() - start > TIMEOUT_IN_MS) {
            break;
          }
        }
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      } finally {
        lock.unlock();
      }
      if (!isDone()) {
        throw new RuntimeException("Timeout and not finished on time");
      }
    }
    return "OK" + COUNTER.getAndIncrement();
  }

  private void triggerDoneEvent() {
    lock.lock();
    try {
      finished = true;
      if (done != null) {
        done.signal();
      }
    } finally {
      lock.unlock();
    }
  }

  private boolean isDone() {
    return finished;
  }

}
