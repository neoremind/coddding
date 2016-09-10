package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Calculate the sum of two integers a and b, but you are not allowed to use the operator + and -.
 * <p>
 * Example:
 * Given a = 1 and b = 2, return 3.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/sum-of-two-integers/
 */
public class SumOfTwoIntegers {

    public int getSum(int a, int b) {
        String m = toBinary(a);
        String n = toBinary(b);
        int i = 0, j = 0;
        int carry = 0;
        StringBuilder sb = new StringBuilder();
        while (i < m.length() && j < n.length()) {
            int value = ((m.charAt(i++) - '0') + (n.charAt(j++) - '0')) + carry;
            sb.append(value % 2);
            carry = value / 2;
        }
        while (i < m.length()) {
            int value = (m.charAt(i++) - '0') + carry;
            sb.append(value % 2);
            carry = value / 2;
        }
        while (j < n.length()) {
            int value = (n.charAt(j++) - '0') + carry;
            sb.append(value % 2);
            carry = value / 2;
        }
        if (carry != 0) {
            sb.append(carry);
        }
        return toInt(sb.toString());
    }

    int toInt(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                continue;
            }
            res += (s.charAt(i) - '0') << i;
        }
        return res;
    }

    String toBinary(int n) {
        StringBuilder sb = new StringBuilder();
        while (n != 0) {
            int value = n & 1;
            n = n >>> 1;
            sb.append(value);
        }
        return sb.toString();
    }

    @Test
    public void test() {
        assertThat(getSum(56, 43), Matchers.is(99));
    }

}
