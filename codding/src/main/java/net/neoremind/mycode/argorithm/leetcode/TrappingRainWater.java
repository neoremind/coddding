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

    public int trap2(int[] height) {
        if (height == null ||  height.length < 3) {
            return 0;
        }
        int l = 0, r = height.length - 1;
        int maxL = 0, maxR = height.length - 1;
        int res = 0;
        while (l <= r) {
            if (height[maxL] <= height[maxR]) {
                if (height[l] >= height[maxL]) {
                    maxL = l;
                } else {
                    res += height[maxL] - height[l];
                }
                l++;
            } else {
                if (height[r] >= height[maxR]) {
                    maxR = r;
                } else {
                    res += height[maxR] - height[r];
                }
                r--;
            }
        }
        return res;
    }

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

    public int trap3(int[] height) {
        int len = height.length;
        int[] left = new int[len];
        int[] right = new int[len];
        int max = 0;
        for (int i =0;i<len;i++) {
            max = Math.max(max, height[i]);
            left[i] = max;
        }
        // System.out.println(Arrays.toString(left));

        max = 0;
        for (int i =len-1;i>=0;i--) {
            max = Math.max(max, height[i]);
            right[i] = max;
        }
        //  System.out.println(Arrays.toString(right));

        int res = 0;
        for (int i =0;i<len;i++) {
            res += (Math.min(left[i], right[i]) - height[i]);
            //System.out.println(res);
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
