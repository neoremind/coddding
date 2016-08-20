package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * N皇后问题解法数量
 * <p>
 * leetcode上8ms的水平
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/n-queens-ii/
 */
public class NQueensII {

    int count = 0;

    public int totalNQueens(int n) {
        count = 0;
        char[][] chessboard = new char[n][n];
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[0].length; j++) {
                chessboard[i][j] = '.';
            }
        }
        solveNQueens(chessboard, 0);
        return count;
    }

    public void solveNQueens(char[][] chessboard, int n) {
        if (n == chessboard.length) {
            count++;
        } else {
            for (int i = 0; i < chessboard[0].length; i++) {
                if (isValid(chessboard, n, i)) {
                    chessboard[n][i] = 'Q';
                    solveNQueens(chessboard, n + 1);
                    chessboard[n][i] = '.';
                }
            }
        }
    }

    public boolean isValid(char[][] chessboard, int x, int y) {
        int rowLen = chessboard.length;
        int colLen = chessboard[0].length;
        // check single column
        for (int i = 0; i < chessboard.length; i++) {
            if (chessboard[i][y] == 'Q') {
                return false;
            }
        }
        // no need to check single row
        // check 45 diagonal
        for (int i = x, j = y; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = x, j = y; i < rowLen && j < colLen; i++, j++) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        // check 135 diagonal
        for (int i = x, j = y; i < rowLen && j >= 0; i++, j--) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = x, j = y; i >= 0 && j < colLen; i--, j++) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }

        return true;
    }

    @Test
    public void test() {
        assertThat(totalNQueens(4), Matchers.is(2));
        assertThat(totalNQueens(5), Matchers.is(10));
        assertThat(totalNQueens(6), Matchers.is(4));
        assertThat(totalNQueens(7), Matchers.is(40));
        assertThat(totalNQueens(8), Matchers.is(92));
        assertThat(totalNQueens(9), Matchers.is(352));
        assertThat(totalNQueens(10), Matchers.is(724));
        assertThat(totalNQueens(11), Matchers.is(2680));
        assertThat(totalNQueens(12), Matchers.is(14200));
        assertThat(totalNQueens(13), Matchers.is(73712));
        assertThat(totalNQueens(14), Matchers.is(365596));  // so slow....
    }

}
