package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a 2D board and a word, find if the word exists in the grid.
 * <p>
 * The word can be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those
 * horizontally or vertically neighboring. The same letter cell may not be used more than once.
 * <p>
 * For example,
 * Given board =
 * <p>
 * [
 * ['A','B','C','E'],
 * ['S','F','C','S'],
 * ['A','D','E','E']
 * ]
 * word = "ABCCED", -> returns true,
 * word = "SEE", -> returns true,
 * word = "ABCB", -> returns false.
 *
 * beat 66.57%
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/word-search/
 */
public class WordSearch {

    public boolean exist(char[][] board, String word) {
        if (word == null || word.length() == 0) {
            return false;
        }

        boolean[][] visited = new boolean[board.length][board[0].length];

        char[] words = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (backtrack(board, words, i, j, 0, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean backtrack(char[][] board, char[] words, int x, int y, int step, boolean[][] visited) {
        if (step == words.length) {
            return true;
        }
        if (x < 0 || x == board.length || y < 0 || y == board[0].length) {
            return false;
        }
        if (visited[x][y]) {
            return false;
        }
        if (words[step] != board[x][y]) {
            return false;
        }
        visited[x][y] = true;
        boolean isAnyOk = backtrack(board, words, x, y + 1, step + 1, visited) ||
                backtrack(board, words, x, y - 1, step + 1, visited) ||
                backtrack(board, words, x + 1, y, step + 1, visited) ||
                backtrack(board, words, x - 1, y, step + 1, visited);
        visited[x][y] = false;
        return isAnyOk;
    }

    public boolean exist2(char[][] board, String word) {
        if (word == null || word.length() == 0) return true;
        int row = board.length;
        int col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                boolean[][] visited = new boolean[row][col];
                if (dfs2(board, visited, word, row, col, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    int[][] d = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    boolean dfs2(char[][] board, boolean[][] visited, String word, int row, int col, int i, int j, int index) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= row || j < 0 || j >= col || visited[i][j]) {
            return false;
        }
        if (word.charAt(index) != board[i][j]) {
            return false;
        }
        visited[i][j] = true;
        for (int k = 0; k < 4; k++) {
            if (dfs2(board, visited, word, row, col, i + d[k][0], j + d[k][1], index + 1)) {
                return true;
            }
        }
        visited[i][j] = false;
        return false;
    }

    @Test
    public void test() {
        char[][] board = new char[][] {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        assertThat(exist(board, "A"), Matchers.is(true));
        assertThat(exist(board, "ABC"), Matchers.is(true));
        assertThat(exist(board, "ABCCED"), Matchers.is(true));
        assertThat(exist(board, "SEE"), Matchers.is(true));
        assertThat(exist(board, "ABCB"), Matchers.is(false));

        board = new char[][] {
                {'A'}
        };
        assertThat(exist(board, "A"), Matchers.is(true));

        board = new char[][] {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'E', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        assertThat(exist(board, "ABCESEEEFS"), Matchers.is(true));
    }

}
