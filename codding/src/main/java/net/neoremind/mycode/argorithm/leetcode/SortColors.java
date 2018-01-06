package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Given an array with n objects colored red, white or blue, sort them so that objects of the same color are
 * adjacent, with the colors in the order red, white and blue.
 * <p>
 * Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
 * <p>
 * Note:
 * You are not suppose to use the library's sort function for this problem.
 * <p>
 * 荷兰国旗问题
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/sort-colors/
 */
public class SortColors {

    public void sortColors(int[] nums) {
        int begin = 0;
        int current = 0;
        int end = nums.length - 1;
        while (current <= end) {
            if (nums[current] == 0) {
                swap(current, begin, nums);
                begin++;
                current++;
            } else if (nums[current] == 1) {
                current++;
            } else {
                swap(current, end, nums);
                end--;
            }
        }
    }

    public void sortKColors(int[] nums, int k) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for (int i = left; i <= right; i++) {
                max = Math.max(nums[i], max);
                min = Math.min(nums[i], min);
            }
            int current = left;
            while (current <= right) {
                if (nums[current] == min) {
                    swap(current, left, nums);
                    current++;
                    left++;
                } else if (nums[current] > min && nums[current] < max) {
                    current++;
                } else {
                    swap(current, right, nums);
                    right--;
                }
            }
        }
    }

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        int[] nums = {0, 1, 0, 2, 1, 0, 2, 1};
        sortColors(nums);
        assertThat(nums, Matchers.is(new int[]{0, 0, 0, 1, 1, 1, 2, 2}));

        nums = new int[]{0, 1, 0, 3, 2, 3, 1, 5, 0, 4, 0, 2, 1};
        sortKColors(nums, 6);
        assertThat(nums, Matchers.is(new int[]{0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 4, 5}));
    }
}
