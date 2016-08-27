package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
 * <p>
 * Integers in each row are sorted from left to right.
 * The first integer of each row is greater than the last integer of the previous row.
 * For example,
 * <p>
 * Consider the following matrix:
 * <p>
 * [
 * [1,   3,  5,  7],
 * [10, 11, 16, 20],
 * [23, 30, 34, 50]
 * ]
 * Given target = 3, return true.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/search-a-2d-matrix/
 */
public class SearchA2DMatrix {

    /**
     * 剑指offer当中的题目3：二维数组中的查询的变种。时间复杂度O(M+N)。
     * 可以用一个通用的方法：
     * 这个解法是从右上角开始找，如果target小于右上角的，则col--，如果大于右上角，则row++，直到数组越界或者找到target为止。
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int row = 0;
        int col = n - 1;
        while (row < m && col >= 0) {
            if (matrix[row][col] < target) {
                row++;
            } else if (matrix[row][col] > target) {
                col--;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean searchMatrix2(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int begin = 0;
        int end = m * n - 1;
        while (begin <= end) {
            int mid = (begin + end) >> 1;
            if (matrix[mid / n][mid % n] < target) {
                begin = mid + 1;
            } else if (matrix[mid / n][mid % n] > target) {
                end = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    @Test
    public void test() {
        int[][] matrix = new int[][] {{1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}};
        assertThat(searchMatrix(matrix, 11), Matchers.is(true));
        assertThat(searchMatrix(matrix, 29), Matchers.is(false));

        assertThat(11 / 2, Matchers.is(5));
        assertThat(14 / 4, Matchers.is(3));
        assertThat(searchMatrix2(matrix, 11), Matchers.is(true));
        assertThat(searchMatrix2(matrix, 29), Matchers.is(false));
    }
}
