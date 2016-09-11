package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Reverse bits of a given 32 bits unsigned integer.
 * <p>
 * For example, given input 43261596 (represented in binary as 00000010100101000001111010011100), return 964176192
 * (represented in binary as 00111001011110000010100101000000).
 * <p>
 * Follow up:
 * If this function is called many times, how would you optimize it?
 * <p>
 * Related problem: Reverse Integer
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/reverse-bits/
 */
public class ReverseBits {

    // you need treat n as an unsigned value
    public int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res += ((n >>> (31 - i)) & 1) << i;
        }
        return res;
    }

    @Test
    public void test() {
        int res = reverseBits(5);
        System.out.println(Integer.toBinaryString(5));
        System.out.println(Integer.toBinaryString(res));
        assertThat(res, Matchers.is(6));
    }

}
