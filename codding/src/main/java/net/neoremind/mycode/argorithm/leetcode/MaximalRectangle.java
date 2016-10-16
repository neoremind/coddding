package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Stack;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.
 * <p>
 * For example, given the following matrix:
 * <p>
 * 1 0 1 0 0
 * 1 0 1 1 1
 * 1 1 1 1 1
 * 1 0 0 1 0
 * Return 6.
 *
 * https://leetcode.com/problems/maximal-rectangle/
 *
 * @author zhangxu
 */
public class MaximalRectangle {

    public int maximalRectangle(char[][] matrix) {
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] heights = getHistogram(matrix, i);
            System.out.println("calc :" + Arrays.toString(heights));
            max = Math.max(max, largestRectangleInHistogram(heights));
            System.out.println("sub max :" + largestRectangleInHistogram(heights));
        }
        return max;
    }

    int[] getHistogram(char[][] matrix, int row) {
        int[] heights = new int[matrix[0].length];
        boolean[] hasEnd = new boolean[matrix[0].length];
        for (int i = row; i >= 0; i--) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '1' && !hasEnd[j]) {
                    heights[j]++;
                } else {
                    hasEnd[j] = true;
                }
            }
        }
        return heights;
    }

    int largestRectangleInHistogram(int[] heights) {
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i <= heights.length; i++) {
            int h;
            if (i == heights.length) {
                h = 0;
            } else {
                h = heights[i];
            }
            if (stack.isEmpty() || h >= heights[stack.peek()]) {
                stack.push(i);
            } else {
                int lastHighIdx = stack.pop();
                int area;
                if (!stack.isEmpty()) {
                    area = heights[lastHighIdx] * (i - 1 - stack.peek());
                } else {
                    area = heights[lastHighIdx] * i;
                }
                maxArea = Math.max(maxArea, area);
                i--;
            }
        }
        return maxArea;
    }

    @Test
    public void test() {
        char[][] matrix = new char[][] {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'},
        };
        assertThat(maximalRectangle(matrix), Matchers.is(6));
    }

}
