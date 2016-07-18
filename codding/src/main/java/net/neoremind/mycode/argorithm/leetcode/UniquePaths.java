package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).
 * <p>
 * The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right
 * corner of the grid (marked 'Finish' in the diagram below).
 * <p>
 * How many possible unique paths are there?
 * <p>
 * Note: m and n will be at most 100.
 * <p>
 * 当m=0或者n=0时候表示在第一行或者第一列上，没法go left or up，所以这些步骤上总是1个可选择的路径。
 * 其他的cell利用动态规划的思想来完成。(i, j)这个cell上的路径数等于P[i][j]，要不然从上面的cell过来，
 * 要不然从左边的cell过来，所以路径数等于P[i - 1][j] + P[i][j - 1]。
 * <p>
 * [1]
 * *****************************
 * [1, 1]
 * [1, 2]
 * *****************************
 * [1, 1, 1]
 * [1, 2, 3]
 * [1, 3, 6]
 * *****************************
 * [1, 1, 1, 1]
 * [1, 2, 3, 4]
 * [1, 3, 6, 10]
 * [1, 4, 10, 20]
 * *****************************
 * [1, 1, 1, 1, 1, 1, 1]
 * [1, 2, 3, 4, 5, 6, 7]
 * [1, 3, 6, 10, 15, 21, 28]
 * *****************************
 *
 * @author zhangxu
 * @see <a href="https://leetcode.com/problems/unique-paths/">unique-paths</a>
 */
public class UniquePaths {

    @Test
    public void test() {
        assertThat(uniquePaths(1, 1), Matchers.is(1));
        assertThat(uniquePaths(2, 2), Matchers.is(2));
        assertThat(uniquePaths(3, 3), Matchers.is(6));
        assertThat(uniquePaths(4, 4), Matchers.is(20));
        assertThat(uniquePaths(3, 7), Matchers.is(28));
    }

    /**
     * @param m 行数
     * @param n 列数
     *
     * @return unique path number
     */
    public int uniquePaths(int m, int n) {
        int[][] arr = new int[m][n];
        for (int i = 0; i < m; i++) {
            arr[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            arr[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                arr[i][j] = arr[i][j - 1] + arr[i - 1][j];  // 上面+左面的cell路径和
            }
        }

        printGracefully(arr);
        return arr[m - 1][n - 1];
    }

    private void printGracefully(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(Arrays.toString(arr[i]));
        }
        System.out.println(StringUtils.repeat("*", 30));
    }
}
