package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Longest Common Substring, LCS, 最长公共子串
 * <p>
 * 最长公共子序列(Subsequence)是不连续的字符，最长公共子串(Substring)必须是连续的字符。
 *
 * @author zhangxu
 */
public class LCSubstring {

    /**
     * 自己实现的暴力解法，O(N^2)
     * <pre>
     * m = a.len
     * for x := 0..m {
     *     i = x
     *     j = 0
     *     while (i not to end && j not to end) {
     *         if a[i] == b[j]
     *             i++,j++
     *             store substring
     *         else
     *             if same string terminates, then set i back to x, try to find among the rest
     *             j++
     *     }
     *     find longest substring in the iteration staring from x and all b
     * }
     * find longest substring in all 0..m starting char at string a
     *
     * repeat this on string b
     *
     * compare the two result return the longest
     * </pre>
     */
    public String longestCommonSubstring_BruteForce(String a, String b) {
        String longestBasedOnA = lcs_BruteForce(a, b);
        String longestBasedOnB = lcs_BruteForce(b, a);
        if (longestBasedOnA.length() > longestBasedOnB.length()) {
            return longestBasedOnA;
        }
        return longestBasedOnB;
    }

    public String lcs_BruteForce(String a, String b) {
        String longest = "";
        for (int m = 0; m < a.length(); m++) {
            int i = m;
            int j = 0;
            String longestSubString = "";
            String subString = "";
            State state = State.START;
            while (i < a.length() && j < b.length()) {
                if (a.charAt(i) != b.charAt(j)) {
                    if (state == State.MATCH) {
                        if (subString.length() > longestSubString.length()) {
                            longestSubString = subString;
                        }
                        state = State.START;
                        i = m;
                        subString = "";
                    }
                } else {
                    subString += a.charAt(i);
                    state = State.MATCH;
                    i++;
                }
                j++;
            }
            if (subString.length() > longestSubString.length()) {
                longestSubString = subString;
            }
            if (longestSubString.length() > longest.length()) {
                longest = longestSubString;
            }
        }
        return longest;
    }

    enum State {
        START,
        MATCH
    }

    /**
     * 和{@link LCS#longestCommonSubsequenceDP(String, String)}非常类似，只是当i>0,j>0并且Xi!=Yj时，需要归零，
     * 并且要记录最长子串的end和长度。时间复杂度 O(N*M)
     * <pre>
     *            0                               if i = 0 or j = 0
     * C[i, j] =  C[i - 1, j - 1] + 1             if i,j > 0 and Xi = Yj
     *            0                               if i,j > 0 and Xi != Yj
     * </pre>
     * <pre>
     * function LCSLength(X[1..m], Y[1..n])
     *     int max, end = 0
     *     C = array(0..m, 0..n)
     *     for i := 0..m
     *         C[i,0] = 0
     *     for j := 0..n
     *         C[0,j] = 0
     *     for i := 1..m
     *         for j := 1..n
     *             if X[i] = Y[j]
     *                 C[i,j] := C[i-1,j-1] + 1
     *                 if C[i, j] > max
     *                     max = C[i,j]
     *                     end = j
     *             else
     *                 C[i,j] := 0
     *     return Y.substring(end-max, end)
     * </pre>
     */
    public String longestCommonSubstring_DP(String a, String b) {
        if (a == null || b == null) {
            return "";
        }

        int lenA = a.length();
        int lenB = b.length();
        int[][] D = new int[lenA + 1][lenB + 1];  //新建大一维的数组
        for (int i = 0; i <= lenA; i++) {
            D[i][0] = 0;
        }
        for (int j = 0; j <= lenB; j++) {
            D[0][j] = 0;
        }

        int maxLen = 0;
        int end = 0;
        for (int i = 1; i <= lenA; i++) {
            for (int j = 1; j <= lenB; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    D[i][j] = D[i - 1][j - 1] + 1;
                    if (D[i][j] > maxLen) {
                        maxLen = D[i][j];
                        end = j;
                    }
                } else {
                    D[i][j] = 0;
                }
            }
        }

        // print 2D array
        System.out.println("    " + Arrays.toString(b.toCharArray()));
        for (int idx = 0; idx < D.length; idx++) {
            System.out.println((idx == 0 ? " " : a.charAt(idx - 1)) + Arrays.toString(D[idx]));
        }

        //return D[lenA][lenB];
        return b.substring(end - maxLen, end);
    }

    @Test
    public void test() {
        String a = "cnblogs";
        String b = "belongs";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("lo")); //lo
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("lo")); //lo

        a = "cnblhogs";
        b = "belongs";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("gs")); //gs
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("gs")); //gs

        a = "abicabcd";
        b = "xabc";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("abc")); //abc
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("abc")); //abc

        a = "xabc";
        b = "abicabcd";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("abc")); //abc
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("abc")); //abc

        a = "xabciuoabc";
        b = "kfjhabciowabcikdabciuss";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("abciu")); //abc
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("abciu")); //abc

        a = "kfjhabciowabcikdabciuss";
        b = "xabciuoabc";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("abciu")); //abc
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("abciu")); //abc

        a = "abcd";
        b = "abcxabcde";
        assertThat(longestCommonSubstring_BruteForce(a, b), Matchers.is("abcd")); //abc
        assertThat(longestCommonSubstring_DP(a, b), Matchers.is("abcd")); //abc
    }

}
