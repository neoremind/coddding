package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
 * <p>
 * The update(i, val) function modifies nums by updating the element at index i to val.
 * Example:
 * Given nums = [1, 3, 5]
 * <p>
 * sumRange(0, 2) -> 9
 * update(1, 2)
 * sumRange(0, 2) -> 8
 * Note:
 * The array is only modifiable by the update function.
 * You may assume the number of calls to update and sumRange function is distributed evenly.
 * <p>
 * https://leetcode.com/problems/range-sum-query-mutable/
 * <p>
 * 使用Binary Indexed Tree，BIT，树状数组
 *
 * @author zhangxu
 */
public class RangeSumQueryMutable {

    @Test
    public void test() {
        int[] nums = new int[] {1, 3, 5};
        NumArray numArray = new NumArray(nums);
        assertThat(numArray.sumRange(0, 2), Matchers.is(9));
        numArray.update(1, 2);
        assertThat(numArray.sumRange(1, 2), Matchers.is(7));
    }

    class NumArray {
        int[] nums;
        int[] BIT;
        int n;

        public NumArray(int[] nums) {
            this.nums = nums;

            n = nums.length;
            BIT = new int[n + 1];
            for (int i = 0; i < n; i++) {
                init(i, nums[i]);
            }
        }

        public void init(int i, int val) {
            i++;
            while (i <= n) {
                BIT[i] += val;
                i += (i & -i);
            }
        }

        void update(int i, int val) {
            int diff = val - nums[i];
            nums[i] = val;
            init(i, diff);
        }

        public int getSum(int i) {
            int sum = 0;
            i++;
            while (i > 0) {
                sum += BIT[i];
                i -= (i & -i);
            }
            return sum;
        }

        public int sumRange(int i, int j) {
            return getSum(j) - getSum(i - 1);
        }
    }
}
