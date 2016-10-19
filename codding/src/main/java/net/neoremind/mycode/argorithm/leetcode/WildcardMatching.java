package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement wildcard pattern matching with support for '?' and '*'.
 * <p>
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * <p>
 * The matching should cover the entire input string (not partial).
 * <p>
 * The function prototype should be:
 * bool isMatch(const char *s, const char *p)
 * <p>
 * Some examples:
 * isMatch("aa","a") → false
 * isMatch("aa","aa") → true
 * isMatch("aaa","aa") → false
 * isMatch("aa", "*") → true
 * isMatch("aa", "a*") → true
 * isMatch("ab", "?*") → true
 * isMatch("aab", "c*a*b") → false
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/wildcard-matching/
 */
//TODO
public class WildcardMatching {

    //Wrong answer
    public boolean isMatch(String s, String p) {
        int i = 0;
        int j = 0;
        int starIdx = -1;
        while (i < s.length()) {
            if (j < p.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                i++;
                j++;
            } else if (j < p.length() && p.charAt(j) == '*') {
                starIdx = j;
                j++;
            } else if (starIdx != -1) {
                i++;
            } else {
                return false;
            }
        }
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }
        return j == p.length();
    }

    // Correct answer

    /**
     * <pre>
     *     a b c d e f h h x  <= S
     *     a *           h x  <= P
     * </pre>
     * 过程如下，tow pointer，i，j
     * 1）S从头开始，P从头开始
     * 2）char相等或者P中是?，则i++，j++
     * 3）如果没有遇到任何*，就不动了，那么肯定是不match了，直接返回false
     * 4）如果遇到*，记录*的位置starIdx，那么j就一直固定在starIdx+1，等待这S中后面的某个char和其匹配
     * 5）如果S往后一直没有匹配starIdx+1，那么一直走
     * 6）直到一个匹配了，那么继续回到#1，i++，j++
     * 7）如果下一个字符又不匹配了，如上面的例子到了h h 和 h x，后面的h和x不匹配，那么这时候j返回starIdx+1，i从x的位置也就是iIdx+1开始
     * 8）这时候竟然匹配了，如#6，然后又回到了#1.
     * 9）最后如果j还剩，除非是*，那么比较j==P.len判断是否match。
     */
    public boolean isMatch2(String s, String p) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iIndex = -1;

        while (i < s.length()) {
            if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                ++i;
                ++j;
            } else if (j < p.length() && p.charAt(j) == '*') {
                starIndex = j;
                iIndex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iIndex + 1;
                iIndex++;
            } else {
                return false;
            }
        }

        while (j < p.length() && p.charAt(j) == '*') {
            ++j;
        }

        return j == p.length();
    }

    @Test
    public void test() {
        assertThat(isMatch("a", "aa"), Matchers.is(false));
        assertThat(isMatch("aa", "a"), Matchers.is(false));
        assertThat(isMatch("aa", "aa"), Matchers.is(true));
        assertThat(isMatch("aaa", "aa"), Matchers.is(false));
        assertThat(isMatch("aa", "*"), Matchers.is(true));
        assertThat(isMatch("aa", "a*"), Matchers.is(true));
        assertThat(isMatch("ab", "*a"), Matchers.is(false));
        assertThat(isMatch("ab", "?*"), Matchers.is(true));
        assertThat(isMatch("aab", "c*a*b"), Matchers.is(false));
        assertThat(isMatch2("abefcdgiescdfimde", "ab*cd?i*de"), Matchers.is(true));
    }
}
