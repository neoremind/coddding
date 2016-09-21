package net.neoremind.mycode.argorithm.leetcode;

/**
 * Note: This is an extension of House Robber.
 * <p>
 * After robbing those houses on that street, the thief has found himself a new place for his thievery so that he
 * will not get too much attention. This time, all houses at this place are arranged in a circle. That means the
 * first house is the neighbor of the last one. Meanwhile, the security system for these houses remain the same as
 * for those in the previous street.
 * <p>
 * Given a list of non-negative integers representing the amount of money of each house, determine the maximum amount
 * of money you can rob tonight without alerting the police.
 * <p>
 * 这道题就是在上一题的基础上加了一个条件，变成了环，所以如果抢了第一家，就不能抢最后一家。所以我们可以分别计算抢了从第二家到最后一家与抢从第一家到倒数第二家的最大值，取两个值中更大的那个就是结果。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/house-robber-ii/
 */
public class HouseRobberII {

    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        // dp[i] = Max{dp[i-2] + nums[i], dp[i-1]}
        for (int i = 2; i < nums.length - 1; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }
        int max = dp[nums.length - 2];

        dp[0] = 0;
        dp[1] = nums[1];
        // dp[i] = Max{dp[i-2] + nums[i], dp[i-1]}
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }

        return Math.max(max, dp[nums.length - 1]);
    }

}
