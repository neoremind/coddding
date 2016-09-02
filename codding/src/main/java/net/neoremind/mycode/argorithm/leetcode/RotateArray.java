package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Rotate an array of n elements to the right by k steps.
 * <p>
 * For example, with n = 7 and k = 3, the array [1,2,3,4,5,6,7] is rotated to [5,6,7,1,2,3,4].
 * <p>
 * Note:
 * Try to come up as many solutions as you can, there are at least 3 different ways to solve this problem.
 * <p>
 * [show hint]
 * <p>
 * Hint:
 * Could you do it in-place with O(1) extra space?
 * Related problem: Reverse Words in a String II
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/rotate-array/
 */
public class RotateArray {

    /**
     * k = 3
     * index:
     * [0,1,2,3,4,5,6]
     * <p>
     * nums:
     * [1,2,3,4,5,6,7]
     * <p>
     * result:
     * [5,6,7,1,2,3,4]
     * 找规律：
     * result[i] = nums[(i + k)%len]
     * result[3] = nums[0]  3%7
     * result[4] = nums[1]  4%7
     * result[5] = nums[2]  5%7
     * result[6] = nums[3]  6%7
     * result[0] = nums[4]  7%7
     * result[1] = nums[5]  8%7
     * result[2] = nums[6]  9%7
     */
    public void rotate1(int[] nums, int k) {
        int len = nums.length;
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = nums[i];
        }
        for (int i = 0; i < len; i++) {
            nums[(i + k) % len] = res[i];
        }
    }

    /**
     * <pre>
     * k=4
     * step1: [1,2,3,4,5,6,7]
     * step2: [3,2,1,4,5,6,7]
     * step3: [3,2,1,7,6,5,4]
     * step4: [4,5,6,7,1,2,3]
     * </pre>
     */
    public void rotate2(int[] nums, int k) {
        if (nums == null || nums.length < 2) {
            return;
        }
        k = k % nums.length;
        reverse(nums, 0, nums.length - k - 1);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate1(nums, 5);
        assertThat(nums, Matchers.is(new int[] {3, 4, 5, 6, 7, 1, 2}));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate1(nums, 2);
        assertThat(nums, Matchers.is(new int[] {6, 7, 1, 2, 3, 4, 5}));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate1(nums, 0);
        assertThat(nums, Matchers.is(new int[] {1, 2, 3, 4, 5, 6, 7}));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate2(nums, 5);
        assertThat(nums, Matchers.is(new int[] {3, 4, 5, 6, 7, 1, 2}));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate2(nums, 2);
        assertThat(nums, Matchers.is(new int[] {6, 7, 1, 2, 3, 4, 5}));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7};
        rotate2(nums, 0);
        assertThat(nums, Matchers.is(new int[] {1, 2, 3, 4, 5, 6, 7}));
    }
}
