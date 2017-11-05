package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertThat;

/**
 * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
 * <p>
 * Example:
 * Given nums = [-2, 0, 3, -5, 2, -1]
 * <p>
 * sumRange(0, 2) -> 1
 * sumRange(2, 5) -> -1
 * sumRange(0, 5) -> -3
 * Note:
 * You may assume that the array does not change.
 * There are many calls to sumRange function.
 *
 * @author xu.zhang
 */
public class RangeSumQueryImmutable {

    @Test
    public void test() {
        int[] nums = new int[]{-2, 0, 3};
        RangeSumQueryImmutable.NumArray numArray = new RangeSumQueryImmutable.NumArray(nums);
        assertThat(numArray.sumRange(0, 2), Matchers.is(1));
        assertThat(numArray.sumRange(1, 2), Matchers.is(3));
        assertThat(numArray.sumRange(0, 1), Matchers.is(-2));

        nums = new int[]{-2, 0, 3, -5, 2, -1};
        numArray = new RangeSumQueryImmutable.NumArray(nums);
        assertThat(numArray.sumRange(0, 2), Matchers.is(1));
        assertThat(numArray.sumRange(2, 5), Matchers.is(-1));
        assertThat(numArray.sumRange(0, 5), Matchers.is(-3));

    }

    class NumArray {

        int[] sum;

        public NumArray(int[] nums) {
            sum = new int[nums.length + 1];
            if (nums.length != 0) {
                for (int i = 1; i <= nums.length; i++) {
                    sum[i] = sum[i - 1] + nums[i - 1];
                }
                System.out.println(Arrays.toString(sum));
            }
        }

        public int sumRange(int i, int j) {
            if (i == 0) {
                return sum[j + 1];
            }
            return sum[j + 1] - sum[i];
        }
    }

}
