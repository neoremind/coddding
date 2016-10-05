package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Write a function that takes an unsigned integer and returns the number of ’1' bits it has (also known as the
 * Hamming weight).
 * <p>
 * For example, the 32-bit integer ’11' has binary representation 00000000000000000000000000001011, so the function
 * should return 3.
 * <p>
 * //TODO hamming code
 * hamming weight 汉明重量是字符串相对于同样长度的零字符串的汉明距离
 * hamming distance 汉明距离就是将一个字符串替换成另外一个字符串所需要替换的字符长度
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/number-of-1-bits/
 */
public class NumberOf1Bits {

    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int flag = 1;
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (flag << i)) != 0) {
                res++;
            }
        }
        return res;
    }

    public int hammingWeight4(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (((n >>> (31 - i)) & 1) == 1) {
                res++;
            }
        }
        return res;
    }

    public int hammingWeight2(int n) {
        int res = 0;
        while (n != 0) {
            if ((n & 1) == 1) {
                res++;
            }
            n = n >>> 1;  //>>退出不了循环
        }
        return res;
    }

    /**
     * 会TLE
     */
    public int hammingWeight3(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }

    @Test
    public void test() {
        int a = 0b10101010111;
        assertThat(hammingWeight(a), Matchers.is(7));
        assertThat(hammingWeight2(a), Matchers.is(7));
        assertThat(hammingWeight3(a), Matchers.is(7));
        a = 0x7fffffff;
        assertThat(a, Matchers.is(Integer.MAX_VALUE));
        assertThat(hammingWeight(a), Matchers.is(31));
        assertThat(hammingWeight2(a), Matchers.is(31));
        assertThat(hammingWeight3(a), Matchers.is(31));
        a = 0x80000000;
        assertThat(a, Matchers.is(Integer.MIN_VALUE));
        assertThat(hammingWeight(a), Matchers.is(1));
        assertThat(hammingWeight2(a), Matchers.is(1));
        assertThat(hammingWeight3(a), Matchers.is(1));
    }
}
