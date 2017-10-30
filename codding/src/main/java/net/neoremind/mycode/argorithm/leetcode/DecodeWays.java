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
 * <p>
 * 题目的要求不是和excel那道题似的考虑进位，而是直接A-Z的映射，没有AB这一说，因此不要理解错误题目！
 * <p>
 * 动态规划：
 * 从当前的数字往前看1个数组，如果数字不等于0，那么就等于前面的所有排列。
 * <p>
 * 往前看两个数字，如果数字在10和26之间，则可以找到一个字母，则等于前面所有的排列，否则就是0，这根本构不成一个合法的解析数字。
 *
 * @author zhangxu
 */
public class DecodeWays {

    /**
     * <pre>
     * d[i] = (d[i-2] if s.sub[i-2, i] > '10' & < '26' else 0) + d[i-1] if s.substring[i-1,i] >= '1' & <= '9'
     *         0  1
     *         1  1
     *         </pre>
     */
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

    // 12 AB L
    // dp[i] = (dp[i-1] if s[i] > 0 & <= 9)
    //         + (dp[i-2] if s[i-1,i] is between 1 to 26(include))
    //         1 i = 0
    //         1 i = 1
    //         1 i = 2
    // 1  A
    // 12
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            if (s.charAt(0) > '0' && s.charAt(0) <= '9') {
                return 1;
            } else {
                return 0;
            }
        }
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > '0' && c <= '9') {
                dp[i + 1] += dp[i];
            }
            if (i >= 1) {
                String sub = s.substring(i - 1, i + 1);
                int num = Integer.parseInt(sub);
                if (num >= 10 && num <= 26) {
                    dp[i + 1] += dp[i - 1];
                }
            }
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
