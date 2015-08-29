package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Determine if a Sudoku is valid, according to: Sudoku Puzzles - The Rules.
 * <p/>
 * The Sudoku board could be partially filled, where empty cells are filled with the character '.'.
 * <p/>
 * There are just 3 rules to Sudoku.
 * <ul>
 * <li>Each row must have the numbers 1-9 occuring just once.</li>
 * <li>Each column must have the numbers 1-9 occuring just once.</li>
 * <li>And the numbers 1-9 must occur just once in each of the 9 sub-boxes of the grid.</li>
 * </ul>
 * <p/>
 * 这道题是Sudoku Solver的一个子问题，在解数独的时候我们需要验证当前数盘是否合法。其实思路比较简单，也就是用brute
 * force。对于每一行，每一列，每个九宫格进行验证，总共需要27次验证，每次看九个元素。所以时间复杂度就是O(3*n^2), n=9。代码如下：
 *
 * @author zhangxu
 */
public class ValidSudoku {

    public boolean isValidSudoku(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            return false;
        }

        System.out.println("Check rows...");
        for (int i = 0; i < 9; i++) {
            boolean[] map = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    if (map[(int) (board[i][j] - '1')] == true) {
                        return false;
                    }
                    map[(int) (board[i][j] - '1')] = true;
                }
            }
        }

        System.out.println("Check columns...");
        for (int i = 0; i < 9; i++) {
            boolean[] map = new boolean[9];
            for (int j = 0; j < 9; j++) {
                if (board[j][i] != '.') {
                    System.out.println(j + "\t" + i);
                    if (map[(int) (board[j][i] - '1')] == true) {
                        return false;
                    }
                    map[(int) (board[j][i] - '1')] = true;
                }
            }
        }

        System.out.println("Check 3*3 grid...");

        /**
         * 遍历检测grid顺序：
         * 第一行3个block：
         * 00 01 02 10 11 12 20 21 22
         * 03 04 05 13 14 15 23 24 25
         * 06 07 08 16 17 18 26 27 28
         *
         * 第二行3个block：
         * 30 31 32 40 41 42 50 51 52
         * 33 34 35 43 44 45 53 54 55
         * 36 37 38 46 47 48 56 57 58
         *
         * ...
         */

        for (int block = 0; block < 9; block++) {
            boolean[] map = new boolean[9];
            for (int i = block / 3 * 3; i < block / 3 * 3 + 3; i++) {
                for (int j = block % 3 * 3; j < block % 3 * 3 + 3; j++) {
                    if (board[i][j] != '.') {
                        if (map[(int) (board[i][j] - '1')]) {
                            return false;
                        }
                        map[(int) (board[i][j] - '1')] = true;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void testIsValidSudoku() {
        char[][] board = {
                {'.', '.', '4', '.', '.', '.', '6', '3', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'5', '.', '.', '.', '.', '.', '.', '9', '.'},
                {'.', '.', '.', '5', '6', '.', '.', '.', '.'},
                {'4', '.', '3', '.', '.', '.', '.', '.', '1'},
                {'.', '.', '.', '7', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '2', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.'},
        };
        assertThat(new ValidSudoku().isValidSudoku(board), Matchers.is(true));
    }

}
