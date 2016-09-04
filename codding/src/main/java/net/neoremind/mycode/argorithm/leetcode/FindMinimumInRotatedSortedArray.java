package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.
 * <p>
 * (i.e., 0 1 2 4 5 6 7 might become 4 5 6 7 0 1 2).
 * <p>
 * Find the minimum element.
 * <p>
 * You may assume no duplicate exists in the array.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 */
public class FindMinimumInRotatedSortedArray {

    /**
     * <pre>
     * 情况1：
     * start     mid     end
     * [2,        0,     1]
     *
     * start/mid  end
     * [2,        0,    1]
     *
     *        start/end
     * [2,        0,    1]
     *
     * 情况2：
     * start     mid           end
     * [3,        0,     1,     2]
     *
     * start/mid  end
     * [2,        0,    1]   //同情况1
     *
     * 情况3：
     * start     mid           end
     * [2,        3,     0,     1]
     *
     *                start/mid  end
     * [2,        3,    0,      1]
     *
     *                start/end
     * [2,        3,    0,      1]   RETURN START
     *
     * 情况4：
     * start            mid           end
     * [3,        4,     0,     1,     2]
     *
     * start     mid     end
     * [3,        4,    0,      1,     2]
     *
     *               start/end
     * [2,        3,    0,      1]   RETURN START
     * </pre>
     */
    public int findMin(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int start = 0;
        int end = nums.length - 1;
        while (start < end) {
            int mid = (start + end) >> 1;
            if (nums[mid] > nums[end]) {
                start = mid + 1;
            } else if (nums[mid] <= nums[end]) {  //这个等于放到上面也可以，单独拿出来做end--就可以避免重复
                end = mid;
            }
        }
        return nums[start];
    }

    @Test
    public void test() {
        int[] nums = {4, 5, 6, 7, 8, 0, 1, 2};
        assertThat(findMin(nums), Matchers.is(0));

        nums = new int[] {4, 5, 0, 1, 2};
        assertThat(findMin(nums), Matchers.is(0));

        nums = new int[] {7, 8, 0, 1, 2, 3, 4, 5};
        assertThat(findMin(nums), Matchers.is(0));

        nums = new int[] {0, 1, 2, 3, 4, 5};
        assertThat(findMin(nums), Matchers.is(0));

        nums = new int[] {1, 2, 3, 4, 5, 0};
        assertThat(findMin(nums), Matchers.is(0));

        nums = new int[] {};
        assertThat(findMin(nums), Matchers.is(-1));

        nums = new int[] {3};
        assertThat(findMin(nums), Matchers.is(3));
    }
}
