package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.stream.IntStream;

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
     * brute force
     */
    public int maxProduct2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = nums[0];
        for (int i = 0; i < nums.length; i++) {
            int max = nums[i];
            int p = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                p *= nums[j];
                max = Math.max(p, max);
            }
            res = Math.max(max, res);
        }
        return res;
    }

    public int maxProduct3(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        // dp[i] = max(max[i - 1] * nums[i], min[i - 1] * nums[i], nums[i])
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int[] max = new int[nums.length];
        max[0] = nums[0];
        int[] min = new int[nums.length];
        min[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            max[i] = Math.max(Math.max(max[i - 1] * nums[i], min[i - 1] * nums[i]), nums[i]);
            min[i] = Math.min(Math.min(max[i - 1] * nums[i], min[i - 1] * nums[i]), nums[i]);
            dp[i] = max[i];
        }
        return IntStream.of(dp).max().getAsInt();
    }

    @Test
    public void test() {
        int[] nums = new int[]{-3, -4};
        int res = maxProduct(nums);
        assertThat(res, Matchers.is(12));

        nums = new int[]{2, 3, -2, 4};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(6));

        nums = new int[]{-2, 3, -4};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(24));

        nums = new int[]{-4, -3, -2};
        res = maxProduct(nums);
        assertThat(res, Matchers.is(12));
    }
}
