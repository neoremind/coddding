package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 最大连续子数组的乘积
 * <p>
 * Find the contiguous subarray within an array (containing at least one number) which has the largest product.
 * <p>
 * For example, given the array [2,3,-2,4],
 * the contiguous subarray [2,3] has the largest product = 6.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/maximum-product-subarray/
 */
public class MaximumProductSubarray {

    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = nums[0];
        int max = nums[0];
        int min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int tempMax = max;
            max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
            min = Math.min(Math.min(tempMax * nums[i], min * nums[i]), nums[i]);
            res = Math.max(max, res);
        }

        return res;
    }

    /**
     * 处理不了-2, 3, -4这种跨区的段的
     */
    public int maxProductWrong(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int[] dp = new int[nums.length];
        //           nums[i]  (dp[i - 1] ^ nums[i]) >>> 31) & 1 == 1
        // dp[i] =   dp[i - 1] * nums[i]
        //           dp[0]   i=0
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (dp[i - 1] == 0) {
                dp[i] = nums[i] >= 0 ? nums[i] : 0;
            } else if ((((dp[i - 1] ^ nums[i]) >>> 31) & 1) == 1) {
                dp[i] = nums[i];
            } else {
                dp[i] = dp[i - 1] * nums[i];
            }
        }

        int max = Integer.MIN_VALUE;
        for (int n : dp) {
            if (n > max) {
                max = n;
            }
        }
        return max;
    }

    @Test
    public void test() {
        int[] nums = new int[] {-3, -4};
        int res = maxProduct(nums);
        assertThat(res, Matchers.is(12));

        nums = new int[] {2, 3, -2, 4};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(6));

        nums = new int[] {-2, 3, -4};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(24));

        nums = new int[] {-4, -3, -2};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(12));
    }
}
