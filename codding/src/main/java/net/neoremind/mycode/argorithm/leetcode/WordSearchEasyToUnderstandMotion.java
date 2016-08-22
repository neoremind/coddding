package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

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
 * @author zhangxu
 * @see https://leetcode.com/problems/word-search/
 */
public class WordSearchEasyToUnderstandMotion {

    public boolean exist(char[][] board, String word) {
        if (word == null || word.length() == 0) {
            return false;
        }

        boolean[][] visited = new boolean[board.length][board[0].length];

        char[] words = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (backtrack(board, words, i, j, 0, visited).getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Result<Boolean> backtrack(char[][] board, char[] words, int x, int y, int step, boolean[][] visited) {
        if (step == words.length) {
            return ResultImpl.of(board, x, y, step, visited, true, ResultType.ALL_MATCH);
        }
        if (x < 0 || x == board.length || y < 0 || y == board[0].length) {
            return ResultImpl.of(board, x, y, step, visited, false, ResultType.OUT_OF_RANGE);
        }
        if (visited[x][y]) {
            return ResultImpl.of(board, x, y, step, visited, false, ResultType.VISISED);
        }
        if (words[step] != board[x][y]) {
            return ResultImpl.of(board, x, y, step, visited, false, ResultType.WORD_NOT_SAME_BREAK_HERE);
        }
        visited[x][y] = true;
        ResultImpl.of(board, x, y, step, visited, false, ResultType.MATCH).getValue();
        boolean isAnyOk = backtrack(board, words, x, y + 1, step + 1, visited).getValue() ||
                backtrack(board, words, x, y - 1, step + 1, visited).getValue() ||
                backtrack(board, words, x + 1, y, step + 1, visited).getValue() ||
                backtrack(board, words, x - 1, y, step + 1, visited).getValue();
        visited[x][y] = false;
        return ResultImpl.of(board, x, y, step, visited, isAnyOk, ResultType.MATCH);
    }

    static boolean SUCCESS = false;

    enum ResultType {

        ALL_MATCH("Search done and find successfully!"),
        MATCH("Match char, continue"),
        OUT_OF_RANGE("Goes out of range"),
        WORD_NOT_SAME_BREAK_HERE("Not match"),
        VISISED("Visited, step back..");

        ResultType(String desc) {
            this.desc = desc;
        }

        public String desc;
    }

    interface Result<T> {
        T getValue();
    }

    static class ResultImpl implements Result<Boolean> {
        public char[][] board;
        public int x;
        public int y;
        public int step;
        public boolean[][] visited;
        public Boolean value;
        public ResultType type;

        private ResultImpl(char[][] board, int x, int y, int step, boolean[][] visited, Boolean value,
                           ResultType type) {
            this.board = board;
            this.x = x;
            this.y = y;
            this.step = step;
            this.visited = visited;
            this.value = value;
            this.type = type;
        }

        public static Result<Boolean> of(char[][] board, int x, int y, int step, boolean[][] visited, Boolean value,
                                         ResultType type) {
            return new ResultImpl(board, x, y, step, visited, value, type);
        }

        @Override
        public Boolean getValue() {
            if (type == ResultType.ALL_MATCH) {
                SUCCESS = true;
            }
            if (SUCCESS) {
                System.out.println(type.desc + ". recursive stack size=" + step);
            } else {
                if (type == ResultType.OUT_OF_RANGE) {
                    System.out.println(type.desc + ". recursive stack size=" + step);
                } else {
                    System.out.println("curr={" + board[x][y] + "} " + type.desc + ". recursive stack size=" + step);
                }
            }
            for (int i = 0; i < board.length; i++) {
                StringBuilder row = new StringBuilder("[");
                for (int j = 0; j < board[0].length; j++) {
                    if (visited[i][j]) {
                        row.append((char) 27 + "[1m" + board[i][j] + (char) 27 + "[0m");
                    } else {
                        row.append(board[i][j]);
                    }
                    if (j != board[0].length - 1) {
                        row.append(",");
                    }
                }
                row.append("]");
                System.out.println(row);
            }
            try {
                Thread.sleep(3900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return value;
        }
    }

    @Test
    public void test() {
        try {
            System.out.println();
            Thread.sleep(5000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        char[][] board = new char[][] {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        //        assertThat(exist(board, "A"), Matchers.is(true));
        //        assertThat(exist(board, "ABC"), Matchers.is(true));
        //assertThat(exist(board, "ABCCED"), Matchers.is(true));
        assertThat(exist(board, "SEECFBC"), Matchers.is(true));
//        board = new char[][] {
//                {'A', 'B', 'C', 'E'},
//                {'S', 'F', 'E', 'S'},
//                {'A', 'D', 'E', 'E'}
//        };
//        assertThat(exist(board, "ABCESEEEFS"), Matchers.is(true));
    }

}
