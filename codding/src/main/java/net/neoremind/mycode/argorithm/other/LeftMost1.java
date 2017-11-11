package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

/**
 * @author xu.zhang
 */
public class LeftMost1 {

    public int leftMost1(int[][] nums) {
        // nums length not 0
        int rightLimit = nums[0].length - 1;
        for (int i = 0; i < nums.length; i++) {
            int[] row = nums[i];
            int left = 0;
            int right = rightLimit;
            if (row[right] == 0) {
                continue;
            }
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (row[mid] == 1) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            rightLimit = right;
        }
        return rightLimit;
    }

    @Test
    public void test() {
        int[][] nums = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 1, 1, 1},
                {0, 1, 1, 1, 1},
                {0, 0, 0, 1, 1}
        };
        System.out.println(leftMost1(nums)); // 1

        nums = new int[][]{
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1, 1},
                {0, 0, 0, 0, 1, 1}
        };
        System.out.println(leftMost1(nums)); // 2

        nums = new int[][]{
                {0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 1, 1},
                {0, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1}
        };
        System.out.println(leftMost1(nums)); // 0

        nums = new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 1}
        };
        System.out.println(leftMost1(nums)); // 4
    }
}
