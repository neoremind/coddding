package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an unsorted array of integers, find the length of longest increasing subsequence.
 * <p>
 * For example,
 * Given [10, 9, 2, 5, 3, 7, 101, 18],
 * The longest increasing subsequence is [2, 3, 7, 101], therefore the length is 4.
 * Note that there may be more than one LIS combination, it is only necessary for you to return the length.
 * <p>
 * Your algorithm should run in O(n2) complexity.
 * <p>
 * Follow up: Could you improve it to O(n log n) time complexity?
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/longest-increasing-subsequence/
 */
public class LongestIncreasingSubsequence {

    public int lengthOfLIS(int[] nums) {
        int[] longest = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            longest[i] = 1;
        }
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j] && longest[i] < longest[j] + 1) {
                    longest[i] = longest[j] + 1;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < longest.length; i++) {
            if (longest[i] > max) {
                max = longest[i];
            }
        }
        return max;
    }

    @Test
    public void test() {
        int[] nums = new int[] {10, 9, 2, 5, 3, 7, 101, 18};
        assertThat(lengthOfLIS(nums), Matchers.is(4));
        nums = new int[] {10, 9, 2, 5, 3, 4};
        assertThat(lengthOfLIS(nums), Matchers.is(3));
    }
}
