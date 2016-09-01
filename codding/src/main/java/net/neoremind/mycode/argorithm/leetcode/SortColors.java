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

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        int[] nums = {0, 1, 0, 2, 1, 0, 2, 1};
        sortColors(nums);
        assertThat(nums, Matchers.is(new int[] {0, 0, 0, 1, 1, 1, 2, 2}));
    }
}
