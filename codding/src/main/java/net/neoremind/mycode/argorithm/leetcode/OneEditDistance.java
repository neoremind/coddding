package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * 161.One Edit Distance
 * <p>
 * Given two strings S and T, determine if they are both one edit distance apart.
 *
 * @author xu.zhang
 */
public class OneEditDistance {

    public boolean isOneEditDistance(String s, String t) {
        if (Math.abs(s.length() - t.length()) > 1) {
            return false;
        }
        if (s.length() == t.length()) {
            int diff = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != t.charAt(i)) {
                    diff++;
                }
            }
            return diff == 1;
        } else if (s.length() > t.length()) {
            return helper(s, t);
        } else {
            return helper(t, s);
        }
    }

    /**
     * s is longer
     */
    private boolean helper(String s, String t) {
        for (int i = 0; i < t.length(); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                return s.substring(i + 1).equals(t.substring(i));
            }
        }
        return true;
    }

    @Test
    public void test() {
        assertThat(isOneEditDistance("abc", "abcde"), Matchers.is(false));
        assertThat(isOneEditDistance("abc", "abcd"), Matchers.is(true));
        assertThat(isOneEditDistance("abc", "axbc"), Matchers.is(true));
        assertThat(isOneEditDistance("axbc", "abc"), Matchers.is(true));
        assertThat(isOneEditDistance("abc", "abc"), Matchers.is(false));
    }
}
