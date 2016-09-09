package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a string S and a string T, find the minimum window in S which will contain all the characters in T in
 * complexity O(n).
 * <p>
 * For example,
 * S = "ADOBECODEBANC"
 * T = "ABC"
 * Minimum window is "BANC".
 * <p>
 * Note:
 * If there is no such window in S that covers all characters in T, return the empty string "".
 * <p>
 * If there are multiple such windows, you are guaranteed that there will always be only one unique minimum window in S.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/minimum-window-substring/
 */
public class MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        System.out.println(s);
        System.out.println(t);
        int start = 0, end = 0;
        int counter = t.length();
        int[] a = new int[256];
        for (char c : t.toCharArray()) {
            a[c]++;
        }
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;
        while (end < s.length()) {
            if (a[s.charAt(end)] > 0) {
                System.out.println(
                        String.format("end=%d, s.charAt(%d)=%s, a[%s]=%d", end, end,
                                s.charAt(end), s.charAt(end), a[s.charAt(end)]));
                counter--;
            }
            a[s.charAt(end)]--;
            System.out.println(
                    String.format("end=%d, s.charAt(%d)=%s, a[%s]=%d", end, end,
                            s.charAt(end), s.charAt(end), a[s.charAt(end)]));
            end++;
            while (counter == 0) {
                if (minLen > end - start) {
                    minStart = start;
                    minLen = end - start;
                    System.out.println(
                            String.format("minStart=%d, minLen=%d, valid=%s", minStart, minLen, s.substring(minStart,
                                    minStart + minLen)));
                }
                a[s.charAt(start)]++;
                if (a[s.charAt(start)] > 0) {
                    counter++;
                }
                start++;
                System.out.println(
                        String.format("start=%d, end=%d, maybe_valid=%s", start, end, s.substring(start, end)));
            }
        }
        System.out.println(minStart);
        System.out.println(minLen);
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }

    @Test
    public void test() {
        assertThat(minWindow("ADOBECODEBANC", "ABC"), Matchers.is("BANC"));
    }

}
