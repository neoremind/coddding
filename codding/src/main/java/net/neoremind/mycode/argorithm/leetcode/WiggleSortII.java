package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.sort.ArrayHelper;

/**
 * Given an unsorted array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....
 * <p>
 * Example:
 * (1) Given nums = [1, 5, 1, 1, 6, 4], one possible answer is [1, 4, 1, 5, 1, 6].
 * (2) Given nums = [1, 3, 2, 2, 3, 1], one possible answer is [2, 3, 1, 3, 1, 2].
 * <p>
 * Note:
 * You may assume all input has valid answer.
 * <p>
 * Follow Up:
 * Can you do it in O(n) time and/or in-place with O(1) extra space?
 * <p>
 * https://leetcode.com/problems/wiggle-sort-ii/
 *
 * @author zhangxu
 */
//TODO quick select思想，但是最后的交换总是不太对，faint :-(
public class WiggleSortII {

    @Test
    public void testIsValidSudoku() {
        int[] nums = new int[] {1, 5, 1, 1, 6, 4};
        wiggleSort(nums);
        assertThat(nums, Matchers.is(new int[] {1, 5, 1, 4, 1, 6}));

        nums = new int[] {1, 5, 1, 6, 4};
        wiggleSort(nums);
        assertThat(nums, Matchers.is(new int[] {1, 6, 4, 5, 1}));

        // 1 3 2 2 1 3
        //nums = new int[] {1, 3, 2, 2, 3, 1};
        nums = new int[] {1, 1, 2, 2, 3, 3};
        wiggleSort(nums);
        System.out.println(Arrays.toString(nums));
        // assertThat(nums, Matchers.is(new int[] {2, 3, 1, 3, 1, 2}));

        nums = ArrayHelper.getShuffledArray(11);
        wiggleSort(nums);
        System.out.println(Arrays.toString(nums));
    }

    // overall time complexity is O(N)
    public void wiggleSort(int[] nums) {
        // preconditions…
        if (nums == null || nums.length == 0) {
            return;
        }
        if (nums.length == 1) {
            return;
        }
        int len = nums.length;
        int mid = len >> 1;
        partition(nums, 0, len - 1, mid);
        System.out.println(Arrays.toString(nums));
        if (len % 2 == 0) {
            int i = 1;
            int j = len - 2;
            while (i < mid) {
                swap(i, j, nums);
                i += 2;
                j -= 2;
            }
        } else {
            int i = 1;
            int j = len - 1;
            while (i < mid) {
                swap(i, j, nums);
                i += 2;
                j -= 2;
            }
        }
    }

    /**
     * O(N) time complexity
     * Return mid index which is len / 2 and satisfy all the left elements
     * are smaller than mid while all the right elements are bigger than mid
     */
    int partition(int[] nums, int start, int end, int targetPos) {
        int pivot = nums[start];
        int i = start;
        int j = end + 1;
        while (true) {
            while (nums[++i] < pivot) {
                if (i == end) {
                    break;
                }
            }
            while (nums[--j] > pivot) {
                if (j == start) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swap(i, j, nums);
        }
        swap(start, j, nums);
        if (j < targetPos) {
            return partition(nums, j + 1, end, targetPos);
        } else if (j > targetPos) {
            return partition(nums, start, j - 1, targetPos);
        } else {
            return j;
        }
    }

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
