package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * {@link ImplementTrie}对应的，参考了discuss的高分回答，不用linkedlist实现，而是利用只有26个字母的话构造一个Array来存储
 * 用char - 'a' 来定位数组的索引
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/implement-trie-prefix-tree/
 * @see https://discuss.leetcode.com/topic/19221/ac-java-solution-simple-using-single-array
 */
public class ImplementTrie2 {

    @Test
    public void test() {
        _Trie trie = new _Trie();
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

class _TrieNode {
    public char val;
    public boolean isWord;
    public _TrieNode[] children = new _TrieNode[26];

    public _TrieNode() {
    }

    _TrieNode(char c) {
        _TrieNode node = new _TrieNode();
        node.val = c;
    }
}

class _Trie {
    private _TrieNode root;

    public _Trie() {
        root = new _TrieNode();
        root.val = ' ';
    }

    public void insert(String word) {
        _TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) {
                ws.children[c - 'a'] = new _TrieNode(c);
            }
            ws = ws.children[c - 'a'];
        }
        ws.isWord = true;
    }

    public boolean search(String word) {
        _TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) {
                return false;
            }
            ws = ws.children[c - 'a'];
        }
        return ws.isWord;
    }

    public boolean startsWith(String prefix) {
        _TrieNode ws = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (ws.children[c - 'a'] == null) {
                return false;
            }
            ws = ws.children[c - 'a'];
        }
        return true;
    }
}
