package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Given a string s and a dictionary of words dict, add spaces in s to construct a sentence where each word is a
 * valid dictionary word.
 * <p>
 * Return all such possible sentences.
 * <p>
 * For example, given
 * s = "catsanddog",
 * dict = ["cat", "cats", "and", "sand", "dog"].
 * <p>
 * A solution is ["cats and dog", "cat sand dog"].
 * <p>
 * https://leetcode.com/problems/word-break-ii/
 *
 * @author zhangxu
 */
public class WordBreakII {

    public List<String> wordBreak(String s, Set<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        StepWrapper[] record = new StepWrapper[s.length() + 1];
        dp[0] = true;
        for (int i = 0; i < s.length(); i++) {
            if (!dp[i]) {
                continue;
            }
            for (String word : wordDict) {
                int end = i + word.length();
                if (end <= s.length() && s.substring(i, end).equals(word)) {
                    dp[end] = true;
                    if (record[end] == null) {
                        record[end] = new StepWrapper();
                    }
                    record[end].steps.add(new Step(record[i], word, i, end));
                }
            }
        }
        if (!dp[s.length()]) {
            return Collections.emptyList();
        }
        List<String> res = new ArrayList<>();
        backtrack(record[s.length()], res, new ArrayList<>());
        return res;
    }

    void backtrack(StepWrapper s, List<String> res, List<String> temp) {
        if (s == null) {
            List<String> copy = new ArrayList<>(temp);
            Collections.reverse(copy);
            res.add(copy.stream().collect(Collectors.joining(" ")));
        } else {
            for (Step step : s.steps) {
                temp.add(step.str);
                backtrack(step.prev, res, temp);
                temp.remove(temp.size() - 1);
            }
        }
    }

    class StepWrapper {
        List<Step> steps = new ArrayList<>();
    }

    class Step {
        StepWrapper prev;
        String str;
        int start;
        int end;

        public Step(StepWrapper prev, String str, int start, int end) {
            this.prev = prev;
            this.str = str;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "Step{" +
                    "str='" + str + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    public List<String> wordBreakDFS(String s, Set<String> wordDict) {
        return DFS(s, wordDict, new HashMap<String, LinkedList<String>>());
    }

    List<String> DFS(String s, Set<String> wordDict, HashMap<String, LinkedList<String>> map) {
        if (map.containsKey(s)) {
            return map.get(s);
        }

        LinkedList<String> res = new LinkedList<>();
        if (s.length() == 0) {
            res.add("");
            return res;
        }
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                List<String> sublist = DFS(s.substring(word.length()), wordDict, map);
                for (String sub : sublist) {
                    res.add(word + (sub.isEmpty() ? "" : " ") + sub);
                }
            }
        }
        map.put(s, res);
        return res;
    }

    @Test
    public void test() {
        String s = "catsanddog";
        Set<String> wordDict = Sets.newHashSet("cat", "cats", "and", "sand", "dog");
        System.out.println(wordBreak(s, wordDict));
        System.out.println(wordBreakDFS(s, wordDict));

//        s = "todayisagoodday";
//        wordDict = Sets.newHashSet("today", "cereal", "breakfast", "day", "is", "tv", "good", "z", "a");
//        System.out.println(wordBreak(s, wordDict));
//        System.out.println(wordBreakDFS(s, wordDict));
//
//        s =
//                "aaaaaaaaaaaaaaaaaaaaaaaab";
//        wordDict = Sets.newHashSet("a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa",
//                "aaaaaaaaab");
//        System.out.println(wordBreak(s, wordDict));
//        System.out.println(wordBreakDFS(s, wordDict));

        s = "a";
        wordDict = Sets.newHashSet();
        System.out.println(wordBreak(s, wordDict));
        System.out.println(wordBreakDFS(s, wordDict));
    }
}
