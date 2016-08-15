package net.neoremind.mycode.argorithm.leetcode;

/**
 * 53. Maximum Subarray  QuestionEditorial Solution  My Submissions
 * Total Accepted: 126990
 * Total Submissions: 339761
 * Difficulty: Medium
 * Find the contiguous subarray within an array (containing at least one number) which has the largest sum.
 * <p>
 * For example, given the array [−2,1,−3,4,−1,2,1,−5,4],
 * the contiguous subarray [4,−1,2,1] has the largest sum = 6.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/maximum-subarray/
 */
public class MaximumSubarray {

    /**
     * @see {@link net.neoremind.mycode.argorithm.dp.MaxSumOfSubArray}
     */
    public int maxSubArray(int[] nums) {
        int[] s = new int[nums.length];
        s[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (s[i - 1] < 0) {
                s[i] = nums[i];
            } else {
                s[i] = s[i - 1] + nums[i];
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
     * 另外一种节省空间的做法
     */
    public int maxSubArray2(int[] nums) {
        int maxSum = nums[0], sum = 0;
        for(int i = 0; i < nums.length; ++i){
            sum = Math.max(sum + nums[i], nums[i]);
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    }
}
