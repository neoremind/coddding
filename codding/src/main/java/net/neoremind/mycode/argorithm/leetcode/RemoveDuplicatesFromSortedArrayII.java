package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Follow up for "Remove Duplicates":
 * What if duplicates are allowed at most twice?
 * <p>
 * For example,
 * Given sorted array nums = [1,1,1,2,2,3],
 * <p>
 * Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3. It doesn't
 * matter what you leave beyond the new length.
 * <p>
 * 和{@link RemoveDuplicatesFromSortedArray}的模板一样，都是基于这里i和slow-N比较，如大于才共同进步
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/remove-duplicates-from-sorted-array/
 */
public class RemoveDuplicatesFromSortedArrayII {

    public int removeDuplicates(int[] nums) {
        if (nums.length < 3) {
            return 2;
        }
        int slow = 2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[i] > nums[slow - 2]) {
                nums[slow++] = nums[i];
            }
        }
        return slow;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3, 3, 3, 4, 4, 5, 6};
        assertThat(removeDuplicates(nums), Matchers.is(8));
        System.out.println(Arrays.toString(nums));

        nums = new int[] {1, 1, 2};
        assertThat(removeDuplicates(nums), Matchers.is(3));
        System.out.println(Arrays.toString(nums));

        nums = new int[] {1, 2};
        assertThat(removeDuplicates(nums), Matchers.is(2));
        System.out.println(Arrays.toString(nums));

    }
}
