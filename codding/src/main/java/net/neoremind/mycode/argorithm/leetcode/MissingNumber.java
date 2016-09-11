package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an array containing n distinct numbers taken from 0, 1, 2, ..., n, find the one that is missing from the array.
 * <p>
 * For example,
 * Given nums = [0, 1, 3] return 2.
 * <p>
 * Note:
 * Your algorithm should run in linear runtime complexity. Could you implement it using only constant extra space
 * complexity?
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/missing-number/
 */
public class MissingNumber {

    /**
     * <pre>
     *     index : 0   1   2   3   4
     *     value   0   4   1   2
     *     -XOR----------------------
     *            0-0  1-1 2-2
     *            剩下3和4，那么要得到3就是再次做XOR4也就是长度，去掉4即可。
     *
     *     index : 0   1   2   3   4
     *     value   0   3   1   2
     *     -XOR----------------------
     *            0-0  1-1 2-2  3-3
     *            剩下0，那么要得到0就是再次做XOR4也就是长度，4^0=4。
     * </pre>
     */
    public int missingNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= i;
            res ^= nums[i];
        }
        return res ^ nums.length;
    }

    @Test
    public void test() {
        int[] nums = new int[] {0, 4, 1, 2};
        assertThat(missingNumber(nums), Matchers.is(3));
        nums = new int[] {0, 3, 1, 2};
        assertThat(missingNumber(nums), Matchers.is(4));
    }
}
