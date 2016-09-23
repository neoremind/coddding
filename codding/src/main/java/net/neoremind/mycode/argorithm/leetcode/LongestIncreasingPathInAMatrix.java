package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an integer matrix, find the length of the longest increasing path.
 * <p>
 * From each cell, you can either move to four directions: left, right, up or down. You may NOT move diagonally or
 * move outside of the boundary (i.e. wrap-around is not allowed).
 * <p>
 * Example 1:
 * <p>
 * nums = [
 * [9,9,4],
 * [6,6,8],
 * [2,1,1]
 * ]
 * Return 4
 * The longest increasing path is [1, 2, 6, 9].
 * <p>
 * Example 2:
 * <p>
 * nums = [
 * [3,4,5],
 * [3,2,6],
 * [2,2,1]
 * ]
 * Return 4
 * The longest increasing path is [3, 4, 5, 6]. Moving diagonally is not allowed.
 * <p>
 * https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 *
 * @author zhangxu
 */
public class LongestIncreasingPathInAMatrix {

    public int longestIncreasingPath(int[][] matrix) {
        // some preconditions…
        if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int m = matrix.length;  //row
        int n = matrix[0].length;  //col
        if (n == 0) {
            return 0;
        }
        int res = 1;
        int[][] records = new int[m][n]; //by default every element is 0
        // for each [i, j] in matrix, res = max(res, dfs(matrix, records));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res = Math.max(dfs(i, j, m, n, matrix, records), res);
            }
        }
        return res;
    }

    int dfs(int i, int j, int m, int n, int[][] matrix, int[][] records) {
        if (records[i][j] != 0) {
            return records[i][j];   // 这点很重要！类似动态规划的记忆式搜索，like a greedy snake, memorize the result
        }
        int res = 1;
        int[] d1 = new int[] {1, 0, -1, 0};
        int[] d2 = new int[] {0, 1, 0, -1};
        for (int x = 0; x < 4; x++) {
            int nextI = i + d1[x];
            int nextJ = j + d2[x];
            if (nextI >= 0 && nextI < m && nextJ >= 0 && nextJ < n &&
                    matrix[nextI][nextJ] > matrix[i][j]) {
                // 是+1不是+res这点写错了！！！
                res = Math.max(dfs(nextI, nextJ, m, n, matrix, records) + 1, res);
            }
        }
        records[i][j] = res;
        return res;
    }

    @Test
    public void test() {
        int[][] matrix = {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}};
        assertThat(longestIncreasingPath(matrix), Matchers.is(4));
    }
}
