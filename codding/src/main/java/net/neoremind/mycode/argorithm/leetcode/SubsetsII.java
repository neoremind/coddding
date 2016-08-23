package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a collection of integers that might contain duplicates, nums, return all possible subsets.
 * <p>
 * Note: The solution set must not contain duplicate subsets.
 * <p>
 * For example,
 * If nums = [1,2,2], a solution is:
 * <p>
 * [
 * [2],
 * [1],
 * [1,2,2],
 * [2,2],
 * [1,2],
 * []
 * ]
 * backtrack实际是一个DFS的过程
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/subsets-ii/
 */
public class SubsetsII {

    /**
     * Recursive (Backtracking)
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        dfs(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void dfs(List<List<Integer>> list, List<Integer> tempList, int[] nums, int start) {
        list.add(new ArrayList<>(tempList));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            tempList.add(nums[i]);
            dfs(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3};
        List<List<Integer>> result = subsetsWithDup(nums);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(8));

        nums = new int[] {1, 2, 1, 2};
        result = subsetsWithDup(nums);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(9));
    }
}
