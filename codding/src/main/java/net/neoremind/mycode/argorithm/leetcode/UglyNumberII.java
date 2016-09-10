package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Write a program to find the n-th ugly number.
 * <p>
 * Ugly numbers are positive numbers whose prime factors only include 2, 3, 5. For example, 1, 2, 3, 4, 5, 6, 8, 9,
 * 10, 12 is the sequence of the first 10 ugly numbers.
 * <p>
 * Note that 1 is typically treated as an ugly number.
 * <p>
 * <p>
 * 方法1，暴力遍历，利用{@link UglyNumber}
 * 方法2，DP的思想
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/ugly-number-ii/
 */
public class UglyNumberII {

    /**
     * <pre>
     * [1, 2, 3, 4, 5, 6, 8, 9, 10, 12]
     *     ^
     *     |
     *     |
     *     |
     *  index2/index3/index5
     * </pre>
     */
    public int nthUglyNumber(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        int index2 = 0, index3 = 0, index5 = 0;
        int factor2 = 2, factor3 = 3, factor5 = 5;
        for (int i = 1; i < n; i++) {
            int min = Math.min(Math.min(factor2, factor3), factor5);
            dp[i] = min;
            if (factor2 == min) {
                factor2 = 2 * dp[++index2];
            }
            if (factor3 == min) {
                factor3 = 3 * dp[++index3];
            }
            if (factor5 == min) {
                factor5 = 5 * dp[++index5];
            }
        }
        System.out.println(Arrays.toString(dp));
        return dp[n - 1];
    }

    @Test
    public void test() {
        System.out.println(nthUglyNumber(10));
    }

}
