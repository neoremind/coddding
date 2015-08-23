package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement atoi to convert a string to an integer.
 * <p/>
 * Hint: Carefully consider all possible input cases. If you want a challenge, please do not see below and ask
 * yourself what are the possible input cases.
 * <p/>
 * Notes: It is intended for this problem to be specified vaguely (ie, no given input specs). You are responsible to
 * gather all the input requirements up front.
 * <p/>
 * Update (2015-02-10):
 * The signature of the C++ function had been updated. If you still see your function signature accepts a const char
 * * argument, please click the reload button  to reset your code definition.
 * <p/>
 * spoilers alert... click to show requirements for atoi.
 * <p/>
 * Requirements for atoi:
 * The function first discards as many whitespace characters as necessary until the first non-whitespace character is
 * found. Then, starting from this character, takes an optional initial plus or minus sign followed by as many
 * numerical digits as possible, and interprets them as a numerical value.
 * <p/>
 * The string can contain additional characters after those that form the integral number, which are ignored and have
 * no effect on the behavior of this function.
 * <p/>
 * If the first sequence of non-whitespace characters in str is not a valid integral number, or if no such sequence
 * exists because either str is empty or it contains only whitespace characters, no conversion is performed.
 * <p/>
 * If no valid conversion could be performed, a zero value is returned. If the correct value is out of the range of
 * representable values, INT_MAX (2147483647) or INT_MIN (-2147483648) is returned.
 *
 * @author zhangxu
 */
public class Atoi {

    public int myAtoi(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int sign = 1;
        int num = 0;
        int signCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if (signCount > 1) {
                return 0;
            }
            if (sign == 1 && num > Integer.MAX_VALUE / 10) {
                return Integer.MAX_VALUE;
            }
            if (sign == -1 && num < Integer.MIN_VALUE / 10) {
                return Integer.MIN_VALUE;
            }
            char ch = str.charAt(i);
            if (ch == ' ') {
                continue;
            }
            if (ch == '+') {
                sign = 1;
                signCount++;
                continue;
            }
            if (ch == '-') {
                sign = -1;
                signCount++;
                continue;
            }
            if (ch < '0' || ch > '9') {
                continue;
            }
            int x = ch - '0'; // char to int
            num = num * 10;
            if (sign == 1 && num > Integer.MAX_VALUE - x) {
                return Integer.MAX_VALUE;
            }
            if (sign == -1 && -num < Integer.MIN_VALUE + x) {
                return Integer.MIN_VALUE;
            }
            num = num + x;
        }
        return num * sign;
    }

    @Test
    public void testMyAtoi() {
        System.out.println("Integer.MAX_VALUE=" + Integer.MAX_VALUE);
        System.out.println("Integer.MIN_VALUE=" + Integer.MIN_VALUE);

        int num = myAtoi("123");
        System.out.println(num);
        assertThat(num, Matchers.is(123));

        num = myAtoi("-123");
        System.out.println(num);
        assertThat(num, Matchers.is(-123));

        num = myAtoi("  +123");
        System.out.println(num);
        assertThat(num, Matchers.is(123));

        num = myAtoi(" -+-123");
        System.out.println(num);
        assertThat(num, Matchers.is(0));

        num = myAtoi("0");
        System.out.println(num);
        assertThat(num, Matchers.is(0));

        num = myAtoi("  d e 5 o 9 42b;c");
        System.out.println(num);
        assertThat(num, Matchers.is(5942));

        num = myAtoi("2147483648"); // Integer.MAX_VALUE + 1
        System.out.println(num);
        assertThat(num, Matchers.is(Integer.MAX_VALUE));

        num = myAtoi("21474836470"); // Integer.MAX_VALUE * 10
        System.out.println(num);
        assertThat(num, Matchers.is(Integer.MAX_VALUE));

        num = myAtoi("-2147483649"); // Integer.MIN_VALUE - 12
        System.out.println(num);
        assertThat(num, Matchers.is(Integer.MIN_VALUE));

        num = myAtoi("-21474836480"); // Integer.MIN_VALUE * 10
        System.out.println(num);
        assertThat(num, Matchers.is(Integer.MIN_VALUE));
    }

}
