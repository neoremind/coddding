package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array nums and a target value k, find the maximum length of a subarray that sums to k. If there isn't
 * one, return 0 instead.
 * <p>
 * Example 1:
 * Given nums = [1, -1, 5, -2, 3], k = 3,
 * return 4. (because the subarray [1, -1, 5, -2] sums to 3 and is the longest)
 * <p>
 * Example 2:
 * Given nums = [-2, -1, 2, 1], k = 1,
 * return 2. (because the subarray [-1, 2] sums to 1 and is the longest)
 * <p>
 * Follow Up:
 * Can you do it in O(n) time?
 *
 * @author xu.zhang
 */
public class MaximumSizeSubarraySumEqualsK {

    public int maxSubArrayLen(int[] nums, int k) {
        int sum = 0;
        int maxLen = Integer.MIN_VALUE;
        Map<Integer, Integer> mem = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum == k) {
                maxLen = i + 1;
            } else if (mem.containsKey(sum - k)) {
                maxLen = Math.max(maxLen, i - mem.get(sum - k));
            }
            if (!mem.containsKey(sum)) {
                mem.put(sum, i);
            }
        }
        return maxLen == Integer.MIN_VALUE ? -1 : maxLen;
    }

    @Test
    public void test() {
        assertThat(maxSubArrayLen(new int[]{1, -1, 5, -2, 3}, 3), is(4));
        assertThat(maxSubArrayLen(new int[]{-2, -1, 2, 1}, 1), is(2));
        assertThat(maxSubArrayLen(new int[]{5, 2, 3, 1, 2, 3, 4, 5}, 10), is(4));
        assertThat(maxSubArrayLen(new int[]{5, 2, 3, 1, 2, 3, -1, 1, 4, 5}, 10), is(6));
        assertThat(maxSubArrayLen(new int[]{1, 2, 3, 4, 5}, 10), is(4));
        assertThat(maxSubArrayLen(new int[]{1, 2, 3, 4, 1, 1, 1, 2}, 10), is(5));
    }

}
