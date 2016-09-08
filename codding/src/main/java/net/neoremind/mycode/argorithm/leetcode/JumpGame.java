package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Given an array of non-negative integers, you are initially positioned at the first index of the array.
 * <p>
 * Each element in the array represents your maximum jump length at that position.
 * <p>
 * Determine if you are able to reach the last index.
 * <p>
 * For example:
 * A = [2,3,1,1,4], return true.
 * <p>
 * A = [3,2,1,0,4], return false.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/jump-game
 */
public class JumpGame {

    public boolean canJump(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > max) {
                return false;
            }
            max = Math.max(nums[i] + i, max);
        }
        return true;
    }

    @Test
    public void test() {
        int[] nums = new int[] {2, 3, 1, 1, 4};
        assertThat(canJump(nums), is(true));
        nums = new int[] {0};
        assertThat(canJump(nums), is(true));
        nums = new int[] {1};
        assertThat(canJump(nums), is(true));
        nums = new int[] {2};
        assertThat(canJump(nums), is(true));
        nums = new int[] {2, 0, 0, 0, 0, 0, 6};
        assertThat(canJump(nums), is(false));
    }

}
