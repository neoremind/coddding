package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Input: dictionary[] = {"GEEKS", "FOR", "QUIZ", "GO"};
 * boggle[][]   = {{'G','I','Z'},
 * {'U','E','K'},
 * {'Q','S','E'}};
 * isWord(str): returns true if str is present in dictionary
 * else false.
 * <p>
 * Output:  Following words of dictionary are present
 * GEEKS
 * QUIZ
 * <p>
 * http://www.geeksforgeeks.org/boggle-find-possible-words-board-characters/
 *
 * @author zhangxu
 * @see net.neoremind.mycode.argorithm.leetcode.WordSearchII
 */
//TODO 当words非常多的时候backtrack效率会很低，考虑使用更好的搜索算法，使用Trie Tree
public class BoggleGame {

    List<String> findWords(char[][] board, List<String> dict) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                helper(res, board, i, j, dict, IntStream.range(0, dict.size()).boxed().collect(Collectors.toList()), 0,
                        new boolean[board.length][board[0].length]);
            }
        }
        return res.stream().distinct().collect(Collectors.toList());
    }

    void helper(List<String> res, char[][] board, int i, int j, List<String> dict,
                List<Integer> prefixMatchWordIndex, int index, boolean[][] visited) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }
        if (prefixMatchWordIndex.size() == 0) {
            return;
        }
        if (visited[i][j]) {
            return;
        }
        List<Integer> newPrefixMatchWordIndex = new ArrayList<>();
        for (int m : prefixMatchWordIndex) {
            String str = dict.get(m);
            if (index < str.length() && str.charAt(index) == board[i][j]) {
                if (index == str.length() - 1) {
                    res.add(str);
                } else {
                    newPrefixMatchWordIndex.add(m);
                }
            }
        }
        visited[i][j] = true;
        int[] x = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] y = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int k = 0; k < 8; k++) {
            int newI = i + x[k];
            int newJ = j + y[k];
            helper(res, board, newI, newJ, dict, newPrefixMatchWordIndex, index + 1, visited);
        }
        visited[i][j] = false;
    }

    @Test
    public void test() {
        char[][] board = new char[][] {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        //List<String> dict = Lists.newArrayList("AFD", "ECCB", "EXY", " SFCEZ", "A");
        List<String> dict = Lists.newArrayList("AFD", "CCEB", "ECCB");
        List<String> res = findWords(board, dict);
        System.out.println(res);

        board = new char[][] {
                {'G', 'I', 'Z'},
                {'U', 'E', 'K'},
                {'Q', 'S', 'E'}
        };
        //List<String> dict = Lists.newArrayList("AFD", "ECCB", "EXY", " SFCEZ", "A");
        dict = Lists.newArrayList("GEEKS", "QUIZ", "ZUIZ");
        res = findWords(board, dict);
        System.out.println(res);
    }

}
