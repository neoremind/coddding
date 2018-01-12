package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Input:
 * s: "cbaebabacd" p: "abc"
 * <p>
 * Output:
 * [0, 6]
 * <p>
 * Explanation:
 * The substring with start index = 0 is "cba", which is an anagram of "abc".
 * The substring with start index = 6 is "bac", which is an anagram of "abc".
 *
 * @author xu.zhang
 */
public class FindAllAnagramsInAString {

    public List<Integer> findAnagrams(String s, String p) {
        int start = 0, end = 0;
        int counter = p.length();
        int[] a = new int[256];
        for (char c : p.toCharArray()) {
            a[c]++;
        }
        List<Integer> res = new ArrayList<>();
        while (end < s.length()) {
            if (a[s.charAt(end)] > 0) {
                counter--;
            }
            a[s.charAt(end)]--;
            end++;
            while (counter == 0) {
                if (end - start == p.length()) {
                    res.add(start);
                }
                a[s.charAt(start)]++;
                if (a[s.charAt(start)] > 0) {
                    counter++;
                }
                start++;
            }
        }
        return res;
    }

    @Test
    public void test() {
        assertThat(findAnagrams("cbaebabacd", "abc"), Matchers.is(Lists.newArrayList(0, 6)));
        assertThat(findAnagrams("abab", "ab"), Matchers.is(Lists.newArrayList(0, 1, 2)));
    }

}
