package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Given a 2D matrix matrix, find the sum of the elements inside the rectangle defined by its upper left corner
 * (row1, col1) and lower right corner (row2, col2).
 * <p>
 * Range Sum Query 2D
 * The above rectangle (with the red border) is defined by (row1, col1) = (2, 1) and (row2, col2) = (4, 3), which
 * contains sum = 8.
 * <p>
 * Example:
 * Given matrix = [
 * [3, 0, 1, 4, 2],
 * [5, 6, 3, 2, 1],
 * [1, 2, 0, 1, 5],
 * [4, 1, 0, 1, 7],
 * [1, 0, 3, 0, 5]
 * ]
 * <p>
 * sumRegion(2, 1, 4, 3) -> 8
 * sumRegion(1, 1, 2, 2) -> 11
 * sumRegion(1, 2, 2, 4) -> 12
 * Note:
 * You may assume that the matrix does not change.
 * There are many calls to sumRegion function.
 * You may assume that row1 ≤ row2 and col1 ≤ col2.
 * <p>
 * <p>
 * <pre>
 *     Construct a 2D array sums[row+1][col+1]
 *
 * (notice: we add additional blank row sums[0][col+1]={0} and blank column sums[row+1][0]={0} to remove the edge
 * case checking), so, we can have the following definition
 *
 * sums[i+1][j+1] represents the sum of area from matrix[0][0] to matrix[i][j]
 *
 * To calculate sums, the ideas as below
 *
 * +-----+-+-------+     +--------+-----+     +-----+---------+     +-----+--------+
 * |     | |       |     |        |     |     |     |         |     |     |        |
 * |     | |       |     |        |     |     |     |         |     |     |        |
 * +-----+-+       |     +--------+     |     |     |         |     +-----+        |
 * |     | |       |  =  |              |  +  |     |         |  -  |              |
 * +-----+-+       |     |              |     +-----+         |     |              |
 * |               |     |              |     |               |     |              |
 * |               |     |              |     |               |     |              |
 * +---------------+     +--------------+     +---------------+     +--------------+
 *
 * sums[i][j]      =    sums[i-1][j]    +     sums[i][j-1]    -   sums[i-1][j-1]   +
 *
 * matrix[i-1][j-1]
 * So, we use the same idea to find the specific area's sum.
 *
 * +---------------+   +--------------+   +---------------+   +--------------+   +--------------+
 * |               |   |         |    |   |   |           |   |         |    |   |   |          |
 * |   (r1,c1)     |   |         |    |   |   |           |   |         |    |   |   |          |
 * |   +------+    |   |         |    |   |   |           |   +---------+    |   +---+          |
 * |   |      |    | = |         |    | - |   |           | - |      (r1,c2) | + |   (r1,c1)    |
 * |   |      |    |   |         |    |   |   |           |   |              |   |              |
 * |   +------+    |   +---------+    |   +---+           |   |              |   |              |
 * |        (r2,c2)|   |       (r2,c2)|   |   (r2,c1)     |   |              |   |              |
 * +---------------+   +--------------+   +---------------+   +--------------+   +--------------+
 * </pre>
 *
 * @author xu.zhang
 */
public class RangeSumQuery2DImmutable {

    class NumMatrix {
        int[][] dp;

        public NumMatrix(int[][] matrix) {
            if (matrix.length == 0) {
                return;
            }
            int m = matrix.length;
            int n = matrix[0].length;
            dp = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + matrix[i - 1][j - 1];
                }
            }
//        for (int[] row : dp) {
//            System.out.println(Arrays.toString(row));
//        }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            if (dp == null) {
                return 0;
            }
            int iMin = Math.min(row1, row2) + 1;
            int iMax = Math.max(row1, row2) + 1;
            int jMin = Math.min(col1, col2) + 1;
            int jMax = Math.max(col1, col2) + 1;
            //System.out.println(iMin + " " + iMax + " " + jMin + " " + jMax);
            //System.out.println(dp[iMax][jMax] + " " + dp[iMin][jMax] + " " + dp[iMax][jMin] + " " + dp[iMin][jMin]);
            return dp[iMax][jMax] - dp[iMin - 1][jMax] - dp[iMax][jMin - 1] + dp[iMin - 1][jMin - 1];
        }
    }

    @Test
    public void test() {
        int[][] nums = new int[][]{
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}
        };
        RangeSumQuery2DImmutable.NumMatrix numMatrix = new RangeSumQuery2DImmutable.NumMatrix(nums);
        assertThat(numMatrix.sumRegion(2, 1, 4, 3), Matchers.is(8));
        assertThat(numMatrix.sumRegion(1, 1, 2, 2), Matchers.is(11));
        assertThat(numMatrix.sumRegion(1, 2, 2, 4), Matchers.is(12));
    }

}
