package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an integer, write a function to determine if it is a power of two.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/power-of-two/
 */
public class PowerOf2 {

    public boolean isPowerOf2(int n) {
        if (n < 1) {
            return false;
        }
        return (n & (n - 1)) == 0;
    }

    @Test
    public void testPowerOf3() {
        assertThat(isPowerOf2(-5), Matchers.is(false));
        assertThat(isPowerOf2(0), Matchers.is(false));
        assertThat(isPowerOf2(1), Matchers.is(true));
        assertThat(isPowerOf2(2), Matchers.is(true));
        assertThat(isPowerOf2(3), Matchers.is(false));
        assertThat(isPowerOf2(5), Matchers.is(false));
        assertThat(isPowerOf2(8), Matchers.is(true));
        assertThat(isPowerOf2(9), Matchers.is(false));
        assertThat(isPowerOf2(16), Matchers.is(true));
        assertThat(isPowerOf2(27), Matchers.is(false));
    }

}
