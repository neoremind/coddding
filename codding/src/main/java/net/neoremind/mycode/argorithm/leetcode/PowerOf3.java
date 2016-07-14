package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an integer, write a function to determine if it is a power of three.
 * <p>
 * Follow up:
 * Could you do it without using any loop / recursion?
 *
 * @author zhangxu
 */
public class PowerOf3 {

    /**
     * #Iterative Solution#
     *
     * @param n
     *
     * @return
     */
    public boolean isPowerOf3(int n) {
        if (n > 1) {
            while (n % 3 == 0) {
                n /= 3;
            }
        }
        return n == 1;
    }

    /**
     * #Recursive Solution#
     *
     * @param n
     *
     * @return
     */
    public boolean isPowerOfThree(int n) {
        return n > 0 && (n == 1 || (n % 3 == 0 && isPowerOfThree(n / 3)));
    }

    @Test
    public void testPowerOf3() {
        assertThat(isPowerOf3(3), Matchers.is(true));
        assertThat(isPowerOf3(5), Matchers.is(false));
        assertThat(isPowerOf3(9), Matchers.is(true));
        assertThat(isPowerOf3(27), Matchers.is(true));

        assertThat(isPowerOfThree(3), Matchers.is(true));
        assertThat(isPowerOfThree(5), Matchers.is(false));
        assertThat(isPowerOfThree(9), Matchers.is(true));
        assertThat(isPowerOfThree(27), Matchers.is(true));
    }

}
