package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 最大子数组的和，三种解法：
 * 1）暴力求解O(N^3) //省略
 * 2）暴力求解O(N^2)
 * 3）动态规划
 * 4）分治
 * <p>
 * 剑指offer 题目31
 * leetcode #53
 *
 * @author zhangxu
 * @see http://www.cnblogs.com/bourbon/archive/2011/08/23/2151044.html
 */
public class MaxSumOfSubArray {

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
     * O(N^logN)分治法
     * 这是个分治的思想，解决复杂问题我们经常使用的一种思维方法——分而治之。
     * 而对于此题，我们把数组A[1..n]分成两个相等大小的块：A[1..n/2]和A[n/2+1..n]，最大的子数组只可能出现在三种情况：
     * A[1..n]的最大子数组和A[1..n/2]最大子数组相同；
     * A[1..n]的最大子数组和A[n/2+1..n]最大子数组相同；
     * A[1..n]的最大子数组跨过A[1..n/2]和A[n/2+1..n]
     * 前两种情况的求法和整体的求法是一样的，因此递归求得。
     * 第三种，我们可以采取的方法也比较简单，沿着第n/2向左搜索，直到左边界，找到最大的和maxleft，以及沿着第n/2+1向右搜索找到最大和maxright，那么总的最大和就是maxleft+maxright。
     * 而数组A的最大子数组和就是这三种情况中最大的一个。
     * <p>
     * <pre>
     * int maxSubArray(int *A,int l,int r) {
     * if l == r
     *     return A[l]
     *     mid = (l+r)/2;
     *     ml = maxSubArray(A,l,mid); //分治
     *     mr = maxSubArray(A,mid+1,r);
     *     for i=mid downto l do
     *         search maxleft;
     *     for i=mid+1 to r do
     *         search maxright;
     *     return max(ml,mr,maxleft+maxright); //归并
     * }
     * </pre>
     */
    public int findMaxSumOfSubArray_DAC(int[] array) {
        return findMaxSumDAC(array, 0, array.length - 1);
    }

    private int findMaxSumDAC(int[] array, int left, int right) {
        if (left == right) {
            return array[left];
        }
        int center = (left + right) / 2;
        int maxLeftSum = findMaxSumDAC(array, left, center);
        int maxRightSum = findMaxSumDAC(array, center + 1, right);

        int maxLeft = Integer.MIN_VALUE, tempLeft = 0;
        int maxRight = Integer.MIN_VALUE, tempRight = 0;
        for (int i = center; i >= left; --i) {
            tempLeft += array[i];
            if (tempLeft > maxLeft) {
                maxLeft = tempLeft;
            }
        }
        for (int i = center + 1; i <= right; ++i) {
            tempRight += array[i];
            if (tempRight > maxRight) {
                maxRight = tempRight;
            }
        }

        int maxCenterSum = maxLeft + maxRight;

        // return max of the three
        return maxCenterSum > maxLeftSum ? (maxCenterSum > maxRightSum ? maxCenterSum : maxRightSum)
                : maxLeftSum > maxRightSum ? maxLeftSum : maxRightSum;
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

    /**
     * 另外一种节省空间的DP做法
     * <pre>
     * maxSum = a[1]
     * sum = a[1]
     *
     * for i: 2 to LENGTH[a]
     *    sum = max(sum + a[i], a[i])
     *    if sum > result
     *       maxSum = sum
     *
     * return maxSum
     * </pre>
     */
    public int findMaxSumOfSubArray_DP_2(int[] array) {
        int maxSum = array[0];
        int sum = 0;
        for (int i = 0; i < array.length; ++i) {
            sum = Math.max(sum + array[i], array[i]);
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    }

    @Test
    public void test() {
        int[] array = new int[] {6, 4, 5, -3, 9, 7};
        int result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(28));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(28));
        result = findMaxSumOfSubArray_DP_2(array);
        assertThat(result, Matchers.is(28));
        result = findMaxSumOfSubArray_DAC(array);
        assertThat(result, Matchers.is(28));

        array = new int[] {1, -2, 3, -4, 5, -6};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP_2(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DAC(array);
        assertThat(result, Matchers.is(5));

        array = new int[] {-1, 2, 3, -4, 2};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DP_2(array);
        assertThat(result, Matchers.is(5));
        result = findMaxSumOfSubArray_DAC(array);
        assertThat(result, Matchers.is(5));

        array = new int[] {0};
        result = findMaxSumOfSubArray_BruteForce(array);
        assertThat(result, Matchers.is(0));
        result = findMaxSumOfSubArray_DP(array);
        assertThat(result, Matchers.is(0));
        result = findMaxSumOfSubArray_DP_2(array);
        assertThat(result, Matchers.is(0));
        result = findMaxSumOfSubArray_DAC(array);
        assertThat(result, Matchers.is(0));
    }

}
