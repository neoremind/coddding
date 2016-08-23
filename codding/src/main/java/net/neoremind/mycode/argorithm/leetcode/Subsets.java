package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a set of distinct integers, nums, return all possible subsets.
 * <p>
 * Note: The solution set must not contain duplicate subsets.
 * <p>
 * For example,
 * If nums = [1,2,3], a solution is:
 * <p>
 * [
 * [3],
 * [1],
 * [2],
 * [1,2,3],
 * [1,3],
 * [2,3],
 * [1,2],
 * []
 * ]
 * <p>
 * backtrack实际是一个DFS的过程
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/subsets/
 */
public class Subsets {

    /**
     * Recursive (Backtracking)
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        dfs(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void dfs(List<List<Integer>> list, List<Integer> tempList, int[] nums, int start) {
        list.add(new ArrayList<>(tempList));
        for (int i = start; i < nums.length; i++) {
            tempList.add(nums[i]);
            dfs(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    /**
     * Bit Manipulation
     * <p>
     * Each element is either in the subset or not
     * <p>
     * : element is in subset
     * -: element NOT in subset
     * so subsets of [1,2,3] are
     * <p>
     * [-,-,*] = [3]
     * [*,-,-] = [1]
     * [-,*,-] = [2]
     * [*,*,*] = [1,2,3]
     * [*,-,*] = [1,3]
     * [-,*,*] = [2,3]
     * [*,*,-] = [1,2]
     * [-,-,-] = []
     * <p>
     * use binary to repsent only two states
     * <p>
     * 001 = [3]
     * 100 = [1]
     * 010 = [2]
     * 111 = [1,2,3]
     * 101 = [1,3]
     * 011 = [2,3]
     * 110 = [1,2]
     * 000 = []
     *
     * @param S
     *
     * @return
     */
    public List<List<Integer>> subsets2(int[] S) {
        Arrays.sort(S);
        int totalNumber = 1 << S.length;
        List<List<Integer>> collection = new ArrayList<List<Integer>>(totalNumber);
        for (int i = 0; i < totalNumber; i++) {
            List<Integer> set = new LinkedList<Integer>();
            for (int j = 0; j < S.length; j++) {
                if ((i & (1 << j)) != 0) {
                    set.add(S[j]);
                }
            }
            collection.add(set);
        }
        return collection;
    }

    /**
     * Iterative
     * <p>
     * This problem can also be solved iteratively. Take [1, 2, 3] in the problem statement as an example. The process
     * of generating all the subsets is like:
     * <p>
     * Initially: [[]]
     * Adding the first number to all the existed subsets: [[], [1]];
     * Adding the second number to all the existed subsets: [[], [1], [2], [1, 2]];
     * Adding the third number to all the existed subsets: [[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]].
     */
    //TODO
    public List<List<Integer>> subsets3(int[] S) {
        return null;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3};
        List<List<Integer>> result = subsets(nums);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(8));

        nums = new int[] {1, 2, 3, 4};
        result = subsets(nums);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(16));

        nums = new int[] {1, 2, 3, 4};
        result = subsets2(nums);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(16));
    }
}
