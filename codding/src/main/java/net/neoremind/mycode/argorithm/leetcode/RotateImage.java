package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * You are given an n x n 2D matrix representing an image.
 * <p>
 * Rotate the image by 90 degrees (clockwise).
 * <p>
 * Follow up:
 * Could you do this in-place?
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/rotate-image/
 */
public class RotateImage {

    /**
     * clockwise rotate
     * first reverse up to down, then swap the symmetry
     * 1 2 3     7 8 9     7 4 1
     * 4 5 6  => 4 5 6  => 8 5 2
     * 7 8 9     1 2 3     9 6 3
     */
    public void rotateClockwise1(int[][] matrix) {
        for (int i = 0; i < matrix.length / 2; i++) {
            int upper = i;
            int lower = matrix.length - 1 - i;
            for (int j = 0; j < matrix[0].length; j++) {
                swap(matrix, upper, j, lower, j);
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                swap(matrix, i, j, j, i);
            }
        }
    }

    /**
     * 1 2 3     1 4 7     7 4 1
     * 4 5 6  => 2 5 8  => 8 5 2
     * 7 8 9     3 6 9     9 6 3
     */
    public void rotateClockwise2(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                swap(matrix, i, j, j, i);
            }
        }

        for (int i = 0; i < matrix[0].length / 2; i++) {
            int left = i;
            int right = matrix[0].length - 1 - i;
            for (int j = 0; j < matrix.length; j++) {
                swap(matrix, j, left, j, right);
            }
        }
    }

    /**
     * 1 2 3     3 2 1     3 6 9
     * 4 5 6  => 6 5 4  => 2 5 8
     * 7 8 9     9 8 7     1 4 7
     */
    public void rotateAntiClockwise(int[][] matrix) {
        for (int i = 0; i < matrix[0].length / 2; i++) {
            int left = i;
            int right = matrix[0].length - 1 - i;
            for (int j = 0; j < matrix.length; j++) {
                swap(matrix, j, left, j, right);
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                swap(matrix, i, j, j, i);
            }
        }
    }

    /**
     * 1 2 3     1 4 7     3 6 9
     * 4 5 6  => 2 5 8  => 2 5 8
     * 7 8 9     3 6 9     1 4 7
     */
    public void rotateAntiClockwise2(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                swap(matrix, i, j, j, i);
            }
        }

        for (int i = 0; i < matrix.length / 2; i++) {
            int upper = i;
            int lower = matrix.length - 1 - i;
            for (int j = 0; j < matrix[0].length; j++) {
                swap(matrix, upper, j, lower, j);
            }
        }
    }

    public void swap(int[][] matrix, int m, int n, int x, int y) {
        int tmp = matrix[m][n];
        matrix[m][n] = matrix[x][y];
        matrix[x][y] = tmp;
    }

    @Test
    public void test() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateClockwise1(matrix);
        assertThat(matrix[0], Matchers.is(new int[] {7, 4, 1}));

        matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateClockwise2(matrix);
        assertThat(matrix[0], Matchers.is(new int[] {7, 4, 1}));

        matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateAntiClockwise(matrix);
        assertThat(matrix[0], Matchers.is(new int[] {3, 6, 9}));

        matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateAntiClockwise2(matrix);
        assertThat(matrix[0], Matchers.is(new int[] {3, 6, 9}));
    }
}
