package net.neoremind.mycode.argorithm.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a matrix consists of 0 and 1, find the distance of the nearest 0 for each cell.
 * <p>
 * The distance between two adjacent cells is 1.
 * Example 1:
 * Input:
 * <p>
 * 0 0 0
 * 0 1 0
 * 0 0 0
 * Output:
 * 0 0 0
 * 0 1 0
 * 0 0 0
 * Example 2:
 * Input:
 * <p>
 * 0 0 0
 * 0 1 0
 * 1 1 1
 * Output:
 * 0 0 0
 * 0 1 0
 * 1 2 1
 * Note:
 * The number of elements of the given matrix will not exceed 10,000.
 * There are at least one 0 in the given matrix.
 * The cells are adjacent in only four directions: up, down, left and right.
 *
 * DFS的解法太笨拙了。
 *
 * BFS很合适，一个技巧是一次性把所有元素加入queue，然后置所有1为-1好比较大小。
 *
 * @author xu.zhang
 */
//TODO BFS
public class Matrix01 {

    public int[][] updateMatrix(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        boolean[][] visited = new boolean[row][col];
        Set<String> hasSetBefore = new HashSet<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    helper(matrix, visited, i + 1, j, row, col, 1, hasSetBefore);
                    helper(matrix, visited, i - 1, j, row, col, 1, hasSetBefore);
                    helper(matrix, visited, i, j + 1, row, col, 1, hasSetBefore);
                    helper(matrix, visited, i, j - 1, row, col, 1, hasSetBefore);
                }
            }
        }
        return matrix;
    }

    void helper(int[][] matrix, boolean[][] visited, int i, int j, int row, int col, int step, Set<String>
            hasSetBefore) {
        //System.out.println(String.format("i=%d,j=%d,step=%d", i, j, step));
        if (i < 0 || i >= row || j < 0 || j >= col || matrix[i][j] == 0 || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        if (!hasSetBefore.contains("x" + i + "y" + j) || (hasSetBefore.contains("x" + i + "y" + j) && matrix[i][j] >=
                step)) {
            matrix[i][j] = step;
            hasSetBefore.add("x" + i + "y" + j);
            //System.out.println(String.format("update i=%d,j=%d to step=%d", i, j, step));
            helper(matrix, visited, i + 1, j, row, col, step + 1, hasSetBefore);
            helper(matrix, visited, i - 1, j, row, col, step + 1, hasSetBefore);
            helper(matrix, visited, i, j + 1, row, col, step + 1, hasSetBefore);
            helper(matrix, visited, i, j - 1, row, col, step + 1, hasSetBefore);
        }
        visited[i][j] = false;
    }

}
