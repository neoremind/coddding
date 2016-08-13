package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 最大子数组的和，动态规划和暴力求解两种方式
 *
 * @author zhangxu
 */
public class MaxSumOfSubArray {

    @Test
    public void test() {
        int[] array = new int[] {6, 4, 5, -3, 9, 7};
        int result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(28));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(28));

        array = new int[] {1, -2, 3, -4, 5, -6};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(5));

        array = new int[] {-1, 2, 3, -4, 2};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(5));

        array = new int[] {0};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(0));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(0));
    }

    /**
     * O(N^2)暴力求解
     */
    public int findMaxSumOfSubArray_BruteForce(int[] array) {
        int maxSum = array[0];
        for (int i = 0; i < array.length; i++) {
            int subSum = 0;
            for (int j = i; j < array.length; j++) {
                subSum += array[j];
                if (subSum > maxSum) {
                    maxSum = subSum;
                }
            }
        }
        return maxSum;
    }

    /**
     * 动态规划求解。
     * s[i]表示以i结尾最大的子数组和，如果s[i - 1]小于0了，则说明在i肯定是保留现在的值更大，否则就叠加前面的s[i - 1]
     * <pre>
     * s[i] = Max{s[i] (s[i - 1] < 0), s[i - 1] + v[i]}
     * </pre>
     * <p>
     * 最后遍历一下s[i]数组即可。
     */
    public int findMaxSumOfSubArray_DP(int[] array) {
        int[] s = new int[array.length];
        s[0] = array[0];
        for (int i = 1; i < array.length; i++) {
            if (s[i - 1] < 0) {
                s[i] = array[i];
            } else {
                s[i] = s[i - 1] + array[i];
            }
        }

        int maxSum = s[0];
        for (int i : s) {
            if (i > maxSum) {
                maxSum = i;
            }
        }
        return maxSum;
    }
}
