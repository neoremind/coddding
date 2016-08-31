package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.
 * <p>
 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
 * <p>
 * You are given a target value to search. If found in the array return its index, otherwise return -1.
 * <p>
 * You may assume no duplicate exists in the array.
 * <p>
 * HARD难度
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/search-in-rotated-sorted-array/
 */
public class SearchInRotatedSortedArray {

    /**
     * 见README.md
     */
    public int search(int[] nums, int target) {
        int lo = 0;
        int hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (nums[mid] == target) {
                return mid;
            }

            if (nums[lo] <= nums[mid]) {
                if (target >= nums[lo] && target < nums[mid]) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            } else {
                if (target > nums[mid] && target <= nums[hi]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
        }
        return nums[lo] == target ? lo : -1;
    }

    @Test
    public void test() {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        assertThat(search(nums, target), Matchers.is(4));
    }
}
