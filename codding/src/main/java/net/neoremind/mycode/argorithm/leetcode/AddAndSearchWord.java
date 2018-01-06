package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class AddAndSearchWord {

    @Test
    public void test() {
        WordDictionary dict = new WordDictionary();
        dict.addWord("bad");
        dict.addWord("dad");
        dict.addWord("mad");
        assertThat(dict.search("pad"), Matchers.is(false));
        assertThat(dict.search("bad"), Matchers.is(true));
        assertThat(dict.search(".ad"), Matchers.is(true));
        assertThat(dict.search("b.."), Matchers.is(true));
    }

    class WordDictionary {

        Trie trie = new Trie();

        // Adds a word into the data structure.
        public void addWord(String word) {
            trie.insert(word);
        }

        // Returns if the word is in the data structure. A word could
        // contain the dot character '.' to represent any one letter.
        public boolean search(String word) {
            return trie.search(word);
        }
    }

    class TrieNode {
        char content;
        LinkedList<TrieNode> children = new LinkedList<>();
        boolean isEnd;
        int count;

        // Initialize your data structure here.
        public TrieNode() {
        }

        public TrieNode(char c) {
            this.content = c;
        }

        public TrieNode subTrieNode(char c) {
            for (TrieNode child : children) {
                if (child.content == c) {
                    return child;
                }
            }
            return null;
        }
    }

    class Trie {
        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        // Inserts a word into the trie.
        public void insert(String word) {
            if (word == null || word.length() == 0) {
                return;
            }
            TrieNode curr = root;
            for (char c : word.toCharArray()) {
                TrieNode node = curr.subTrieNode(c);
                if (node == null) {
                    node = new TrieNode(c);
                    curr.children.add(node);
                }
                node.count++;
                curr = node;
            }
            curr.isEnd = true;
        }

        // Returns if the word is in the trie.
        public boolean search(String word) {
            if (word == null || word.length() == 0) {
                return false;
            }
            return dfs(word.toCharArray(), 0, root);
        }

        // Returns if the word is in the trie.
        public boolean dfs(char[] str, int idx, TrieNode node) {
            if (idx >= str.length) {
                if (node.isEnd) {
                    return true;
                } else {
                    return false;
                }
            }
            char target = str[idx];
            if (target == '.') {
                for (TrieNode child : node.children) {
                    if (dfs(str, idx + 1, child)) {
                        return true;
                    }
                }
                return false;
            } else {
                TrieNode child = node.subTrieNode(target);
                if (child == null) {
                    return false;
                }
                return dfs(str, idx + 1, child);
            }
        }
    }

    /**
     * 徒手写的
     */
    class WordDictionary2 {

        class Node {
            char val;
            boolean isWord;
            Node[] children = new Node[26];

            public Node(char val) {
                this.val = val;
            }
        }

        Node root;

        /**
         * Initialize your data structure here.
         */
        public WordDictionary2() {
            root = new Node(' ');
        }

        /**
         * Adds a word into the data structure.
         */
        public void addWord(String word) {
            if (word == null || word.isEmpty()) {
                return;
            }
            char[] chars = word.toCharArray();
            Node node = root;
            for (char c : chars) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new Node(c);
                }
                node = node.children[c - 'a'];
            }
            node.isWord = true;
        }

        /**
         * Returns if the word is in the data structure. A word could contain the dot character '.' to represent any
         * one letter.
         */
        public boolean search(String word) {
            if (word == null || word.isEmpty()) {
                return false;
            }
            char[] chars = word.toCharArray();
            return dfs(root, chars, 0);
        }

        public boolean dfs(Node node, char[] chars, int index) {
            if (node == null) return false;
            if (index == chars.length) return node.isWord;
            char c = chars[index];
            if (c == '.') {
                for (int i = 0; i < 26; i++) {
                    if (dfs(node.children[i], chars, index + 1)) {
                        return true;
                    }
                }
                return false;
            } else {
                return dfs(node.children[c - 'a'], chars, index + 1);
            }
        }
    }

}
