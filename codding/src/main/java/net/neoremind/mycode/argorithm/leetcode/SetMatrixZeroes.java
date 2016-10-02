package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in place.
 * <p>
 * click to show follow up.
 * <p>
 * Follow up:
 * Did you use extra space?
 * A straight forward solution using O(mn) space is probably a bad idea.
 * A simple improvement uses O(m + n) space, but still not the best solution.
 * Could you devise a constant space solution?
 * <p>
 * https://leetcode.com/problems/set-matrix-zeroes/
 *
 * @author zhangxu
 */
public class SetMatrixZeroes {

    public void setZeroes(int[][] matrix) {
        // if matrix[i][j] == 0 then set matrix[i][0] and matrix[0][j] == 0 to indicate later.
        // but for matrix[0][0] this is double meaning, so allocate extra variable to indicate.
        boolean row0 = false;
        boolean col0 = false;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                col0 = true;
                break;
            }
        }
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[0][j] == 0) {
                row0 = true;
                break;
            }
        }

        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[0][j] = 0;
                    matrix[i][0] = 0;
                }
            }
        }

        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        if (row0) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[0][j] = 0;
            }
        }
        if (col0) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    @Test
    public void test() {
        int[][] matrix = new int[][] {
                {0, 0, 0, 5},
                {4, 3, 1, 4},
                {0, 1, 1, 4},
                {1, 2, 1, 3},
                {0, 0, 1, 1}
        };
        setZeroes(matrix);
    }
}
