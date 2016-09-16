package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.primitives.Ints;

/**
 * Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the
 * area of largest rectangle in the histogram.
 * <p>
 * For example,
 * Given heights = [2,1,5,6,2,3],
 * return 10.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/largest-rectangle-in-histogram/
 */
public class LargestRectangleInHistogram {

    public int largestRectangleArea(int[] heights) {
        int max = 0;
        for (int i = 0; i < heights.length; i++) {
            int minHeight = Integer.MAX_VALUE;
            for (int j = i; j < heights.length; j++) {
                if (heights[j] < minHeight) {
                    minHeight = heights[j];
                }
                max = Math.max(max, minHeight * (j - i + 1));
            }
        }
        return max;
    }

    public int largestRectangleAreaDP(int[] heights) {
        int len = heights.length;

        int max = 0;
        int[] left = new int[len];
        int[] right = new int[len];

        helper(heights, left, right);
        for (int i = 0; i < len; i++) {
            max = Math.max(max, (right[i] - left[i] + 1) * heights[i]);
        }
        return max;
    }

    void helper(int[] heights, int[] left, int[] right) {
        for (int i = 0; i < heights.length; i++) {
            int k = i;
            while (k > 0 && heights[i] <= heights[k - 1]) {
                k = left[k - 1];
            }
            left[i] = k;
        }

        for (int i = heights.length - 1; i >= 0; i--) {
            int k = i;
            while (k < heights.length - 1 && heights[i] <= heights[k + 1]) {
                k = right[k + 1];
            }
            right[i] = k;
        }
    }

    /**
     * O(n) stack based JAVA solution
     * <p>
     * https://discuss.leetcode.com/topic/7599/o-n-stack-based-java-solution
     */
    //TODO
    public int largestRectangleAreaStack(int[] heights) {
        return -1;
    }

    @Test
    public void test() {
        int[] height = new int[] {2, 1, 5, 6, 2, 3};
        prettyPrint(height);
        assertThat(largestRectangleArea(height), Matchers.is(10));
        assertThat(largestRectangleAreaDP(height), Matchers.is(10));

        height = new int[] {6, 2, 5, 4, 5, 1, 6};
        prettyPrint(height);
        assertThat(largestRectangleArea(height), Matchers.is(12));
        assertThat(largestRectangleAreaDP(height), Matchers.is(12));

        height = new int[] {2, 4, 2, 1};
        prettyPrint(height);
        assertThat(largestRectangleArea(height), Matchers.is(6));
        assertThat(largestRectangleAreaDP(height), Matchers.is(6));
    }

    private void prettyPrint(int[] height) {
        int row = Ints.max(height);
        for (int i = row; i >= 0; i--, row--) {
            for (int j = 0; j < height.length; j++) {
                if (height[j] > row) {
                    System.out.print("∏");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        for (int j = 0; j < height.length; j++) {
            System.out.print(j);
        }
        System.out.println();
    }
}
