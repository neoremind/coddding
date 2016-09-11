package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array of integers, every element appears three times except for one. Find that single one.
 * <p>
 * Note:
 * Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
 *
 * @see https://leetcode.com/problems/single-number-ii/
 */
public class SingleNumberII {

    public static int singleNumber(int[] nums) {
        int[] digits = new int[32];
        for (int i = 0; i < 32; i++) {
            for (int num : nums) {
                digits[i] += (num >> i) & 1;
            }
            digits[i] %= 3;
        }
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res += digits[i] << i;
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {8, 6, 3, 6, 1, 6, 3, 8, 3, 8};
        int single = singleNumber(nums);
        System.out.println(single);
        assertThat(single, is(1));

        nums = new int[] {1, 4, 1, 1};
        single = singleNumber(nums);
        System.out.println(single);
        assertThat(single, is(4));
    }

}
