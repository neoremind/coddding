package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a sorted array, remove the duplicates in place such that each element appear only once and return the new
 * length.
 * <p>
 * Do not allocate extra space for another array, you must do this in place with constant memory.
 * <p>
 * For example,
 * Given input array nums = [1,1,2],
 * <p>
 * Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively. It doesn't
 * matter what you leave beyond the new length.
 * <p>
 * Array Two Pointers
 * Show Similar Problems
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/remove-duplicates-from-sorted-array/
 */
public class RemoveDuplicatesFromSortedArray {

    /**
     * 注意如果用遇到不同就左移后面所有的，那么就太慢了，时间复杂度是O(N!)
     * <p>
     * 这里是O(N)
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length < 2) {
            return 1;
        }
        int slow = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[slow - 1]) {
                nums[slow++] = nums[i];
            }
        }
        return slow;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3, 4, 5, 6};
        assertThat(removeDuplicates(nums), Matchers.is(6));
        System.out.println(Arrays.toString(nums));

        nums = new int[] {1, 1, 2};
        assertThat(removeDuplicates(nums), Matchers.is(2));
        System.out.println(Arrays.toString(nums));

        nums = new int[] {1, 2};
        assertThat(removeDuplicates(nums), Matchers.is(2));
        System.out.println(Arrays.toString(nums));

        nums = new int[] {1};
        assertThat(removeDuplicates(nums), Matchers.is(1));
        System.out.println(Arrays.toString(nums));
    }
}
