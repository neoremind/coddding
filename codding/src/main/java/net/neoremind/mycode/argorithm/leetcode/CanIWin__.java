package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;

import java.util.Arrays;

import static org.junit.Assert.assertThat;

/**
 * 不带记忆化搜索的版本，TLE
 *
 * @author xu.zx
 */
public class CanIWin__ {

  public static boolean canIWin(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) return true;
    if (sum(maxChoosableInteger) < desiredTotal) {
      return false;
    }
    boolean[] v = new boolean[maxChoosableInteger + 1];
    return canIWin(maxChoosableInteger, desiredTotal, v);
  }

  private static boolean canIWin(int maxChoosableInteger, int target, boolean[] v) {
    if (target <= 0) {
      return false;
    }
    for (int i = 1; i <= maxChoosableInteger; i++) {
      if (v[i]) {
        continue;
      }
      v[i] = true;
      boolean canNextWin = canIWin(maxChoosableInteger, target - i, v);
      v[i] = false;
      if (!canNextWin) {
        return true;
      }
    }
    return false;
  }

  private static int sum(int n) {
    return (n + 1) * n / 2;
  }

  public static boolean canIWin_(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) return true;
    if (sum(maxChoosableInteger) < desiredTotal) {
      return false;
    }
    boolean[] v = new boolean[maxChoosableInteger + 1];
    return canIWin_(maxChoosableInteger, desiredTotal, v, 0);
  }

  private static boolean canIWin_(int maxChoosableInteger, int target, boolean[] v, int stackDepth) {
    System.out.println(getName(stackDepth) + " can win?");
    if (target <= 0) {
      System.out.println(getName(stackDepth) + " fail because target meet ");
      return false;
    }
    for (int i = 1; i <= maxChoosableInteger; i++) {
      if (v[i]) {
        continue;
      }
      System.out.println(getName(stackDepth) + " choose " + i);
      v[i] = true;
      boolean canNextWin = canIWin_(maxChoosableInteger, target - i, v, stackDepth + 1);
      v[i] = false;
      if (!canNextWin) {
        System.out.println(getName(stackDepth, v) + " definitely win!");
        return true;
      }
    }
    System.out.println(getName(stackDepth, v) + " fail because 对手下一步win");
    return false;
  }

  private static int sum_(int n) {
    return (n + 1) * n / 2;
  }

  private static String getName(int stackDepth) {
    return stackDepth + " : " + (stackDepth % 2 == 0 ? "甲" : "乙");
  }

  private static String getName(int stackDepth, boolean[] v) {
    StringBuilder st = new StringBuilder("[");
    for (int i = 0; i < v.length; i++) {
      st.append(v[i] ? "1" : "0");
    }
    st.append("]");
    return stackDepth + " : " + (stackDepth % 2 == 0 ? "甲" : "乙" + " " + st.toString());
  }

  public static void main(String[] args) {
    assertThat(canIWin(10, 11), Matchers.is(false));
    System.out.println("maxChoosableInteger=" + 4);
    for (int i = 1; i <= 10; i++) {
      System.out.println(i + "=>" + canIWin_(4, i));
      System.out.println();
      System.out.println();
    }
  }

}
