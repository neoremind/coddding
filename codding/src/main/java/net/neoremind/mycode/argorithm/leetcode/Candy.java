package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/candy/
 *
 * @author xu.zx
 */
public class Candy {

  public int candy(int[] ratings) {
    int[] a = new int[ratings.length];
    Arrays.fill(a, 1);
    for (int i = 1; i < ratings.length; i++) {
      if (ratings[i] > ratings[i - 1]) {
        a[i] = a[i - 1] + 1;
      }
    }
    for (int i = ratings.length - 1; i > 0; i--) {
      if (ratings[i - 1] > ratings[i]) {
        a[i - 1] = Math.max(a[i - 1], a[i] + 1);
      }
    }

    int res = 0;
    for (int i = 0; i < a.length; i++) {
      res += a[i];
    }
    return res;
  }

}
