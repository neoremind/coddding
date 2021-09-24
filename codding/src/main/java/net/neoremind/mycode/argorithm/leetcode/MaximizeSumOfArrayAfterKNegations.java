package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/
 *
 * greedy
 *
 * @author xu.zx
 */
public class MaximizeSumOfArrayAfterKNegations {

  public int largestSumAfterKNegations(int[] A, int K) {
    A = Arrays.stream(A).boxed().sorted(new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        return Integer.compare(Math.abs(b - 0), Math.abs(a - 0));
      }
    }).mapToInt(p -> p).toArray();


    for (int i = 0; i < A.length; i++) {
      if (K == 0) break;
      if (A[i] < 0) {
        A[i] = ~A[i] + 1;
        K--;
      }
    }
    if (K > 0) {
      if ((K & 0x01) == 1) {
        A[A.length - 1] = ~A[A.length - 1] + 1;
      }
    }

    int sum = 0;
    for (int i = 0; i < A.length; i++) {
      sum += A[i];
    }
    return sum;
  }
}
