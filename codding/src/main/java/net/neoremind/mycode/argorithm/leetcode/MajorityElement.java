package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊
 * n/2 ⌋ times.
 * <p>
 * You may assume that the array is non-empty and the majority element always exist in the array.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/majority-element/
 */
public class MajorityElement {

    // Sorting
    public int majorityElement1(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    public int majorityElement2(int[] nums) {
        Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();
        int ret = 0;
        for (int num : nums) {
            if (!myMap.containsKey(num)) {
                myMap.put(num, 1);
            } else {
                myMap.put(num, myMap.get(num) + 1);
            }
            if (myMap.get(num) > nums.length / 2) {
                ret = num;
                break;
            }
        }
        return ret;
    }

    // Moore voting algorithm
    public int majorityElement3(int[] nums) {
        int count = 0, ret = 0;
        for (int num : nums) {
            if (count == 0) {
                ret = num;
            }
            if (num != ret) {
                count--;
            } else {
                count++;
            }
        }
        return ret;
    }

    // Bit manipulation
    public int majorityElement4(int[] nums) {
        int[] bit = new int[32];
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                if ((num >> (31 - i) & 1) == 1) {
                    bit[i]++;
                }
            }
        }
        int ret = 0;
        for (int i = 0; i < 32; i++) {
            bit[i] = bit[i] > nums.length / 2 ? 1 : 0;
            ret += bit[i] * (1 << (31 - i));
        }
        return ret;
    }

    @Test
    public void test() {
        int[] nums = new int[] {2, 5, 7, 9, 2, 0, 2, 0, 2, 2, 1, 2, 6, 7, 2, 2, 2, 2, 2};
        //assertThat(majorityElement1(nums), is(2));
        assertThat(majorityElement2(nums), is(2));
        assertThat(majorityElement3(nums), is(2));
        assertThat(majorityElement4(nums), is(2));
    }
}
