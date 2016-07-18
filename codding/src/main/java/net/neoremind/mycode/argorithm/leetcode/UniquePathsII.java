package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Follow up for "Unique Paths":
 * <p>
 * Now consider if some obstacles are added to the grids. How many unique paths would there be?
 * <p>
 * An obstacle and empty space is marked as 1 and 0 respectively in the grid.
 * <p>
 * For example,
 * There is one obstacle in the middle of a 3x3 grid as illustrated below.
 * <pre>
 * [
 * [0,0,0],
 * [0,1,0],
 * [0,0,0]
 * ]
 * </pre>
 * The total number of unique paths is 2.
 * <p>
 * Note: m and n will be at most 100.
 *
 * @author zhangxu
 * @see <a href="https://leetcode.com/problems/unique-paths-ii//">unique-paths-ii</a>
 * @see UniquePaths
 */
public class UniquePathsII {

    @Test
    public void test() {
        int[][] obstacleGrid = new int[][] {
                {1}
        };
        assertThat(uniquePathsWithObstacles(obstacleGrid), Matchers.is(0));

        obstacleGrid = new int[][] {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        assertThat(uniquePathsWithObstacles(obstacleGrid), Matchers.is(2));

        obstacleGrid = new int[][] {
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0}
        };
        assertThat(uniquePathsWithObstacles(obstacleGrid), Matchers.is(2));

        obstacleGrid = new int[][] {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0}
        };
        assertThat(uniquePathsWithObstacles(obstacleGrid), Matchers.is(0));
    }

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        System.out.println("obstacleGrid:");
        printGracefully(obstacleGrid);

        //Empty case
        if (obstacleGrid.length == 0) {
            return 0;
        }

        int rows = obstacleGrid.length;
        int cols = obstacleGrid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (obstacleGrid[i][j] == 1) {
                    obstacleGrid[i][j] = 0;
                } else if (i == 0 && j == 0) {
                    obstacleGrid[i][j] = 1;
                } else if (i == 0) {
                    obstacleGrid[i][j] = obstacleGrid[i][j - 1] * 1;  // 乘法用的很巧妙！！！
                    // For row 0, if there are no paths to left cell, then its 0,else 1
                } else if (j == 0) {
                    obstacleGrid[i][j] = obstacleGrid[i - 1][j] * 1;
                    // For col 0, if there are no paths to upper cell, then its 0,else 1
                } else {
                    obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
                }
            }
        }

        System.out.println("unique paths:");
        printGracefully(obstacleGrid);
        return obstacleGrid[rows - 1][cols - 1];
    }

    private void printGracefully(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(Arrays.toString(arr[i]));
        }
        System.out.println(StringUtils.repeat("*", 30));
    }
}
