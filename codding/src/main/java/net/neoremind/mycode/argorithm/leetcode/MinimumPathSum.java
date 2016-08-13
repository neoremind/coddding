package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes the
 * sum of all numbers along its path.
 * <p>
 * Note: You can only move either down or right at any point in time.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/minimum-path-sum/
 */
public class MinimumPathSum {

    public int minPathSum(int[][] grid) {
        // s[i][j] = Min{s[i - 1][j] + s[i][j - 1]} + grid[i][j]
        int m = grid.length; // row
        int n = grid[0].length; // column
        int[][] s = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j != 0) {
                    s[i][j] = s[i][j - 1] + grid[i][j];
                } else if (i != 0 && j == 0) {
                    s[i][j] = s[i - 1][j] + grid[i][j];
                } else if (i == 0 && j == 0) {
                    s[i][j] = grid[i][j];
                } else {
                    s[i][j] = Math.min(s[i - 1][j], s[i][j - 1]) + grid[i][j];
                }
            }
        }
        Arrays.asList(s).stream().forEach(e -> System.out.println(Arrays.toString(e)));
        return s[m - 1][n - 1];
    }

    @Test
    public void test() {
        int[][] grid = new int[][] {
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 2, 3, 4}
        };
        assertThat(minPathSum(grid), Matchers.is(12));
    }

}
