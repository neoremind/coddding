package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ClassName: ReverseInteger <br/>
 * Function: Reverse digits of an integer.
 * 
 * <pre>
 * Example1: x = 123, return 321
 * Example2: x = -123, return -321
 * 
 * click to show spoilers.
 * 
 * Have you thought about this?
 * Here are some good questions to ask before coding. Bonus points for you if you have already thought through this!
 * 
 * If the integer's last digit is 0, what should the output be? ie, cases such as 10, 100.
 * 
 * Did you notice that the reversed integer might overflow? Assume the input is a 32-bit integer, then the reverse of 1000000003 overflows. How should you handle such cases?
 * 
 * For the purpose of this problem, assume that your function returns 0 when the reversed integer overflows.
 * 
 * Update (2014-11-10):
 * Test cases had been added to test the overflow behavior.
 * </pre>
 * 
 * @author Zhang Xu
 */
public class ReverseInteger {

    public static int reverse(int x) {
        int ret = 0;
        if (x >= 0) {
            while (x > 0) {
                // ret * 10 + (x % 10) > Integer.MAX_VALUE
                if (ret > (Integer.MAX_VALUE - x % 10) / 10) {
                    return 0;
                }
                ret = ret * 10 + (x % 10);
                x /= 10;
            }
        } else {
            while (x < 0) {
                // ret * 10 + (x % 10) < Integer.MIN_VALUE
                if (ret < (Integer.MIN_VALUE - x % 10) / 10) {
                    return 0;
                }
                ret = ret * 10 + (x % 10);
                x /= 10;
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        int y = reverse(123);
        System.out.println(y);
        assertThat(y, is(321));

        y = reverse(0);
        System.out.println(y);
        assertThat(y, is(0));

        y = reverse(100);
        System.out.println(y);
        assertThat(y, is(1));

        y = reverse(5);
        System.out.println(y);
        assertThat(y, is(5));

        y = reverse(-123);
        System.out.println(y);
        assertThat(y, is(-321));

        y = reverse(1534236469);
        System.out.println(y);
        assertThat(y, is(0));
    }

}
