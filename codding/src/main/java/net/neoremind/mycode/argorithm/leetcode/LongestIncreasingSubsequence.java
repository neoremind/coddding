package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.stream.IntStream;

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
 *
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/longest-increasing-subsequence/
 */
public class LongestIncreasingSubsequence {

    /**
     * Time complexity : O(n^2)
     * 递推式如下：
     * <pre>
     *     d(i) = max{1, d(j) + 1},其中j<i,A[j]<=A[i]
     * </pre>
     */
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
        return IntStream.of(longest).max().getAsInt();  //JDK8
    }

    public int lengthOfLIS2(int[] nums) {
        int[] longest = new int[nums.length];
        Arrays.fill(longest, 1);
        int res = 1;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    longest[i] = Math.max(longest[i], longest[j] + 1);
                }
                res = Math.max(res, longest[i]);
            }
        }
        return res;
    }

    @Test
    public void test() {
        int[] nums = new int[] {10, 9, 2, 5, 3, 7, 101, 18};
        assertThat(lengthOfLIS(nums), Matchers.is(4));
        nums = new int[] {10, 9, 2, 5, 3, 4};
        assertThat(lengthOfLIS(nums), Matchers.is(3));
    }
}
