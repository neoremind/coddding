package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Given an array of integers, find out whether there are two distinct indices i and j in the array such that the
 * difference between nums[i] and nums[j] is at most t and the difference between i and j is at most k.
 *
 * @see https://leetcode.com/problems/contains-duplicate-iii/
 */
public class ContainsDuplicateIII {

    /**
     * TLE
     * 注意三点
     * 1） j=i+1
     * 2）j - i <= k
     * 3）j < nums.length，放在上面的i < nums.length无法防止k>nums.length的情况
     * 4）绝对值要用long，避免减法溢出
     */
    public static boolean containsNearbyAlmostDuplicateBruteForce(int[] nums, int k, int t) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j - i <= k && j < nums.length; j++) {
                if (Math.abs((long) nums[i] - (long) nums[j]) <= t) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 使用平衡树BST，例如红黑树，降低Brute force解法中内层循环的复杂度为logK
     */
    public static boolean containsNearbyAlmostDuplicateBST(int[] nums, int k, int t) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return false;
        }

        TreeSet<Long> bst = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            /**
             * Returns the greatest key less than or equal to the given key,
             * or {@code null} if there is no such key.
             */
            Long floor = bst.floor((long) nums[i] + t);
            /**
             * Returns the least key greater than or equal to the given key,
             * or {@code null} if there is no such key.
             */
            Long ceiling = bst.ceiling((long) nums[i] - t);
            if ((floor != null && floor >= nums[i])
                    || (ceiling != null && ceiling <= nums[i])) {
                return true;
            }
            bst.add((long) nums[i]);
            if (bst.size() > k) {
                bst.remove((long) nums[i - k]);
            }
        }
        return false;
    }

    /**
     * 基于桶排序的原理，可以降低到O(N)的时间复杂度，但是牺牲了很多空间
     */
    public static boolean containsNearbyAlmostDuplicateBucket(int[] nums, int k, int t) {
        if (k < 1 || t < 0) {
            return false;
        }
        Map<Long, Long> bucket = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            long mappedValue = (long) nums[i] - Integer.MIN_VALUE;
            long hashedKey = mappedValue / ((long) t + 1);
            if (bucket.containsKey(hashedKey) ||
                    (bucket.containsKey(hashedKey - 1) && mappedValue - bucket.get(hashedKey - 1) <= t) ||
                    (bucket.containsKey(hashedKey + 1) && bucket.get(hashedKey + 1) - mappedValue <= t)) {
                return true;
            }
            bucket.put(hashedKey, mappedValue);
            if (bucket.size() > k) {
                bucket.remove(((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1));
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {3, 6, 0, 4};
        assertThat(containsNearbyAlmostDuplicateBruteForce(nums, 2, 2), is(true));
        assertThat(containsNearbyAlmostDuplicateBST(nums, 2, 2), is(true));
        assertThat(containsNearbyAlmostDuplicateBucket(nums, 2, 2), is(true));

        nums = new int[] {-1, Integer.MAX_VALUE};
        assertThat(containsNearbyAlmostDuplicateBruteForce(nums, 1, Integer.MAX_VALUE), is(false));
        assertThat(containsNearbyAlmostDuplicateBST(nums, 1, Integer.MAX_VALUE), is(false));
        assertThat(containsNearbyAlmostDuplicateBucket(nums, 1, Integer.MAX_VALUE), is(false));

        nums = new int[] {-3, 3};
        assertThat(containsNearbyAlmostDuplicateBruteForce(nums, 2, 4), is(false));
        assertThat(containsNearbyAlmostDuplicateBST(nums, 2, 4), is(false));
        assertThat(containsNearbyAlmostDuplicateBucket(nums, 2, 4), is(false));

        nums = new int[] {-1, -1};
        assertThat(containsNearbyAlmostDuplicateBruteForce(nums, 1, 0), is(true));
        assertThat(containsNearbyAlmostDuplicateBST(nums, 1, 0), is(true));
        assertThat(containsNearbyAlmostDuplicateBucket(nums, 1, 0), is(true));
    }

}
