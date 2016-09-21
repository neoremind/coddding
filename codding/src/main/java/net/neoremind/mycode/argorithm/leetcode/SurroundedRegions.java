package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a 2D board containing 'X' and 'O' (the letter O), capture all regions surrounded by 'X'.
 * <p>
 * A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * <p>
 * For example,
 * X X X X
 * X O O X
 * X X O X
 * X O X X
 * After running your function, the board should be:
 * <p>
 * X X X X
 * X X X X
 * X X X X
 * X O X X
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/surrounded-regions/
 */
public class SurroundedRegions {

    /**
     * 这种从里面找的DFS会TLE，需要从边界找
     *
     * @param board
     */
    public void solve(char[][] board) {
        if (board == null) {
            return;
        }
        int rows = board.length;
        if (rows == 0) { //corner case
            return;
        }
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O' && !visited[i][j]) {
                    dfsIsReachBoundary(board, visited, i, j, rows, cols);
                }
            }
        }
    }

    boolean dfsIsReachBoundary(char[][] board, boolean[][] visited, int i, int j, int rows, int cols) {
        if (i < 0 || j < 0 || i >= rows || j >= cols) {
            return true;
        }
        if (board[i][j] == 'X' || visited[i][j]) {
            return false;
        }
        board[i][j] = 'X';
        visited[i][j] = true;
        boolean isReachBoundary = dfsIsReachBoundary(board, visited, i + 1, j, rows, cols) ||
                dfsIsReachBoundary(board, visited, i, j + 1, rows, cols) ||
                dfsIsReachBoundary(board, visited, i - 1, j, rows, cols) ||
                dfsIsReachBoundary(board, visited, i, j - 1, rows, cols);
        if (isReachBoundary) {
            board[i][j] = 'O';
        }
        return isReachBoundary;
    }

    public void solve2(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (i == 0 || i == board.length - 1 || j == 0 || j == board[0].length - 1) {
                    if (board[i][j] == 'O') {
                        dfs(i, j, board);
                    }
                }
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '*') {
                    board[i][j] = 'O';
                } else {
                    board[i][j] = 'X';
                }
            }
        }
        return;
    }

    private void dfs(int i, int j, char[][] board) {
        if (board[i][j] == 'X' || board[i][j] == '*') {
            return;
        }
        board[i][j] = '*';
        if (i + 1 < board.length) {
            dfs(i + 1, j, board);
        }
        if (i - 1 > 0) {
            dfs(i - 1, j, board);
        }
        if (j + 1 < board[0].length) {
            dfs(i, j + 1, board);
        }
        if (j - 1 > 0) {
            dfs(i, j - 1, board);
        }
    }

    public void solve3(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        int rows = board.length, columns = board[0].length;
        int[][] direction = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i == 0 || i == rows - 1 || j == 0 || j == columns - 1) && board[i][j] == 'O') {
                    Queue<Point> queue = new LinkedList<>();
                    board[i][j] = 'B';
                    queue.offer(new Point(i, j));
                    while (!queue.isEmpty()) {
                        Point point = queue.poll();
                        for (int k = 0; k < 4; k++) {
                            int x = direction[k][0] + point.x;
                            int y = direction[k][1] + point.y;
                            if (x >= 0 && x < rows && y >= 0 && y < columns && board[x][y] == 'O') {
                                board[x][y] = 'B';
                                queue.offer(new Point(x, y));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == 'B') {
                    board[i][j] = 'O';
                } else if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }
    }

    class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * union-find
     */
    public void solve4(char[][] board) {
        if (board == null) {
            return;
        }
        int m = board.length;
        int n = board[0].length;
        UF uf = new UF(m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tryUnion(i, j, i - 1, j, m, n, board, uf);
                tryUnion(i, j, i + 1, j, m, n, board, uf);
                tryUnion(i, j, i, j - 1, m, n, board, uf);
                tryUnion(i, j, i, j + 1, m, n, board, uf);
            }
        }
        Set<Integer> out = new HashSet<Integer>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0 || i == m - 1 || j == n - 1) {
                    if (board[i][j] == 'O') {
                        out.add(uf.find(i * n + j));
                    }
                }
            }
        }

        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (!out.contains(uf.find(i * n + j))) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    void tryUnion(int i, int j, int x, int y, int m, int n, char[][] board, UF uf) {
        if (x < 0 || x >= m || y < 0 || y >= n) {
            return;
        }
        if (board[x][y] == board[i][j]) {
            uf.union(x * n + y, i * n + j);
        }
    }

    class UF {
        int[] s;
        int[] w;
        int count;

        UF(int size) {
            s = new int[size];
            w = new int[size];
            for (int i = 0; i < size; i++) {
                s[i] = i;
                w[i] = 1;
            }
            count = size;
        }

        int find(int p) {
            while (p != s[p]) {
                p = s[p];
            }
            return p;
        }

        void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) {
                return;
            }
            if (w[rootP] < w[rootQ]) {
                s[rootP] = rootQ;
                w[rootQ] += w[rootP];
            } else {
                s[rootQ] = rootP;
                w[rootP] += w[rootQ];
            }
            count--;
        }

    }

    @Test
    public void test() {
        char[][] board = new char[][] {
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'X'},
                {'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X'}
        };
        solve(board);
        Arrays.asList(board).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));

        board = new char[][] {
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'X'},
                {'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X'}
        };
        solve2(board);
        Arrays.asList(board).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));

        board = new char[][] {
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'X'},
                {'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X'}
        };
        solve4(board);
        Arrays.asList(board).stream().forEach(e -> System.out.println(ArrayUtils.toString(e)));

    }

}
