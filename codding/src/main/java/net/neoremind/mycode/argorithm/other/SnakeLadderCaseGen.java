package net.neoremind.mycode.argorithm.other;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author xu.zx
 */
public class SnakeLadderCaseGen {

  public static void main(String[] args) throws InterruptedException {
    SnakeLadderCaseGen generator = new SnakeLadderCaseGen();
    for (int i = 0; i < 5; i++) {
      try {
        System.out.println("========");
//        print(generator.genOrigin(10));
        printGen(generator.gen(250000, 30, 30));
      } catch (IllegalStateException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private static void print(int[] a) {
    System.out.println(a.length);
    for (int i = 0; i < a.length; i++) {
      System.out.print(a[i]);
      if (i != a.length - 1) {
        System.out.print(" ");
      }
    }
    System.out.println();
  }

  private static void printGen(int[] a) {
    System.out.println(a.length);
    for (int i = 0; i < a.length; i++) {
      if (a[i] != i) {
        System.out.print(a[i]);
      } else {
        System.out.print(0);
      }
      if (i != a.length - 1) {
        System.out.print(" ");
      }
    }
    System.out.println();
  }

  private int[] genOrigin(int len) {
    int[] res = new int[len];
    for (int i = 0; i < len; i++) {
      res[i] = i;
    }
    return res;
  }

  private int[] gen(int len, int ladderNum, int holeNum) throws InterruptedException {
    Set<Integer> nextSet = new HashSet<>();
    Random random = new Random(System.nanoTime());
    int[] res = new int[len];
    for (int i = 0; i < len; i++) {
      res[i] = i;
    }
    for (int i = 0; i < ladderNum; i++) {
      int ladderIdx = randomUtilValid(random, 1, len - 3, nextSet);
      int nextIdx = randomUtilValid(random, ladderIdx, len - ladderIdx - 1, nextSet);
//      System.out.println("put " + ladderIdx + " next to " + nextIdx);
      res[ladderIdx] = nextIdx;
    }
    for (int i = 0; i < holeNum; i++) {
      int holeIdx = randomUtilValid(random, 1, len - 2, nextSet);
      int backIdx = randomUtilValid(random, 0, holeIdx, nextSet);
//      System.out.println("put " + holeIdx + " back to " + backIdx);
      res[holeIdx] = backIdx;
    }
    return res;
  }

  private static int randomUtilValid(Random random, int start, int limit, Set<Integer> invalidSet) throws InterruptedException {
    try {
      int res = start + random.nextInt(limit);
      int counter = 1;
      while (invalidSet.contains(res)) {
        res = start + random.nextInt(limit);
        if (counter++ % 1000 == 0) {
//          System.out.println("res=" + res + ", start=" + start + ", limit=" + limit + ", set=" + invalidSet);
          throw new IllegalStateException("impossible gen");
        }
      }
      invalidSet.add(res);
      return res;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
//      System.err.println("limit=" + limit);
      throw e;
    }
  }

}
