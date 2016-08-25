package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement strStr().
 * <p>
 * Returns the index of the first occurrence of needle in haystack, or -1 if needle is not part of haystack.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/implement-strstr/
 */
public class StrStr {

    public int strStr(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (needle.length() > haystack.length()) {
            return -1;
        }
        char[] src = haystack.toCharArray();
        char[] target = needle.toCharArray();
        for (int i = 0; i < src.length - target.length + 1; i++) {
            int j = 0;
            while (j < target.length) {
                if (src[i + j] != target[j]) {
                    break;
                }
                j++;
            }
            if (j == target.length) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void test() {
        String haystack = "abcdefg";
        String needle = "cde";
        assertThat(strStr(haystack, needle), Matchers.is(2));
        assertThat(haystack.indexOf(needle), Matchers.is(2));

        haystack = "mississippi";
        needle = "a";
        assertThat(strStr(haystack, needle), Matchers.is(-1));
        assertThat(haystack.indexOf(needle), Matchers.is(-1));

        haystack = "aaa";
        needle = "aaa";
        assertThat(strStr(haystack, needle), Matchers.is(0));
        assertThat(haystack.indexOf(needle), Matchers.is(0));

        haystack = "";
        needle = "";
        assertThat(strStr(haystack, needle), Matchers.is(0));
        assertThat(haystack.indexOf(needle), Matchers.is(0));
    }
}
