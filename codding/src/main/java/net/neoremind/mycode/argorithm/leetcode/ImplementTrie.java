package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.LinkedList;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement a trie with insert, search, and startsWith methods.
 * <p>
 * You may assume that all inputs are consist of lowercase letters a-z.
 * <p>
 * 参考了http://www.sanfoundry.com/java-program-implement-trie/
 * <p>
 * 内部使用{@link LinkedList}来存储子树
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/implement-trie-prefix-tree/
 * @see net.neoremind.mycode.argorithm.tree.Trie
 */
public class ImplementTrie {

    @Test
    public void test() {
        Trie trie = new Trie();
        trie.insert("abc");
        trie.insert("abd");
        trie.insert("ace");
        trie.insert("ab");
        trie.insert("ap");
        trie.insert("ko");

        assertThat(trie.search("abc"), Matchers.is(true));
        assertThat(trie.search("abd"), Matchers.is(true));
        assertThat(trie.search("ace"), Matchers.is(true));
        assertThat(trie.search("ab"), Matchers.is(true));
        assertThat(trie.search("ap"), Matchers.is(true));
        assertThat(trie.search("ko"), Matchers.is(true));
        assertThat(trie.search("abi"), Matchers.is(false));
        assertThat(trie.search("ax"), Matchers.is(false));
        assertThat(trie.search("o"), Matchers.is(false));
        assertThat(trie.startsWith("ab"), Matchers.is(true));
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
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            TrieNode node = curr.subTrieNode(c);
            if (node == null) {
                return false;
            }
            curr = node;
        }
        return curr != null && curr.isEnd;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return false;
        }
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            TrieNode node = curr.subTrieNode(c);
            if (node == null) {
                return false;
            }
            curr = node;
        }
        return curr != null;
    }
}
