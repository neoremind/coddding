package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Given an 2D board, count how many battleships are in it. The battleships are represented with 'X's, empty slots
 * are represented with '.'s. You may assume the following rules:
 * <p>
 * You receive a valid board, made of only battleships or empty slots.
 * Battleships can only be placed horizontally or vertically. In other words, they can only be made of the shape 1xN
 * (1 row, N columns) or Nx1 (N rows, 1 column), where N can be of any size.
 * At least one horizontal or vertical cell separates between two battleships - there are no adjacent battleships.
 * Example:
 * X..X
 * ...X
 * ...X
 * In the above board there are 2 battleships.
 * Invalid Example:
 * ...X
 * XXXX
 * ...X
 * This is an invalid board that you will not receive - as battleships will always have a cell separating between them.
 * Follow up:
 * Could you do it in one-pass, using only O(1) extra memory and without modifying the value of the board?
 *
 * @author xu.zhang
 */
public class BattleshipsInABoard {

    public int countBattleships(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X' && !visited[i][j]) {
                    //System.out.println(i + " " + j);
                    visited[i][j] = true;
                    helper(board, visited, i + 1, j, m, n, 0);
                    helper(board, visited, i - 1, j, m, n, 1);
                    helper(board, visited, i, j + 1, m, n, 2);
                    helper(board, visited, i, j - 1, m, n, 3);
                    count++;
                }
            }
        }
        return count;
    }

    void helper(char[][] board, boolean[][] visited, int i, int j, int m, int n, int dir) {
        if (i < 0 || i >= m || j < 0 || j >= n || board[i][j] == '.' || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        if (dir == 0) {
            helper(board, visited, i + 1, j, m, n, dir);
        } else if (dir == 1) {
            helper(board, visited, i - 1, j, m, n, dir);
        } else if (dir == 2) {
            helper(board, visited, i, j + 1, m, n, dir);
        } else {
            helper(board, visited, i, j - 1, m, n, dir);
        }
    }

    public int countBattleships2(char[][] board) {
        int m = board.length;
        if (m == 0) return 0;
        int n = board[0].length;

        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == '.') continue;
                if (i > 0 && board[i - 1][j] == 'X') continue;
                if (j > 0 && board[i][j - 1] == 'X') continue;
                count++;
            }
        }

        return count;
    }

    @Test
    public void test() {
        char[][] board = new char[][]{
                {'X', '.', '.', 'X'},
                {'.', '.', '.', 'X'},
                {'.', '.', '.', 'X'},
        };
        assertThat(countBattleships(board), Matchers.is(2));

        board = new char[][]{
                {'X', '.', '.', 'X'},
                {'.', 'X', '.', 'X'},
                {'.', 'X', '.', 'X'},
        };
        assertThat(countBattleships(board), Matchers.is(3));
    }
}
