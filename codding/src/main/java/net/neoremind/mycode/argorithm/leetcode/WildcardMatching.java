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
    public boolean isMatch2(String s, String p) {
        int i = 0;
        int j = 0;
        int starIdx = -1;
        int match = 0;
        while (i < s.length()) {
            if (j < p.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                i++;
                j++;
            } else if (j < p.length() && p.charAt(j) == '*') {
                starIdx = j;
                match = i;
                j++;
            } else if (starIdx != -1) {
                j = starIdx + 1;
                match++;
                i = match;
            } else {
                return false;
            }
        }
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
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
