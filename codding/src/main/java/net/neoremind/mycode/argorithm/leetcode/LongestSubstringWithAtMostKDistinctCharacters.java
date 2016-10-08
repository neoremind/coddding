package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Given a string, find the length of the longest substring T that contains at most k distinct characters.
 * <p>
 * For example, Given s = “eceba” and k = 2,
 * <p>
 * T is "ece" which its length is 3.
 * <p>
 * PAID
 * <p>
 * https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
 *
 * @author zhangxu
 */
public class LongestSubstringWithAtMostKDistinctCharacters {

    public int longestSubWithAtMostKDistinctChar(String s, int k) {
        int[] flag = new int[256];
        int counter = 0;
        int start = 0;
        char[] str = s.toCharArray();
        int maxLen = 0;
        for (int end = 0; end < str.length; end++) {
            if (flag[str[end]] == 0) {
                counter++;
            }
            flag[str[end]]++;
            while (counter > k) {
                flag[str[start]]--;
                if (flag[str[start]] == 0) {
                    counter--;
                }
                start++;
            }
            maxLen = Math.max(maxLen, end - start + 1);
        }
        return maxLen;
    }

    @Test
    public void test() {
        assertThat(longestSubWithAtMostKDistinctChar("cecb", 2), is(3));
        assertThat(longestSubWithAtMostKDistinctChar("abcdcddcfcdcdd", 2), is(6));
        assertThat(longestSubWithAtMostKDistinctChar("abcdcddcfcdcdd", 3), is(12));
        assertThat(longestSubWithAtMostKDistinctChar("abcdcddcfcdcddcd", 2), is(7));
    }

}
