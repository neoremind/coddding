package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given a list of words (without duplicates), please write a program that returns all concatenated words in the
 * given list of words.
 * <p>
 * A concatenated word is defined as a string that is comprised entirely of at least two shorter words in the given
 * array.
 * <p>
 * Example:
 * Input: ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
 * <p>
 * Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
 * <p>
 * Explanation: "catsdogcats" can be concatenated by "cats", "dog" and "cats";
 * "dogcatsdog" can be concatenated by "dog", "cats" and "dog";
 * "ratcatdogcat" can be concatenated by "rat", "cat", "dog" and "cat".
 * Note:
 * The number of elements of the given array will not exceed 10,000
 * The length sum of elements in the given array will not exceed 600,000.
 * All the input string will only include lower case letters.
 * The returned elements order does not matter.
 *
 * @author xu.zhang
 *         https://leetcode.com/problems/concatenated-words/description/
 */
public class ConcatenatedWords {

    /**
     * TLE
     */
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        List<String> wordList = new ArrayList<>(words.length);
        for (String word : words) {
            wordList.add(word);
        }
        Collections.sort(wordList, (w1, w2) -> (w1.length() - w2.length()));
        List<String> preWords = new ArrayList<>(words.length);
        List<String> res = new ArrayList<>();
        for (String word : wordList) {
            if (canBuild(word, preWords)) {
                res.add(word);
            }
            preWords.add(word);
        }
        return res;
    }

    boolean canBuild(String word, List<String> preWords) {
        if (preWords.isEmpty()) return false;
        boolean[] dp = new boolean[word.length() + 1];
        dp[0] = true;
        for (int i = 0; i < word.length(); i++) {
            if (!dp[i]) continue;
            for (String w : preWords) {
                int end = i + w.length();
                if (end <= word.length() && word.substring(i, end).equals(w)) {
                    dp[end] = true;
                }
            }
        }
        return dp[word.length()];
    }

    /**
     * AC
     */
    public List<String> findAllConcatenatedWordsInADict2(String[] words) {
        List<String> wordList = new ArrayList<>(words.length);
        for (String word : words) {
            wordList.add(word);
        }
        Collections.sort(wordList, (w1, w2) -> (w1.length() - w2.length()));
        Set<String> preWords = new HashSet<>(words.length);
        List<String> res = new ArrayList<>();
        for (String word : wordList) {
            if (canBuild2(word, preWords)) {
                res.add(word);
            }
            preWords.add(word);
        }
        return res;
    }

    boolean canBuild2(String s, Set<String> preWords) {
        if (preWords.isEmpty()) return false;
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && preWords.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    @Test
    public void test() {
        assertThat(findAllConcatenatedWordsInADict(new String[]{"cat", "cats", "catsdogcats", "dog",
                "dogcatsdog", "hippopotamuses", "rat", "ratcatdogcat"}), is(Lists.newArrayList("catsdogcats",
                "dogcatsdog", "ratcatdogcat")));
    }
}
