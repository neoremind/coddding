package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author zhangxu
 * @see https://leetcode.com/problems/sudoku-solver/
 */
public class SudokuSolver {

    public void solveSudoku(char[][] board) {
        if (!doSolveSudoku(board)) {
            throw new RuntimeException("No solution for the sudoku");
        }
    }

    public boolean doSolveSudoku(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    continue;
                } else {
                    for (int k = 1; k < 10; k++) {
                        board[i][j] = (char) ('0' + k);
                        if (isValid(board)) {
                            if (doSolveSudoku(board)) {
                                return true;
                            }
                        }
                        board[i][j] = '.';
                    }
                    return false;
                }
            }
        }
        return true;
    }

    ValidSudoku validSudoku = new ValidSudoku();

    public boolean isValid(char[][] board) {
        return validSudoku.isValidSudoku(board);
    }

    /**
     * <pre>
     * [1, 2, 4, 8, 5, 9, 6, 3, 7]
     * [3, 6, 9, 1, 2, 7, 4, 5, 8]
     * [5, 7, 8, 3, 4, 6, 1, 9, 2]
     * [2, 1, 7, 5, 6, 3, 8, 4, 9]
     * [4, 5, 3, 9, 8, 2, 7, 6, 1]
     * [8, 9, 6, 7, 1, 4, 3, 2, 5]
     * [6, 3, 1, 2, 7, 5, 9, 8, 4]
     * [7, 4, 2, 6, 9, 8, 5, 1, 3]
     * [9, 8, 5, 4, 3, 1, 2, 7, 6]
     * </pre>
     */
    @Test
    public void test() {
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
        solveSudoku(board);
        Arrays.asList(board).stream().forEach(e -> System.out.println(Arrays.toString(e)));
    }
}
