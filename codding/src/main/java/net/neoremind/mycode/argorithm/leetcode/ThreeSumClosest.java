package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Given an array S of n integers, find three integers in S such that the sum is closest to a given number, target.
 * Return the sum of the three integers. You may assume that each input would have exactly one solution.
 * <p>
 * For example, given array S = {-1 2 1 -4}, and target = 1.
 * <p>
 * The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 * <p>
 * 和{@link ThreeSum}类似，只是判断条件变成了取绝对值，先固定nums0，1，2加和为closest，后续一个个更新
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/3sum/
 */
public class ThreeSumClosest {

    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int right = nums.length - 1;
        int left = 0;
        int closest = nums[0] + nums[1] + nums[2];
        while (right > 1) {
            int positiveNum = nums[right];
            while (left < right - 1) {
                int negativeNum = nums[left];
                for (int mid = left + 1; mid < right; mid++) {
                    int midNum = nums[mid];
                    if (Math.abs(target - (midNum + positiveNum + negativeNum)) < Math.abs(target - closest)) {
                        closest = (midNum + positiveNum + negativeNum);
                        // 没有break！！！
                    }
                }
                while (left < right && nums[left] == negativeNum) {
                    left++;
                }
            }
            left = 0;
            while (right > 0 && nums[right] == positiveNum) {
                right--;
            }
        }
        return closest;
    }

    @Test
    public void test() {
        int result = threeSumClosest(new int[] {-1, 2, 1, -4}, 1);
        System.out.println(result);
        assertThat(result, Matchers.is(2));

        result = threeSumClosest(new int[] {0, 2, 1, -3}, 1);
        System.out.println(result);
        assertThat(result, Matchers.is(0));
    }

}
