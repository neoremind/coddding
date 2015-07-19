package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array of integers, every element appears twice except for one. Find that single one.
 * <p/>
 * Note:
 * Your algorithm should have a linear runtime complexity.
 * Could you implement it without using extra memory?
 * <p/>
 * 思路:
 * 1. 循环两次数组，时间复杂度o(n2)，空间需要额外两个变量临时存储，不满足题目要求。
 * <p/>
 * 2. bit manipulation method.
 * int 数据共有32位，可以用32变量存储这N个元素中各个二进制位上1出现的次数，最后在进行模2操作，
 * 如果为1，那说明这一位是要找元素二进制表示中为 1 的那一位。见{@link #singleNumber2(int[])}
 * <p/>
 * 3. 利用异或，时间复杂度O(n)，而空间上也不需要任何额外变量。见{@link #singleNumber(int[])}
 * <p/>
 * 对于异或来说：<br/>
 * 1) 满足交换律，即 a ^ b = b ^ a <br/>
 * 2) 0 ^ a = a <br/>
 * 3) a ^ a = 0 <br/>
 * 那么如果对所有元素做异或运算，其结果为那个出现一次的元素，理解是a1 ^ a2 ^ ....，可以将所有相同元素交换至相邻位置，
 * 首先运算相同元素，则会产生(n - 1)/2个0异或积，剩余一个单一元素，他们的异或积为这个单一元素自己，得解。
 */
public class SingleNumber {

    public static int singleNumber2(int[] nums) {
        int[] digits = new int[32];
        for (int i = 0; i < 32; i++) {
            for (int num : nums) {
                digits[i] += (num >> i) & 1;
            }
        }
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res += (digits[i] % 2) << i;
        }
        return res;
    }


    public static int singleNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 1, 2, 3};
        int single = singleNumber(nums);
        System.out.println(single);
        assertThat(single, is(4));

        nums = new int[]{1, 6, 3, 6, 8, 1, 3};
        single = singleNumber(nums);
        System.out.println(single);
        assertThat(single, is(8));

        nums = new int[]{1};
        single = singleNumber(nums);
        System.out.println(single);
        assertThat(single, is(1));

        nums = new int[]{1, 2, 3, 4, 1, 2, 3};
        single = singleNumber2(nums);
        System.out.println(single);
        assertThat(single, is(4));

        nums = new int[]{1, 6, 3, 6, 8, 1, 3};
        single = singleNumber2(nums);
        System.out.println(single);
        assertThat(single, is(8));

        nums = new int[]{1};
        single = singleNumber2(nums);
        System.out.println(single);
        assertThat(single, is(1));
    }

}
