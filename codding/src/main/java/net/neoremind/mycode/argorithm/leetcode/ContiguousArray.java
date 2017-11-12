package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given a binary array, find the maximum length of a contiguous subarray with equal number of 0 and 1.
 * <p>
 * Example 1:
 * Input: [0,1]
 * Output: 2
 * Explanation: [0, 1] is the longest contiguous subarray with equal number of 0 and 1.
 * Example 2:
 * Input: [0,1,0]
 * Output: 2
 * Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
 * Note: The length of the given binary array will not exceed 50,000.
 * <p>
 * 只包含0和1，找到最长的一段，包含数目相等的0和1.和Maximum Size Subarray Sum Equals k非常类似。
 *
 * @author xu.zhang
 */
public class ContiguousArray {

    public int findMaxLength(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) nums[i] = -1;
        }
        Map<Integer, Integer> map = new HashMap<>();
        int res = Integer.MIN_VALUE;
        int k = 0;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum == k) {
                res = Math.max(res, i + 1);
            } else if (map.containsKey(sum - k)) {
                res = Math.max(res, i - map.get(sum - k));
            }
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return res == Integer.MIN_VALUE ? 0 : res;
    }

    @Test
    public void test() {
        int[] nums = new int[]{0, 1};
        assertThat(findMaxLength(nums), is(2));

        nums = new int[]{0, 1, 1, 0, 1, 1, 0};
        assertThat(findMaxLength(nums), is(4));
    }

}
