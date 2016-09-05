package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;

/**
 * Given two numbers represented as strings, return multiplication of the numbers as a string.
 * <p>
 * Note:
 * The numbers can be arbitrarily large and are non-negative.
 * Converting the input string to integer is NOT allowed.
 * You should NOT use internal library such as BigInteger.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/multiply-strings/
 */
public class MultiplyStrings {

    /**
     * <pre>
     *         0   1   2  i
     *             0   1  j
     * ==================
     *         1   2   3
     *             4   5
     * ------------------
     *             1   5
     *         1   0
     *     0   5
     * ==================
     *         1   2
     *     0   8
     *     4
     * ==================
     * 0   1   2   3   4  index
     *
     * 从高到低，先计算低位，第i位*第j位都是对应的，结果的高位存储在(i+j)位，低位存储在(i+j+1)位。
     *
     * </pre>
     */
    public String multiply(String num1, String num2) {
        if (num1 == null || num1.length() == 0) {
            return num2;
        }
        if (num2 == null || num2.length() == 0) {
            return num1;
        }
        int len1 = num1.length();
        int len2 = num2.length();
        int[] res = new int[len1 + len2];
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int value = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int sum = value + res[i + j + 1];
                res[i + j] = res[i + j] + sum / 10;
                res[i + j + 1] = (sum) % 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int p : res) {
            if (!(sb.length() == 0 && p == 0)) {
                sb.append(p);
            }
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    @Test
    public void test() {
        assertThat(multiply("1", "2"), Matchers.is("2"));
        assertThat(multiply("0", "9"), Matchers.is("0"));
        assertThat(multiply("13", "2"), Matchers.is("26"));
        assertThat(multiply("13", "13"), Matchers.is("169"));
        assertThat(multiply("13", "100"), Matchers.is("1300"));
        assertThat(multiply("26", "599"), Matchers.is("15574"));
    }
}
