package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Given a string s and a dictionary of words dict, determine if s can be segmented into a space-separated sequence
 * of one or more dictionary words.
 * <p>
 * For example, given
 * s = "leetcode",
 * dict = ["leet", "code"].
 * <p>
 * Return true because "leetcode" can be segmented as "leet code".
 * <p>
 * https://leetcode.com/problems/word-break/
 * <p>
 * 参考：http://www.programcreek.com/2012/12/leetcode-solution-word-break/
 *
 * @author zhangxu
 */
public class WordBreak {

    /**
     * brute force暴力
     * <p>
     * Time is O(n^2) and exceeds the time limit.
     */
    public boolean wordBreak(String s, Set<String> wordDict) {
        return dfs(s, wordDict, 0);
    }

    boolean dfs(String s, Set<String> wordDict, int start) {
        if (start == s.length()) {
            return true;
        }
        for (String word : wordDict) {
            int end = start + word.length();
            if (end <= s.length() && s.substring(start, end).equals(word)) {
                if (dfs(s, wordDict, end)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 这是最快的beat 78%
     * <p>
     * DP递推式：
     * boolean[] dp;
     * <p>
     * dp[i+1] = dp[j] && s.substring(j, i+1) is in dict, j in [0, i]
     * <p>
     * Time: O(string length * dict size).
     */
    public boolean wordBreakDP(String s, Set<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 0; i < s.length(); i++) {
            if (!dp[i]) {
                continue;
            }
            for (String word : wordDict) {
                int end = i + word.length();
                if (end <= s.length() && s.substring(i, end).equals(word)) {
                    dp[end] = true;
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * 代码简单但是稍慢beat 21%
     */
    public boolean wordBreakDP2(String s, Set<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    @Test
    public void test() {
        String s = "leetcode";
        Set<String> wordDict = Sets.newHashSet("leet", "code");
        assertThat(wordBreak(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP2(s, wordDict), Matchers.is(true));

        s = "todayisagoodday";
        wordDict = Sets.newHashSet("today", "cereal", "breakfast", "day");
        assertThat(wordBreak(s, wordDict), Matchers.is(false));
        assertThat(wordBreakDP(s, wordDict), Matchers.is(false));
        assertThat(wordBreakDP2(s, wordDict), Matchers.is(false));

        s = "todayisagoodday";
        System.out.println(s.length());
        wordDict = Sets.newHashSet("today", "cereal", "breakfast", "day", "is", "tv", "good", "z", "a");
        assertThat(wordBreak(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP2(s, wordDict), Matchers.is(true));

        s =
                "aaaaaaaaaaaaaaaaaaaaaaaab";
        wordDict = Sets.newHashSet("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa",
                "aaaaaaaaab");
        assertThat(wordBreak(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP2(s, wordDict), Matchers.is(true));

        // TLE for brute force dfs
        s =

                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        wordDict = Sets.newHashSet("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa",
                "aaaaaaaaa",
                "aaaaaaaaaa");
        //assertThat(wordBreak(s, wordDict), Matchers.is(true));
        assertThat(wordBreakDP(s, wordDict), Matchers.is(false));
        assertThat(wordBreakDP2(s, wordDict), Matchers.is(false));
    }
}
