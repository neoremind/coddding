package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array of numbers nums, in which exactly two elements appear only once and all the other elements appear
 * exactly twice. Find the two elements that appear only once.
 * <p>
 * For example:
 * <p>
 * Given nums = [1, 2, 1, 3, 2, 5], return [3, 5].
 * <p>
 * Note:
 * The order of the result is not important. So in the above example, [5, 3] is also correct.
 * Your algorithm should run in linear runtime complexity. Could you implement it using only constant space complexity?
 *
 * @author xu.zhang
 */
public class SingleNumberIII {

    public static int[] singleNumber(int[] nums) {
        int diff = 0;
        for (int num : nums) {
            diff ^= num;
        }
        diff &= -diff;
        int[] res = new int[]{0, 0};
        for (int num : nums) {
            if ((num & diff) == 0) {
                res[0] ^= num;
            } else {
                res[1] ^= num;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{8, 6, 3, 6, 1, 3, 2, 8};
        int[] single = singleNumber(nums);
        System.out.println(single[0] + " " + single[1]);
        assertThat(single, is(new int[]{2, 1}));

        nums = new int[]{1, 4, 3, 1, 2, 3};
        single = singleNumber(nums);
        System.out.println(single[0] + " " + single[1]);
        assertThat(single, is(new int[]{4, 2}));
    }
}
