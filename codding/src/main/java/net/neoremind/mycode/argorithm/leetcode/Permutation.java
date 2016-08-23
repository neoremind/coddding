package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 无重复数全排列问题
 * Given a collection of distinct numbers, return all possible permutations.
 * <p>
 * For example,
 * [1,2,3] have the following permutations:
 * <pre>
 * [
 * [1,2,3],
 * [1,3,2],
 * [2,1,3],
 * [2,3,1],
 * [3,1,2],
 * [3,2,1]
 * ]
 * </pre>
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/permutations/
 * @see net.neoremind.mycode.argorithm.backtracking.Permutation
 */
public class Permutation {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        doPermute(nums, 0, result);
        return result;
    }

    private void doPermute(int[] nums, int i, List<List<Integer>> result) {
        if (i == nums.length) {
            List<Integer> numsList = new ArrayList<>(nums.length);
            for (int num : nums) {
                numsList.add(num);
            }
            result.add(numsList);
            System.out.println(numsList);
        } else {
            for (int j = i; j < nums.length; j++) {
                swap(nums, i, j);
                doPermute(nums, i + 1, result);
                swap(nums, i, j);
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * http://blog.csdn.net/sbitswc/article/details/36154559
     * http://blog.csdn.net/hackbuteer1/article/details/6657435
     * https://discuss.leetcode.com/topic/46162/a-general-approach-to-backtracking-questions-in-java-subsets
     * -permutations-combination-sum-palindrome-partioning
     */
    @Test
    public void test() {
        int[] nums = new int[] {1};
        assertThat(permute(nums).size(), Matchers.is(1));

        nums = new int[] {1, 2};
        assertThat(permute(nums).size(), Matchers.is(2));

        nums = new int[] {1, 2, 3};
        assertThat(permute(nums).size(), Matchers.is(6));

        nums = new int[] {1, 2, 3, 4};
        assertThat(permute(nums).size(), Matchers.is(24));
    }

}
