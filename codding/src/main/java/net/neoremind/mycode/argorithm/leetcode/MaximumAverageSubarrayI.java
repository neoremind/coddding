package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array consisting of n integers, find the contiguous subarray of given length k that has the maximum
 * average value. And you need to output the maximum average value.
 * <p>
 * Example 1:
 * Input: [1,12,-5,-6,50,3], k = 4
 * Output: 12.75
 * Explanation: Maximum average is (12-5-6+50)/4 = 51/4 = 12.75
 * Note:
 * 1 <= k <= n <= 30,000.
 * Elements of the given array will be in the range [-10,000, 10,000].
 * <p>
 * https://leetcode.com/problems/maximum-average-subarray-i/#/description
 * <p>
 * https://leetcode.com/problems/maximum-average-subarray-i/#/solution
 *
 * @author xu.zhang
 */
public class MaximumAverageSubarrayI {

    /**
     * Approach #1 Brute Force [Time Limit Exceeded]
     * <p>
     * Time complexity : O(n*k). We traverse over subarrays of length k for a total of n-k times.
     * <p>
     * Space complexity : O(1). Constant extra space is used.
     * <p>
     * 容易导致TLE
     */
    public double findMaxAverage(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k > nums.length) {
            return 0.0;
        }
        double maxAvg = Double.MIN_VALUE;
        for (int i = 0; i <= nums.length - k; i++) {
            int sum = 0;
            for (int j = 0; j < k; j++) {
                sum += nums[i + j];
            }
            maxAvg = Math.max(sum, maxAvg);
        }
        return maxAvg / k;
    }

    /**
     * Approach #2 Cumulative Sum [Accepted]
     * <p>
     * <pre>
     *     nums = [ 1,  12 ,  -5  , -6 ,  50 , 3  , -30 , 25 ]
     *  累加的和 = [ 1,  13 ,  8   ,  2 ,  52 , 55 , 25  , 50 ]
     *
     *  idx 0 - 3 sum = sum[3];
     *  idx 1 - 4 sum = sum[4] - sum[0];
     *  idx 2 - 5 sum = sum[5] - sum[1];
     *  idx 3 - 6 sum = sum[6] - sum[2];
     *  idx 4 - 7 sum = sum[7] - sum[3];
     * </pre>
     * <p>
     * in order to find the sum of elements from the index i to i+k,
     * all we need to do is to use: sum[i] - sum[i-k].
     * <p>
     * Time complexity : O(n). We iterate over the numsnums array of length n once to fill the sumsum array. Then, we
     * iterate over n-kn−k elements of sumsum to determine the required result.
     * <p>
     * Space complexity : O(n). We make use of a sumsum array of length n to store the cumulative sum.
     */
    public double findMaxAverage2(int[] nums, int k) {
        int[] sum = new int[nums.length];
        sum[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        double maxAvg = sum[k - 1] * 1.0 / k;
        for (int i = k; i < nums.length; i++) {
            maxAvg = Math.max(maxAvg, (sum[i] - sum[i - k]) * 1.0 / k);
        }
        return maxAvg;
    }

    /**
     * Approach #3 Sliding Window [Accepted]
     * <p>
     * Time complexity : O(n). We iterate over the given numsnums array of length n once only.
     * <p>
     * Space complexity : O(1). Constant extra space is used.
     */
    public double findMaxAverage3(int[] nums, int k) {
        double sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        double res = sum;
        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            res = Math.max(res, sum);
        }
        return res / k;
    }

    @Test
    public void test() {
        assertThat(findMaxAverage(new int[]{0, 1, 1, 3, 3}, 4), is(2.0));
        assertThat(findMaxAverage(new int[]{1, 12, -5, -6, 50, 3}, 4), is(12.75));

        assertThat(findMaxAverage2(new int[]{0, 1, 1, 3, 3}, 4), is(2.0));
        assertThat(findMaxAverage2(new int[]{1, 12, -5, -6, 50, 3}, 4), is(12.75));

        assertThat(findMaxAverage3(new int[]{0, 1, 1, 3, 3}, 4), is(2.0));
        assertThat(findMaxAverage3(new int[]{1, 12, -5, -6, 50, 3}, 4), is(12.75));
    }

}
