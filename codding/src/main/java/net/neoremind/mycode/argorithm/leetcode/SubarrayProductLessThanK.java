package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Your are given an array of positive integers nums.
 * <p>
 * Count and print the number of (contiguous) subarrays where the product of all the elements in the subarray is less
 * than k.
 * <p>
 * Example 1:
 * Input: nums = [10, 5, 2, 6], k = 100
 * Output: 8
 * Explanation: The 8 subarrays that have product less than 100 are: [10], [5], [2], [6], [10, 5], [5, 2], [2, 6],
 * [5, 2, 6].
 * Note that [10, 5, 2] is not included as the product of 100 is not strictly less than k.
 * Note:
 * <p>
 * 0 < nums.length <= 50000.
 * 0 < nums[i] < 1000.
 * 0 <= k < 10^6.
 *
 * @author xu.zhang
 */
public class SubarrayProductLessThanK {

    public int numSubarrayProductLessThanK2(int[] nums, int k) {
        if (k < 1) {
            return 0;
        }
        int res = 0;
        int product = 1;
        int left = 0;
        for (int i = 0; i < nums.length; i++) {
            product *= nums[i];
            while (product >= k) {
                product = product / nums[left++];
            }
            res += i - left + 1;
        }
        return res;
    }

    int count = 0;

    // WRONG 题目是连续的subarray
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        Arrays.sort(nums);
        helper(nums, 0, k, 1);
        return count;
    }

    void helper(int[] nums, int start, int k, int acc) {
        for (int i = start; i < nums.length; i++) {
            if (acc * nums[i] < k) {
                count++;
                helper(nums, i + 1, k, acc * nums[i]);
            }
        }
    }

    @Test
    public void test() {
        count = 0;
        int[] nums = {2, 3, 4};
        assertThat(numSubarrayProductLessThanK(nums, 100), Matchers.is(7));
        System.out.println();

        count = 0;
        nums = new int[]{1, 2};
        assertThat(numSubarrayProductLessThanK(nums, 10), Matchers.is(3));
        System.out.println();

        count = 0;
        nums = new int[]{1, 1, 1};
        assertThat(numSubarrayProductLessThanK(nums, 2), Matchers.is(7));
        System.out.println();

        count = 0;
        nums = new int[]{10, 5, 2, 6};
        assertThat(numSubarrayProductLessThanK(nums, 100), Matchers.is(11));
        System.out.println();
    }
}
