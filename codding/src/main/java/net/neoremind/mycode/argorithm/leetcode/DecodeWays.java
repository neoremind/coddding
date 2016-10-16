package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * 91. Decode Ways   QuestionEditorial Solution  My Submissions
 * Total Accepted: 88666
 * Total Submissions: 481609
 * Difficulty: Medium
 * Contributors: Admin
 * A message containing letters from A-Z is being encoded to numbers using the following mapping:
 * <p>
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 * Given an encoded message containing digits, determine the total number of ways to decode it.
 * <p>
 * For example,
 * Given encoded message "12", it could be decoded as "AB" (1 2) or "L" (12).
 * <p>
 * The number of ways decoding "12" is 2.
 *
 * 题目的要求不是和excel那道题似的考虑进位，而是直接A-Z的映射，没有AB这一说，因此不要理解错误题目！
 *
 * 动态规划：
 * 从当前的数字往前看1个数组，如果数字不等于0，那么就等于前面的所有排列。
 *
 * 往前看两个数字，如果数字在10和26之间，则可以找到一个字母，则等于前面所有的排列，否则就是0，这根本构不成一个合法的解析数字。
 *
 * @author zhangxu
 */
public class DecodeWays {

    public int numDecodings(String s) {
        if (s == null || s.isEmpty() || s.charAt(0) == '0') {
            return 0;
        }
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i <= s.length(); i++) {
            int num = Integer.parseInt(s.substring(i - 2, i));
            int twoStepsBehind = (num <= 26 && num >= 10) ? dp[i - 2] : 0;
            int oneStepBehind = (Integer.parseInt(s.substring(i - 1, i)) != 0) ? dp[i - 1] : 0;
            dp[i] = twoStepsBehind + oneStepBehind; // can reach here by either hopping 2 steps or 1 step
        }

        return dp[s.length()];
    }

    @Test
    public void test() {
        assertThat(numDecodings("1"), is(1));
        assertThat(numDecodings("12"), is(2));
        assertThat(numDecodings("34"), is(1));
        assertThat(numDecodings("123"), is(3));
        assertThat(numDecodings("134"), is(2));
    }

}
