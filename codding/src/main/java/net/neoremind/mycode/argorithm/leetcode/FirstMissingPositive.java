package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Given an unsorted integer array, find the first missing positive integer.
 * <p>
 * For example,
 * Given [1,2,0] return 3,
 * and [3,4,-1,1] return 2.
 * <p>
 * Your algorithm should run in O(n) time and uses constant space.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/first-missing-positive/
 */
public class FirstMissingPositive {

    public int firstMissingPositive(int[] nums) {
        // nums[i] = i + 1;
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] > 0
                    && nums[i] <= nums.length
                    && nums[nums[i] - 1] != nums[i]) {
                swap(i, nums[i] - 1, nums);
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return nums.length + 1;
    }

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 0};
        assertThat(firstMissingPositive(nums), is(3));

        nums = new int[] {3, 4, -1, 1};
        assertThat(firstMissingPositive(nums), is(2));

        nums = new int[] {5, 4, -1, 1, -9, 2};
        assertThat(firstMissingPositive(nums), is(3));

        nums = new int[] {100, 4, -1, 1, -9, 2};
        assertThat(firstMissingPositive(nums), is(3));
    }

}
