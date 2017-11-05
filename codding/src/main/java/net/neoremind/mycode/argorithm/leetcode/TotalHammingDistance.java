package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * @author xu.zhang
 */
public class TotalHammingDistance {

    public int totalHammingDistance(int[] nums) {
        int len = nums.length;
        int[] countOfOnes = new int[32];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 32; j++) {
                countOfOnes[j] += (nums[i] >> j) & 1;
            }
        }
        int sum = 0;
        for (int count: countOfOnes) {
            sum += count * (len - count);
        }
        return sum;
    }

    @Test
    public void test() {
        int[] nums = {4, 14, 2};
        assertThat(totalHammingDistance(nums), Matchers.is(6));
    }
}
