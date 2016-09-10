package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given an array of integers and an integer k, find out whether there are two distinct indices i and j in the array
 * such that nums[i] = nums[j] and the difference between i and j is at most k.
 *
 * @see https://leetcode.com/problems/contains-duplicate-ii/
 */
public class ContainsDuplicateII {

    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>(nums.length / 3 * 4);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                if (Math.abs(map.get(nums[i]) - i) <= k) {
                    return true;
                }
            }
            map.put(nums[i], i);
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
        boolean res = containsNearbyDuplicate(nums, 2);
        System.out.println(res);
        assertThat(res, is(false));

        nums = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 5};
        res = containsNearbyDuplicate(nums, 2);
        System.out.println(res);
        assertThat(res, is(false));

        res = containsNearbyDuplicate(nums, 5);
        System.out.println(res);
        assertThat(res, is(true));

        nums = new int[] {-1, -1};
        res = containsNearbyDuplicate(nums, 1);
        System.out.println(res);
        assertThat(res, is(true));
    }

}
