package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

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

    @Test
    public void test() {
        char[][] grid = new char[][] {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}
        };
        assertThat(numIslands(grid), Matchers.is(3));

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
