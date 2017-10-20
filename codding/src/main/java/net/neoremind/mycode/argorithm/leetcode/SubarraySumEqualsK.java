package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given an array of integers and an integer k, you need to find the total number of continuous subarrays whose sum
 * equals to k.
 * <p>
 * Example 1:
 * Input:nums = [1,1,1], k = 2
 * Output: 2
 * Note:
 * The length of the array is in range [1, 20,000].
 * The range of numbers in the array is [-1000, 1000] and the range of the integer k is [-1e7, 1e7].
 *
 * @author xu.zhang
 */
public class SubarraySumEqualsK {

    public int subarraySum(int[] nums, int k) {
        int sum = 0;
        int result = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k)) {
                result += map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return result;
    }

    @Test
    public void test() {
        assertThat(subarraySum(new int[]{1, -1, 5, -2, 3}, 3), is(2));
        assertThat(subarraySum(new int[]{-2, -1, 2, 1}, 1), is(1));
        assertThat(subarraySum(new int[]{5, 2, 3, 1, 2, 3, 4, 5}, 10), is(1));
        assertThat(subarraySum(new int[]{5, 2, 3, 1, 2, 3, -1, 1, 4, 5}, 10), is(2));
        assertThat(subarraySum(new int[]{1, 2, 3, 4, 5}, 10), is(1));
        assertThat(subarraySum(new int[]{1, 2, 3, 4, 1, 1, 1, 2}, 10), is(3));
        assertThat(subarraySum(new int[]{1, 2, 3}, 3), is(2));
    }

}
