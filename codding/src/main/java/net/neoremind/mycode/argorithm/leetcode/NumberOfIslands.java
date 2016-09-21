package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. An island is surrounded by water
 * and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid
 * are all surrounded by water.
 * <p>
 * Example 1:
 * <p>
 * 11110
 * 11010
 * 11000
 * 00000
 * Answer: 1
 * <p>
 * Example 2:
 * <p>
 * 11000
 * 11000
 * 00100
 * 00011
 * Answer: 3
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/number-of-islands/
 */
public class NumberOfIslands {

    // dfs solution

    /**
     * 从左上角开始发现了1就用DFS扩张自己的零度，标记为visited，直到DFS退出，其实就是回溯。
     * <p>
     * 然后往下走，如果已经是visited或者是0就不管，遇到新的1证明有了新的领地。
     */
    public int numIslands(char[][] grid) {
        if (grid == null) {
            return 0;
        }
        int count = 0;
        int rows = grid.length;
        if (rows == 0) { //corner case
            return 0;
        }
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    dfs(grid, visited, i, j, rows, cols);
                    count++;
                }
            }
        }
        return count;
    }

    void dfs(char[][] grid, boolean[][] visited, int i, int j, int rows, int cols) {
        if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] == '0' || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        dfs(grid, visited, i + 1, j, rows, cols);
        dfs(grid, visited, i, j + 1, rows, cols);
        dfs(grid, visited, i - 1, j, rows, cols);
        dfs(grid, visited, i, j - 1, rows, cols);
    }

    // union find solution

    /**
     * union find solution
     * <p>
     * 时间复杂度：O(2NlogN)
     * 空间复杂度：O(N)
     */
    public int numIslands2(char[][] grid) {
        if (grid == null) {
            return 0;
        }
        int rows = grid.length;
        if (rows == 0) {
            return 0;
        }
        int cols = grid[0].length;
        UF uf = new UF(rows * cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    tryUnion(i, j, i + 1, j, rows, cols, uf, grid);
                    tryUnion(i, j, i - 1, j, rows, cols, uf, grid);
                    tryUnion(i, j, i, j + 1, rows, cols, uf, grid);
                    tryUnion(i, j, i, j - 1, rows, cols, uf, grid);
                }
            }
        }

        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    res.add(uf.find(i * cols + j));
                }
            }
        }
        return res.size();
    }

    void tryUnion(int i, int j, int x, int y, int rows, int cols, UF uf, char[][] grid) {
        if (i < 0 || i >= rows || j < 0 || j >= cols ||
                x < 0 || x >= rows || y < 0 || y >= cols) {
            return;
        }
        if (grid[x][y] == '0') {
            return;
        }
        uf.union(i * cols + j, x * cols + y);
    }

    class UF {
        int s[];
        int w[];
        int count;

        public UF(int size) {
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
        char[][] grid = new char[][] {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        assertThat(numIslands(grid), Matchers.is(3));
        assertThat(numIslands2(grid), Matchers.is(3));

        grid = new char[][] {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };
        assertThat(numIslands(grid), Matchers.is(1));

        grid = new char[][] {
                {'1', '1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0', '0'},
                {'0', '0', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '1', '1'},
                {'1', '1', '0', '0', '0', '0'},
                {'1', '0', '0', '0', '0', '0'}
        };
        assertThat(numIslands(grid), Matchers.is(4));
    }
}
