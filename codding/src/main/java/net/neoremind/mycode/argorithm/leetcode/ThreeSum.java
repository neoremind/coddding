package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Given an array S of n integers, are there elements a, b, c in S such that a + b + c = 0? Find all unique triplets
 * in the array which gives the sum of zero.
 * <p>
 * Note: The solution set must not contain duplicate triplets.
 * <p>
 * For example, given array S = [-1, 0, 1, 2, -1, -4],
 * <p>
 * A solution set is:
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 * <p>
 * 1）先排序
 * 2）从后往前定住最后一个，然后从前往后定住一个，中间的遍历找。
 * 3）找到了，那么为了去重，要跳过从前往后重复的元素，以及从后往前重复的元素。这样就不用维护Set了。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/3sum/
 */
public class ThreeSum {

    /**
     * TLE
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        int right = nums.length - 1;
        int left = 0;
        while (right > 1) {
            int positiveNum = nums[right];
            while (left < right - 1) {
                int negativeNum = nums[left];
                for (int mid = left + 1; mid < right; mid++) {
                    int midNum = nums[mid];
                    if (midNum + positiveNum + negativeNum == 0) {
                        //                        List<Integer> found = new ArrayList<>(3);
                        //                        found.add(negativeNum);
                        //                        found.add(nums[mid]);
                        //                        found.add(positiveNum);
                        //                        result.add(found);
                        // 下面的语句不会造成Time limit issue，性能好一些！
                        result.add(Arrays.asList(new Integer[]{negativeNum, midNum, positiveNum}));
                        break;  // need to quit in case next number is the same as mid
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
        return result;
    }

    public List<List<Integer>> threeSum2(int[] num) {
        Arrays.sort(num);
        List<List<Integer>> res = new LinkedList<>();
        for (int i = 0; i < num.length - 2; i++) {
            if (i == 0 || (i > 0 && num[i] != num[i - 1])) {
                int lo = i + 1, hi = num.length - 1, sum = 0 - num[i];
                while (lo < hi) {
                    if (num[lo] + num[hi] == sum) {
                        res.add(Arrays.asList(num[i], num[lo], num[hi]));
                        while (lo < hi && num[lo] == num[lo + 1]) lo++;
                        while (lo < hi && num[hi] == num[hi - 1]) hi--;
                        lo++;
                        hi--;
                    } else if (num[lo] + num[hi] < sum) {
                        lo++;
                    } else {
                        hi--;
                    }
                }
            }
        }
        return res;
    }

    @Test
    public void test() {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result = threeSum(nums);
        System.out.println(result);
        assertThat(result.size(), Matchers.is(2));
        assertThat(result.get(0), Matchers.is(Lists.newArrayList(-1, -1, 2)));
        assertThat(result.get(1), Matchers.is(Lists.newArrayList(-1, 0, 1)));

        nums = new int[]{0, 0, 0, 0};
        result = threeSum(nums);
        System.out.println(result);
        assertThat(result.size(), Matchers.is(1));
        assertThat(result.get(0), Matchers.is(Lists.newArrayList(0, 0, 0)));

        nums =
                new int[]{7, -1, 14, -12, -8, 7, 2, -15, 8, 8, -8, -14, -4, -5, 7, 9, 11, -4, -15, -6, 1, -14, 4,
                        3, 10, -5, 2, 1, 6, 11, 2, -2, -5, -7, -6, 2, -15, 11, -6, 8, -4, 2, 1, -1, 4, -6, -15, 1, 5,
                        -15,
                        10, 14, 9, -8, -6, 4, -6, 11, 12, -15, 7, -1, -9, 9, -1, 0, -4, -1, -12, -2, 14, -9, 7, 0, -3,
                        -4, 1,
                        -2, 12, 14, -10, 0, 5, 14, -1, 14, 3, 8, 10, -8, 8, -5, -2, 6, -11, 12, 13, -7, -12, 8, 6, -13,
                        14,
                        -2, -5, -11, 1, 3, -6};
        System.out.println(threeSum(nums));
    }

}
