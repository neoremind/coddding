package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an array of n positive integers and a positive integer s, find the minimal length of a subarray of which the
 * sum ≥ s. If there isn't one, return 0 instead.
 * <p>
 * For example, given the array [2,3,1,2,4,3] and s = 7,
 * the subarray [4,3] has the minimal length under the problem constraint.
 * <p>
 * Array Two Pointers Binary Search
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/minimum-size-subarray-sum/
 */
public class MinimumSizeSubarraySum {

    /**
     * O(n^2)
     * Time Limit Exceeded
     */
    public int minSubArrayLen(int s, int[] nums) {
        int minLen = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int sub = 0;
            for (int j = i; j < nums.length; j++) {
                sub += nums[j];
                if (sub >= s) {
                    minLen = Math.min(minLen, j - i + 1);
                    break;
                }
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    /**
     * 滑动窗口的概念，
     * 1）start/end固定起点
     * 2）固定start，滑动end，维护一个刚好>=s的窗口，记录这个长度是一个解
     * 3）把窗口的左边往前移动，维持一个刚好小于s的窗口，因为要给end向前走的可能，继续这个窗口。
     * （证明这个要想象为什么不会错过start，因为第二步是刚好>=s就停止的了窗口，所以错过的start绝对不可能是起点，因为有他们没有他们都一样）
     * <pre>
     *  start/end
     *     2,      3,     1,      2,      4,     3
     *
     *  start                    end
     *    [2,      3,     1,      2,]     4,     3   是一个解，进入内部第二个循环, minLen=4
     *
     *          start            end
     *     2,      3,     1,      2,      4,     3
     *
     *          start                    end
     *     2,     [3,     1,      2,      4,]    3  是一个解，进入内部第二个循环,minLen=4
     *
     *                   start           end
     *     2,      3,    [1,      2,      4,]    3  minLen=3
     *
     *                          start    end
     *     2,      3,     1,      2,      4,     3
     *
     *                          start           end
     *     2,      3,     1,     [2,      4,     3] 是一个解，进入内部第二个循环,minLen=3
     *
     *                                   start   end
     *     2,      3,     1,      2,     [4,     3]   minLen=2
     *
     *                                         start/end
     *     2,      3,     1,      2,      4,     3
     * </pre>
     */
    public int minSubArrayLen2(int s, int[] nums) {
        int start = 0, end = 0;
        int minLen = Integer.MAX_VALUE;
        int sum = 0;
        for (; end < nums.length; end++) {
            if (sum < s) {  //上一把小于s我就加
                sum += nums[end];
            }

            while (sum >= s) { //条件满足正好是刚刚超过，就计算下长度，然后找前一个start位置刚好小于s
                minLen = Math.min(end - start + 1, minLen);
                sum -= nums[start];
                start++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    @Test
    public void test() {
        int[] nums = new int[] {2, 3, 1, 2, 4, 3};
        assertThat(minSubArrayLen(7, nums), Matchers.is(2));

        nums = new int[] {1, 1, 1};
        assertThat(minSubArrayLen(7, nums), Matchers.is(0));

        nums = new int[] {};
        assertThat(minSubArrayLen(7, nums), Matchers.is(0));

        nums = new int[] {1, 4, 4};
        assertThat(minSubArrayLen(4, nums), Matchers.is(1));

        nums = new int[] {2, 3, 1, 2, 4, 3};
        assertThat(minSubArrayLen2(7, nums), Matchers.is(2));

        nums = new int[] {1, 1, 1};
        assertThat(minSubArrayLen2(7, nums), Matchers.is(0));

        nums = new int[] {};
        assertThat(minSubArrayLen2(7, nums), Matchers.is(0));

        nums = new int[] {1, 4, 4};
        assertThat(minSubArrayLen2(4, nums), Matchers.is(1));
    }

}
