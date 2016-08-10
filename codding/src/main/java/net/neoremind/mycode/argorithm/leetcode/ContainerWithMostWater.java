package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given n non-negative integers a1, a2, ..., an, where each represents a point at coordinate (i, ai). n vertical
 * lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0). Find two lines, which together
 * with x-axis forms a container, such that the container contains the most water.
 * <p>
 * Note: You may not slant the container.
 * <p>
 * 伪代码：
 * <pre>
 * i = 0
 * j = end
 * while i and j not meet
 *     max = MAX(area(i,j) ,max)
 *     if height[i] < height[j]
 *         i = i + 1
 *     if height[i] > height[j]
 *         j = j - 1
 * </pre>
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/container-with-most-water/
 */
public class ContainerWithMostWater {

    public int maxArea(int[] height) {
        int start = 0;
        int end = height.length - 1;
        int maxArea = 0;
        while (start < end) {
            maxArea = Math.max(area(end - start, Math.min(height[start], height[end])), maxArea);
            if (height[start] < height[end]) {
                start++;
            } else {
                end--;
            }
        }
        return maxArea;
    }

    private int area(int width, int height) {
        return width * height;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3, 4, 5};
        int area = maxArea(nums);
        System.out.println(area);
        assertThat(area, Matchers.is(6));
    }

}
