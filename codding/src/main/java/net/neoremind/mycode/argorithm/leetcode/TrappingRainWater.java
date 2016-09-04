package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.primitives.Ints;

/**
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much
 * water it is able to trap after raining.
 * <p>
 * For example,
 * Given [0,1,0,2,1,0,1,3,2,1,2,1], return 6.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/trapping-rain-water/
 */
public class TrappingRainWater {

    public int trap(int[] a) {
        int maxHeight = 0;
        int maxHeightIdx = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > maxHeight) {
                maxHeight = a[i];
                maxHeightIdx = i;
            }
        }
        int res = 0;
        int left = 0, maxLeft = 0;
        int right = a.length - 1, maxRight = 0;
        while (left < maxHeightIdx) {
            if (a[left] > maxLeft) {
                maxLeft = a[left];
            } else {
                res += maxLeft - a[left];
            }
            left++;
        }

        while (right > maxHeightIdx) {
            if (a[right] > maxRight) {
                maxRight = a[right];
            } else {
                res += maxRight - a[right];
            }
            right--;
        }

        return res;
    }

    @Test
    public void test() {
        int[] height = new int[] {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        prettyPrint(height);
        assertThat(trap(height), Matchers.is(6));

        height = new int[] {2, 0, 2};
        prettyPrint(height);
        assertThat(trap(height), Matchers.is(2));
    }

    private void prettyPrint(int[] height) {
        int row = Ints.max(height);
        for (int i = row; i >= 0; i--, row--) {
            for (int j = 0; j < height.length; j++) {
                if (height[j] >= row) {
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
