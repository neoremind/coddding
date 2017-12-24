package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * Design a search autocomplete system for a search engine. Users may input a sentence (at least one word and end
 * with a special character '#'). For each character they type except '#', you need to return the top 3 historical
 * hot sentences that have prefix the same as the part of sentence already typed. Here are the specific rules:
 * <p>
 * The hot degree for a sentence is defined as the number of times a user typed the exactly same sentence before.
 * The returned top 3 hot sentences should be sorted by hot degree (The first is the hottest one). If several
 * sentences have the same degree of hot, you need to use ASCII-code order (smaller one appears first).
 * If less than 3 hot sentences exist, then just return as many as you can.
 * When the input is a special character, it means the sentence ends, and in this case, you need to return an empty
 * list.
 * Your job is to implement the following functions:
 * <p>
 * The constructor function:
 * <p>
 * AutocompleteSystem(String[] sentences, int[] times): This is the constructor. The input is historical data.
 * Sentences is a string array consists of previously typed sentences. Times is the corresponding times a sentence
 * has been typed. Your system should record these historical data.
 * <p>
 * Now, the user wants to input a new sentence. The following function will provide the next character the user types:
 * <p>
 * List<String> input(char c): The input c is the next character typed by the user. The character will only be
 * lower-case letters ('a' to 'z'), blank space (' ') or a special character ('#'). Also, the previously typed
 * sentence should be recorded in your system. The output will be the top 3 historical hot sentences that have prefix
 * the same as the part of sentence already typed.
 * <p>
 * Example:
 * Operation: AutocompleteSystem(["i love you", "island","ironman", "i love leetcode"], [5,3,2,2])
 * The system have already tracked down the following sentences and their corresponding times:
 * "i love you" : 5 times
 * "island" : 3 times
 * "ironman" : 2 times
 * "i love leetcode" : 2 times
 * Now, the user begins another search:
 * <p>
 * Operation: input('i')
 * Output: ["i love you", "island","i love leetcode"]
 * Explanation:
 * There are four sentences that have prefix "i". Among them, "ironman" and "i love leetcode" have same hot degree.
 * Since ' ' has ASCII code 32 and 'r' has ASCII code 114, "i love leetcode" should be in front of "ironman". Also we
 * only need to output top 3 hot sentences, so "ironman" will be ignored.
 * <p>
 * Operation: input(' ')
 * Output: ["i love you","i love leetcode"]
 * Explanation:
 * There are only two sentences that have prefix "i ".
 * <p>
 * Operation: input('a')
 * Output: []
 * Explanation:
 * There are no sentences that have prefix "i a".
 * <p>
 * Operation: input('#')
 * Output: []
 * Explanation:
 * The user finished the input, the sentence "i a" should be saved as a historical sentence in system. And the
 * following input will be counted as a new search.
 * <p>
 * Note:
 * <p>
 * The input sentence will always start with a letter and end with '#', and only one blank space will exist between
 * two words.
 * The number of complete sentences that to be searched won't exceed 100. The length of each sentence including those
 * in the historical data won't exceed 100.
 * Please use double-quote instead of single-quote when you write test cases even for a character input.
 * Please remember to RESET your class variables declared in class AutocompleteSystem, as static/class variables are
 * persisted across multiple test cases. Please see here for more details.
 * 题目大意：
 * 设计搜索自动完成系统
 * <p>
 * 包含如下两个方法：
 * <p>
 * 构造方法：
 * <p>
 * AutocompleteSystem(String[] sentences, int[] times): 输入句子sentences，及其出现次数times
 * <p>
 * 输入方法：
 * <p>
 * List<String> input(char c): 输入字符c可以是26个小写英文字母，也可以是空格，以'#'结尾。返回输入字符前缀对应频率最高的至多3个句子，频率相等时按字典序排列。
 *
 * @author xu.zhang
 */
public class DesignSearchAutocompleteSystem {

    class TrieNode {
        char value;
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();

        TrieNode(char value) {
            this.value = value;
        }
    }

    class Trie {
        TrieNode root;

        Trie() {
            this.root = new TrieNode(' ');
        }

        void add(String s, int times) {
            if (s == null || s.length() == 0) return;
            TrieNode curr = root;
            for (char c : s.toCharArray()) {
                if (!curr.children.containsKey(c)) {
                    curr.children.put(c, new TrieNode(c));
                }
                curr = curr.children.get(c);
                curr.counts.put(s, curr.counts.getOrDefault(s, 0) + times);
            }
        }

        TrieNode search(String prefix) {
            if (prefix == null || prefix.length() == 0) return null;
            TrieNode curr = root;
            for (char c : prefix.toCharArray()) {
                if (!curr.children.containsKey(c)) {
                    return null;
                }
                curr = curr.children.get(c);
            }
            return curr;
        }
    }

    class SearchAutocompleteSystem {

        Trie trie;

        public SearchAutocompleteSystem(String[] sentences, int[] times) {
            if (sentences == null || sentences.length == 0) return;
            trie = new Trie();
            for (int i = 0; i < sentences.length; i++) {
                trie.add(sentences[i], times[i]);
            }
        }

        String prefix = "";

        public List<String> input(char c) {
            if (c == '#') {
                trie.add(prefix, 1);
                prefix = "";
                return new ArrayList<>();
            }
            prefix = prefix + c;
            TrieNode node = trie.search(prefix);
            if (node == null) return new ArrayList<>();
            PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) ->
                    a.getValue().equals(b.getValue()) ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue()
            );
            for (Map.Entry<String, Integer> e : node.counts.entrySet()) {
                pq.add(e);
            }
            List<String> res = new ArrayList<>();
            for (int i = 0; !pq.isEmpty() && i < 3; i++) {
                res.add(pq.poll().getKey());
            }
            return res;
        }
    }

    @Test
    public void test() {
        String[] sentences = new String[]{"i love you", "island", "ironman", "i love leetcode"};
        int[] times = new int[]{5, 3, 2, 2};
        SearchAutocompleteSystem system = new SearchAutocompleteSystem(sentences, times);
        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "island", "i love leetcode")));
        assertThat(system.input(' '), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('l'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('o'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('v'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('e'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input(' '), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('y'), Matchers.is(Lists.newArrayList("i love you")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        sentences = new String[]{"i love you", "island", "ironman", "i love leetcode", "i want"};
        times = new int[]{5, 3, 2, 2, 1};
        system = new SearchAutocompleteSystem(sentences, times);
        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "island", "i love leetcode")));
        assertThat(system.input(' '), Matchers.is(Lists.newArrayList("i love you", "i love leetcode", "i want")));
        assertThat(system.input('l'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('o'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('v'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('e'), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input(' '), Matchers.is(Lists.newArrayList("i love you", "i love leetcode")));
        assertThat(system.input('y'), Matchers.is(Lists.newArrayList("i love you")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('x'), Matchers.is(Lists.newArrayList()));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('x'), Matchers.is(Lists.newArrayList("x")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "island", "i love leetcode")));
        assertThat(system.input('o'), Matchers.is(Lists.newArrayList()));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "island", "i love leetcode")));
        assertThat(system.input('o'), Matchers.is(Lists.newArrayList("io")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "island", "i love leetcode")));
        assertThat(system.input('o'), Matchers.is(Lists.newArrayList("io")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));

        assertThat(system.input('i'), Matchers.is(Lists.newArrayList("i love you", "io", "island")));
        assertThat(system.input('#'), Matchers.is(Lists.newArrayList()));
    }
}
