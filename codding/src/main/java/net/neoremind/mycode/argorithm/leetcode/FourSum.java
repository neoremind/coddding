package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given an array S of n integers, are there elements a, b, c, and d in S such that a + b + c + d = target? Find all
 * unique quadruplets in the array which gives the sum of target.
 * <p>
 * Note: The solution set must not contain duplicate quadruplets.
 * <p>
 * For example, given array S = [1, 0, -1, 0, -2, 2], and target = 0.
 * <p>
 * A solution set is:
 * [
 * [-1,  0, 0, 1],
 * [-2, -1, 1, 2],
 * [-2,  0, 0, 2]
 * ]
 *
 * 基于{@link ThreeSum}的版本，beat 65%。
 *
 * @author xu.zhang
 */
public class FourSum {

    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> res = new LinkedList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 3; i++) {
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
                int newTarget = target - nums[i];
                List<List<Integer>> temp = threeSum(nums, i + 1, nums.length - 1, newTarget);
                for (List<Integer> list : temp) {
                    List<Integer> newList = new ArrayList<>(list);
                    list.add(nums[i]);
                    res.add(newList);
                }
            }
        }
        return res;
    }

    public List<List<Integer>> threeSum(int[] num, int start, int end, int target) {
        List<List<Integer>> res = new LinkedList<>();
        for (int i = start; i < end - 1; i++) {
            if (i == start || (i > start && num[i] != num[i - 1])) {
                int lo = i + 1, hi = end, sum = target - num[i];
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


}
