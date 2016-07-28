package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Given a sorted array and a target value, return the index if the target is found. If not, return the index where
 * it would be if it were inserted in order.
 * <p>
 * You may assume no duplicates in the array.
 * <p>
 * Here are few examples.
 * [1,3,5,6], 5 → 2
 * [1,3,5,6], 2 → 1
 * [1,3,5,6], 7 → 4
 * [1,3,5,6], 0 → 0
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/search-insert-position/
 * @see net.neoremind.mycode.argorithm.search.BinarySearch
 */
public class SearchInsertPosition {

    @Test
    public void test() {
        int[] nums = new int[] {1, 3, 5, 6};
        assertThat(searchInsert(nums, 0), is(0));
        assertThat(searchInsert(nums, 1), is(0));
        assertThat(searchInsert(nums, 2), is(1));
        assertThat(searchInsert(nums, 3), is(1));
        assertThat(searchInsert(nums, 4), is(2));
        assertThat(searchInsert(nums, 5), is(2));
        assertThat(searchInsert(nums, 6), is(3));
        assertThat(searchInsert(nums, 7), is(4));
    }

    public int searchInsert(int[] nums, int target) {
        return search(nums, 0, nums.length - 1, target);
    }

    public int search(int[] nums, int start, int end, int target) {
        if (start > end) {
            return end + 1;
        }
        int mid = start + (end - start) / 2;
        if (nums[mid] > target) {
            return search(nums, start, mid - 1, target);
        } else if (nums[mid] < target) {
            return search(nums, mid + 1, end, target);
        } else {
            return mid;
        }
    }
}
