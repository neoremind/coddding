package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
 * <p>
 * Integers in each row are sorted in ascending from left to right.
 * Integers in each column are sorted in ascending from top to bottom.
 * For example,
 * <p>
 * Consider the following matrix:
 * <p>
 * [
 * [1,   4,  7, 11, 15],
 * [2,   5,  8, 12, 19],
 * [3,   6,  9, 16, 22],
 * [10, 13, 14, 17, 24],
 * [18, 21, 23, 26, 30]
 * ]
 * Given target = 5, return true.
 * <p>
 * Given target = 20, return false.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/search-a-2d-matrix-ii/
 * @see SearchA2DMatrix
 */
public class SearchA2DMatrixII {

    /**
     * 剑指offer当中的题目3：二维数组中的查询。时间复杂度O(M+N)。
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

    boolean searchMatrixDAC(int[][] matrix, int stX, int stY, int edX, int edY, int target) {
        System.out.println(String.format("Search from (%d, %d) to (%d, %d)", stX, edX, stY, edY));
        if (stX >= edX || stY >= edY) { //可以是==
            return false;
        }

        int max = matrix[edX - 1][edY - 1];
        int min = matrix[stX][stY];

        // min <= target <= max
        if (min <= target && target <= max) {

            int mdX = (stX + edX) / 2;
            int mdY = (stY + edY) / 2;

            if (matrix[mdX][mdY] > target) {
                return searchMatrixDAC(matrix, stX, stY, mdX, mdY, target) ||
                        searchMatrixDAC(matrix, stX, mdY, mdX, edY, target) ||
                        searchMatrixDAC(matrix, mdX, stY, edX, mdY, target);
            } else if (matrix[mdX][mdY] < target) {
                return searchMatrixDAC(matrix, stX, mdY, mdX, edY, target) ||
                        searchMatrixDAC(matrix, mdX, stY, edX, mdY, target) ||
                        searchMatrixDAC(matrix, mdX, mdY, edX, edY, target);
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * 分治算法
     */
    public boolean searchMatrixDAC(int[][] matrix, int target) {
        return searchMatrixDAC(matrix, 0, 0, matrix.length, matrix[0].length, target);
    }

    @Test
    public void test() {
        int[][] matrix = new int[][] {{1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}};
        assertThat(searchMatrix(matrix, 5), Matchers.is(true));
        assertThat(searchMatrix(matrix, 11), Matchers.is(true));
        assertThat(searchMatrix(matrix, 29), Matchers.is(false));

        assertThat(5 / 2, Matchers.is(2));
        assertThat(searchMatrixDAC(matrix, 5), Matchers.is(true));
        assertThat(searchMatrixDAC(matrix, 11), Matchers.is(true));
        assertThat(searchMatrixDAC(matrix, 29), Matchers.is(false));
    }
}
