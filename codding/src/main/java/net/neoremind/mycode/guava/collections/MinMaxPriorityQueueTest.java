package net.neoremind.mycode.guava.collections;

import com.google.common.collect.MinMaxPriorityQueue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author xu.zx
 */
public class MinMaxPriorityQueueTest {

  public static void main(String[] args) throws InterruptedException {
    testJavaPq(10000, 5);
    testMinMaxPq(10000, 5);
//
    Thread.sleep(1000);
    testJavaPq(500000, 100);
    Thread.sleep(1000);
    testMinMaxPq(500000, 100);
  }

  private static void testMinMaxPq(int size, int iter) {
    MinMaxPriorityQueue<Long> queue = MinMaxPriorityQueue.orderedBy(new Comparator<Long>() {
      @Override
      public int compare(Long o1, Long o2) {
        return Long.compare(o1, o2);
      }
    })
        .maximumSize(size)
        .create();

    long addTime = 0L;
    long pollTime = 0L;
    for (int j = 0; j < iter; j++) {
      Random random = new Random(0);
      long start = System.currentTimeMillis();
      for (int i = 0; i < size; i++) {
        queue.add(random.nextLong());
      }
      addTime += System.currentTimeMillis() - start;
      long start2 = System.currentTimeMillis();
      for (int i = 0; i < size; i++) {
        long x = queue.poll();
        //System.out.println(x);
      }
      pollTime += System.currentTimeMillis() - start2;
    }
    System.out.println("min-max pq " + addTime + " " + pollTime);
  }

  private static void testJavaPq(int size, int iter) {
    PriorityQueue<Long> queue = new PriorityQueue<>(size, new Comparator<Long>() {
      @Override
      public int compare(Long o1, Long o2) {
        return Long.compare(o1, o2);
      }
    });

    long addTime = 0L;
    long pollTime = 0L;
    for (int j = 0; j < iter; j++) {
      Random random = new Random(0);
      long start = System.currentTimeMillis();
      for (int i = 0; i < size; i++) {
        queue.add(random.nextLong());
      }
      addTime += System.currentTimeMillis() - start;
      long start2 = System.currentTimeMillis();
      for (int i = 0; i < size; i++) {
        long x = queue.poll();
        //System.out.println(x);
      }
      pollTime += System.currentTimeMillis() - start2;
    }
    System.out.println("java pq " + addTime + " " + pollTime);
  }

}
