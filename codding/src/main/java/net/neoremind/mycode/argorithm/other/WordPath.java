package net.neoremind.mycode.argorithm.other;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

/**
 * 一亩三分地里的Google面经。
 * <p>
 * 题目：一个单词的数字，每一个单词的转换到下一个单词可以在任意位置添加一个字母得到，问最长可以转换的单词的是哪个。
 * 例如，"a", "ab", "abc", "tabc", "tabqc", "tabpqc" 是一个转换路径，最长的单词就是tabpqc。
 * <p>
 * 采用DFS的思路，从每个单词出发，尝试寻找匹配其的下一个单词，分别遍历0-len的每个位置的26个字母的可能。
 * <p>
 * DFS就是backtracking，退出的条件是找不到下一个单词。
 * <p>
 * 下面的解法需要优化，因为对于某一个单词会重复的计算。所以TODO了。
 *
 * @author xu.zhang
 */
public class WordPath {

    public String wordPath(List<String> words) {
        TreeMap<Integer, List<String>> len2words = new TreeMap<>((w1, w2) -> w1 - w2);
        for (String word : words) {
            if (!len2words.containsKey(word.length())) {
                len2words.put(word.length(), new ArrayList<>());
            }
            len2words.get(word.length()).add(word);
        }
        List<List<String>> res = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : len2words.entrySet()) {
            for (String word : entry.getValue()) {
                List<String> temp = new ArrayList<>();
                temp.add(word);
                dfs(word, len2words, res, temp);
            }
        }
        res.forEach(System.out::println);
        return res.stream().map(l -> l.get(l.size() - 1)).sorted((e1, e2) -> Integer.compare(e2.length(), e1.length()
        )).collect(Collectors.toList()).get(0);
    }

    //TODO
    void dfs(String word, TreeMap<Integer, List<String>> len2words, List<List<String>> res,
             List<String> temp) {
        if (!len2words.containsKey(word.length() + 1)) {
            res.add(new ArrayList<>(temp));
            return;
        }
        boolean found = false;
        for (int i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                String newWord = word.substring(0, i) + (char) ('a' + j) + word.substring(i);
                if (len2words.get(newWord.length()).contains(newWord)) {
                    found = true;
                    temp.add(newWord);
                    dfs(newWord, len2words, res, temp);
                    temp.remove(temp.size() - 1);
                }
            }
        }
        if (!found) {
            res.add(new ArrayList<>(temp));
        }
    }

    @Test
    public void test() {
        List<String> words = Lists.newArrayList("a", "ab", "abc", "tabc", "tabqc", "tabpqc");
        assertThat(wordPath(words), Matchers.is("tabpqc"));

        words = Lists.newArrayList("a", "ab", "abc", "tabc", "tabqc", "tabpqc", "ah", "iah", "iajh",
                "ikajh", "oa", "opa", "qopa", "qopra", "qopras", "qoprasx");
        assertThat(wordPath(words), Matchers.is("qoprasx"));
    }
}
