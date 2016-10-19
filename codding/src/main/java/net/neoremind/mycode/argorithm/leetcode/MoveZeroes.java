package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an array nums, write a function to move all 0's to the end of it while maintaining the relative order of the
 * non-zero elements.
 * <p>
 * For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums should be [1, 3, 12, 0, 0].
 * <p>
 * Note:
 * You must do this in-place without making a copy of the array.
 * Minimize the total number of operations.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/move-zeroes/
 */
public class MoveZeroes {

    public void moveZeroesNotInOrder(int[] nums) {
        int i = 0;
        int j = nums.length - 1;
        while (i < j) {
            if (nums[i] == 0) {
                swap(i, j, nums);
                j--;
            } else {
                i++;
            }
        }
    }

    public void moveZeroes(int[] nums) {
        int i = 0;
        while (i < nums.length) {
            if (nums[i] == 0) {
                int k = i + 1;
                while (k < nums.length && nums[k] == 0) {
                    k++;
                }
                if (k < nums.length) {
                    swap(i, k, nums);
                }
                i++;
            } else {
                i++;
            }
        }
    }

    /**
     * 最好的
     */
    public void moveZeroes2(int[] nums) {
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[idx++] = nums[i];
            }
        }
        for (int i = idx; i < nums.length; i++) {
            nums[i] = 0;
        }
    }

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        int[] nums = new int[] {0, 1, 0, 3, 12};
        moveZeroesNotInOrder(nums);
        assertThat(nums, Matchers.is(new int[] {12, 1, 3, 0, 0}));

        nums = new int[] {1, 0, 0, 3, 12};
        moveZeroes(nums);
        assertThat(nums, Matchers.is(new int[] {1, 3, 12, 0, 0}));

        nums = new int[] {1, 0, 0, 3, 12};
        moveZeroes2(nums);
        assertThat(nums, Matchers.is(new int[] {1, 3, 12, 0, 0}));
    }

}
