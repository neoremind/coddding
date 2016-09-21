package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * 完全错误的DP!!!
 * 完全错误的DP!!!
 * 完全错误的DP!!!
 * 不能用DP!!!
 * 不能用DP!!!
 * 不能用DP!!!
 * 和{@link net.neoremind.mycode.argorithm.leetcode.LongestIncreasingSubsequence}思想类似的DP
 *
 * @author zhangxu
 */
//TODO
@Deprecated
public class SmallerNumber {

    public List<Integer> countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }

        reverse(nums);

        // 7 3 5 9
        int[] dp = new int[nums.length];
        dp[nums.length - 1] = 0;
        start:
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (nums[j] < nums[i]) {
                    dp[i] = dp[j] + 1;
                    continue start;
                }
            }
        }

        reverse(dp);

        return IntStream.of(dp).boxed().collect(Collectors.toList());
    }

    public void reverse(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }

    @Test
    public void test() {
        int[] a = new int[] {1, 2};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(0, 0)));

        a = new int[] {2, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(1, 0)));

        a = new int[] {9, 5, 3, 7};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(3, 1, 0, 0)));

        a = new int[] {5, 2, 6, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(2, 1, 1, 0)));
    }

}
