package net.neoremind.mycode.argorithm.leetcode;

/**
 * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money
 * stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security system
 * connected and it will automatically contact the police if two adjacent houses were broken into on the same night.
 * <p>
 * Given a list of non-negative integers representing the amount of money of each house, determine the maximum amount
 * of money you can rob tonight without alerting the police.
 * <p>
 * 动态规划，设置maxV[i]表示到第i个房子位置，最大收益。
 * <p>
 * 递推关系为maxV[i] = max(maxV[i-2]+num[i], maxV[i-1])
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/house-robber/
 */
public class HouseRobber {

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
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }

        return dp[nums.length - 1];
    }

    /**
     * 到了Z，最大收益等于Max(偷Z+偷X的最大收益, 不偷Z那么就是Y的最大收益)
     * <pre>
     * ..... -- X -- Y -- Z --.....
     * </pre>
     * 好像不太好理解
     *
     * @return
     */
    public int rob2(int[] nums) {
        int notTake = 0;
        int take = 0;
        int max = 0;
        // dp[i] = Max{dp[i-2] + nums[i], dp[i-1]}
        for (int i = 0; i < nums.length; i++) {
            take = notTake + nums[i];
            notTake = max;
            max = Math.max(take, notTake);
        }

        return max;
    }

}
