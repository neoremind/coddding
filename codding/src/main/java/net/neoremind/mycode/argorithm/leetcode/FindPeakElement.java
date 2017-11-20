package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * A peak element is an element that is greater than its neighbors.
 * <p>
 * Given an input array where num[i] ≠ num[i+1], find a peak element and return its index.
 * <p>
 * The array may contain multiple peaks, in that case return the index to any one of the peaks is fine.
 * <p>
 * You may imagine that num[-1] = num[n] = -∞.
 * <p>
 * For example, in array [1, 2, 3, 1], 3 is a peak element and your function should return the index number 2.
 * <p>
 * Your solution should be in logarithmic complexity.
 *
 * @author xu.zhang
 */
public class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid1 = (low + high) / 2;
            int mid2 = mid1 + 1;
            if (nums[mid1] < nums[mid2])
                low = mid2;
            else
                high = mid1;
        }
        return low;
    }

    @Test
    public void test() {
        // low = 0, high = 3
        // mid = 1, nums[1] < nums[2] so low = 2
        // low = 2, high = 3
        // mid = 2, nums[2] > nums[3] so high = 2
        int[] nums = new int[]{1, 2, 3, 0};
        assertThat(nums[findPeakElement(nums)], is(3));

        nums = new int[]{5, 6, 7, 1, 2, 3};
        assertThat(nums[findPeakElement(nums)], is(7));

        nums = new int[]{5, 6, 7, 8, 1, 2, 3, 4};
        assertThat(nums[findPeakElement(nums)], is(8));

        nums = new int[]{5, 6, 7, 8, 1, 2, 3};
        assertThat(nums[findPeakElement(nums)], is(8));

        nums = new int[]{5, 6, 7, 8, 1, 2};
        assertThat(nums[findPeakElement(nums)], is(2));
    }
}