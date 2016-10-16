package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Stack;

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
     * 太精巧的解法了。
     * <p>
     * <pre>
     *      ∏   -6
     *   ∏  ∏   -5
     *   ∏  ∏   -4
     *  ∏∏  ∏∏  -3
     *  ∏∏∏∏∏∏  -2
     * ∏∏∏∏∏∏∏  -1
     * 0123456
     *
     * 1. stack is empty, push 0 to stack, stack = [0]
     * 2. nums[1] >= stack.peek, push 1 to stack, stack = [0, 1]
     * 3. nums[2] >= stack.peek, push 2 to stack, stack = [0, 1, 2]
     * 4. nums[3] < stack.peek, max area = pop stack which is nums[2] * (i - 1 - stack.peek 1) = 5, keep i not move,
     * 5. nums[3] < stack.peek, max area = pop stack which is nums[1] * (i - 1 - stack.peek 0) = 6, keep i not move
     * 6. nums[3] >= stack.peek 0, push 3 to stack, stack = [0, 3]
     * 7. nums[4] >= stack.peek 3, push 4 to stack, stack = [0, 3, 4]
     * 8. nums[5] >= stack.peek 4, push 5 to stack, stack = [0, 3, 4, 5]
     * 9. nums[6] < stack.peek, max area = pop stack which is nums[5] * (i - 1 - stack.peek 4) = 6, keep i not move
     * 10.nums[6] >= stack.peek 4, push 6 to stack, stack = [0, 3, 4, 6]
     * 11.out of bound which is 0 < stack.peek, max area = pop stack which is nums[6] * (i - 1 - stack.peek 4) = 6
     * 12.out of bound which is 0 < stack.peek, max area = pop stack which is nums[4] * (i - 1 - stack.peek 3) = 8
     * 13.out of bound which is 0 < stack.peek, max area = pop stack which is nums[3] * (i - 1 - stack.peek 0) = 12
     * 14.out of bound which is 0 < stack.peek, max area = pop stack which is nums[0] * (stack is empty so i) = 7
     *
     * </pre>
     * 一个很好的解释：http://www.cnblogs.com/lichen782/p/leetcode_Largest_Rectangle_in_Histogram.html
     * <p>
     * https://discuss.leetcode.com/topic/7599/o-n-stack-based-java-solution
     */
    public int largestRectangleAreaStack(int[] heights) {
        int len = heights.length;
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        for (int i = 0; i <= len; i++) {
            int h;
            if (i == len) {
                h = 0;
            } else {
                h = heights[i];
            }
            if (stack.isEmpty() || h >= heights[stack.peek()]) {
                stack.push(i);
            } else {
                int tp = stack.pop();
                maxArea = Math.max(maxArea, heights[tp] * (stack.isEmpty() ? i : i - 1 - stack.peek()));
                i--;
            }
        }
        return maxArea;
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

        height = new int[] {1, 2, 4, 5, 3};
        prettyPrint(height);
        assertThat(largestRectangleAreaStack(height), Matchers.is(9));

        height = new int[] {1, 3, 5, 2, 2, 6, 3};
        prettyPrint(height);
        assertThat(largestRectangleAreaStack(height), Matchers.is(12));
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
