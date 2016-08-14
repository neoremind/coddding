package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a string S, find the longest palindromic substring in S. You may assume that the maximum length of S is
 * 1000, and there exists one unique longest palindromic substring.
 * <p>
 * O(N^2)，从第一个char开始左右扩展，记录一个low和maxLen表示palindromic的字符
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/longest-palindromic-substring/
 */
public class LongestPalindromicSubstring {

    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (s.length() == 1) {
            return s;
        }
        int low = 0;
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            int k = i;
            int j = i;
            while (k < s.length() - 1 && s.charAt(k) == s.charAt(k + 1)) {
                k++; //skip duplicates
            }
            while (j > 0 && k < s.length() - 1 && s.charAt(j - 1) == s.charAt(k + 1)) {
                j--;
                k++;
            }

            if (k - j + 1 > maxLen) {
                maxLen = k - j + 1;
                low = j;
            }
        }
        return s.substring(low, low + maxLen);
    }

    @Test
    public void test() {
        String s = "ab";
        String str = longestPalindrome(s);
        assertThat(str, Matchers.is("a"));

        s = "abba";
        str = longestPalindrome(s);
        assertThat(str, Matchers.is("abba"));

        s = "xssdfybcacbuio";
        str = longestPalindrome(s);
        assertThat(str, Matchers.is("bcacb"));
    }

}
