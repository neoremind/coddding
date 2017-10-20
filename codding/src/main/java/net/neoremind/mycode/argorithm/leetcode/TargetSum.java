package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * You are given a list of non-negative integers, a1, a2, ..., an, and a target, S. Now you have 2 symbols + and -.
 * For each integer, you should choose one from + and - as its new symbol.
 * <p>
 * Find out how many ways to assign symbols to make sum of integers equal to target S.
 * <p>
 * Example 1:
 * Input: nums is [1, 1, 1, 1, 1], S is 3.
 * Output: 5
 * Explanation:
 * <p>
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 * <p>
 * There are 5 ways to assign symbols to make the sum of nums be target 3.
 * Note:
 * The length of the given array is positive and will not exceed 20.
 * The sum of elements in the given array will not exceed 1000.
 * Your output answer is guaranteed to be fitted in a 32-bit integer.
 * <p>
 * 方法1，会TLE.
 * 和{@link Game24}类似。使用DFS的模板，找两个数字合并，然后直到只剩下一个数字为止，时间复杂度2^n，exponential，会TLE。
 * 仔细想想N皇后的时间复杂度是N!，实际因为有choice is possible的判断，所有不可能这么高，但是还是高于2^n的，N=8的时候是2057，2^8才等于256。
 * <pre>
 * dfs(step0) {
 *     if all steps had solutions
 *         selected_choices is an answer
 *     foreach choice in all choices in current step
 *         if choice is possible
 *             selected_choices[step] = choice
 *             dfs(next step)
 *             selected_choices[step] = no choice or last choice // this is called backtracking
 * }
 * </pre>
 * 方法2：非常简单的DFS版本。
 * <p>
 * 方法3：方法2的记忆化版本。
 * <p>
 * //TODO实际这个可以优化，因为有很多子问题重叠，这正好是动态规划的优势！（但是子问题重叠不是动态规划的本质，而是一个加速手段）。
 * <p>
 * 方法4：DP，2维DP和1维DP //TODO
 * 上面DFS的递归太慢了，指数级的时间复杂度不可行，可以使用动态规划，转换成找子集的问题。
 * 假设nums可以分成P个正数subset和N个负数subset，比如：
 * [1,2,3,4,5]
 * 那么target=3，一种解法就是+1-2+3-4+5=3。
 * 所以P=[1,3,5],N=[2,4].
 * 推导一个公式：
 * sum(P) - sum(N) = target
 * sum(P) + sum(N) + sum(P) - sum(N) = target + sum(P) + sum(N)
 * 2*sum(P) = target + sum(nums)
 * <p>
 * 所以问题就是找
 * subset P，似的sum(P) = (target + sum(nums)) / 2
 * 可以得出target + sum(nums)必须是偶数，所以又是一个剪枝优化。
 * <p>
 * //TODO subset sum 问题
 *
 * @author xu.zhang
 */
public class TargetSum {

    int count = 0;

    public int findTargetSumWays2(int[] nums, int S) {
        calculate(nums, 0, 0, S);
        return count;
    }

    public void calculate(int[] nums, int i, int sum, int S) {
        if (i == nums.length) {
            if (sum == S)
                count++;
        } else {
            calculate(nums, i + 1, sum + nums[i], S);
            calculate(nums, i + 1, sum - nums[i], S);
        }
    }


    public int findTargetSumWays3(int[] nums, int S) {
        Map<Integer, Map<Integer, Integer>> mem = new HashMap<>();
        return calculate(nums, 0, 0, S, mem);
    }

    public int calculate(int[] nums, int i, int sum, int S, Map<Integer, Map<Integer, Integer>> mem) {
        if (mem.containsKey(i) && mem.get(i).get(sum) != null) {
            return mem.get(i).get(sum);
        }
        if (i == nums.length) {
            if (sum == S)
                return 1;
            else
                return 0;
        } else {
            int add = calculate(nums, i + 1, sum + nums[i], S, mem);
            int sub = calculate(nums, i + 1, sum - nums[i], S, mem);
            if (!mem.containsKey(i)) {
                mem.put(i, new HashMap<>());
            }
            mem.get(i).put(sum, mem.get(i).getOrDefault(sum, 0) + add + sub);
            return add + sub;
        }
    }

    /////////////////////////////////////

    int counter = 0;

    /**
     * 自己写的太慢了。
     */
    public int findTargetSumWays(int[] nums, int S) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        List<Integer> list1 = new ArrayList<>(nums.length);
        List<Integer> list2 = new ArrayList<>(nums.length);
        list1.add(nums[0]);
        list2.add(0 - nums[0]);
        for (int i = 1; i < nums.length; i++) {
            list1.add(nums[i]);
            list2.add(nums[i]);
        }
        helper(list1, S);
        helper(list2, S);
        return counter;
    }

    int invoke = 0;

    void helper(List<Integer> list, int S) {
        invoke++;
        if (list.size() == 1) {
            if (list.get(0) == S) {
                counter++;
            }
            return;
        }
        int num1 = list.get(0);
        int num2 = list.get(1);
        List<Integer> rest = new ArrayList<>(list.size() - 1);
        for (int i = 2; i < list.size(); i++) {
            rest.add(list.get(i));
        }

        rest.add(0, num1 + num2);
        helper(rest, S);
        rest.remove(0);

        rest.add(0, num1 - num2);
        helper(rest, S);
        rest.remove(0);
    }

    //////////

    public int findTargetSumWays4(int[] nums, int s) {
        int sum = 0;
        for (int n : nums) sum += n;
        return sum < s || (s + sum) % 2 > 0 ? 0 : subsetSum(nums, (s + sum) >>> 1);
    }

    //利用滚动数组来，未能理解？？？
    private int subsetSum(int[] nums, int s) {
        int[] dp = new int[s + 1];
        dp[0] = 1;
        for (int n : nums)
            for (int i = s; i >= n; i--)
                dp[i] += dp[i - n];
        return dp[s];
    }


    @Test
    public void test() {
        counter = 0;
        invoke = 0;
        assertThat(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3), Matchers.is(5));
        System.out.println(invoke);

        counter = 0;
        invoke = 0;
        assertThat(findTargetSumWays(new int[]{1, 1, 2, 1, 1}, 3), Matchers.is(0));
        System.out.println(invoke);

        counter = 0;
        invoke = 0;
        assertThat(findTargetSumWays(new int[]{20, 38, 6, 2, 47, 18, 50, 41, 38, 32, 24, 38, 38, 30, 5, 26, 15, 37,
                35}, 44), Matchers.is(3072));
        System.out.println(invoke);

        count = 0;
        assertThat(findTargetSumWays2(new int[]{1, 1, 1, 1, 1}, 3), Matchers.is(5));
        count = 0;
        assertThat(findTargetSumWays2(new int[]{20, 38, 6, 2, 47, 18, 50, 41, 38, 32, 24, 38, 38, 30, 5, 26, 15, 37,
                35}, 44), Matchers.is(3072));

        count = 0;
        assertThat(findTargetSumWays3(new int[]{1, 1, 1, 1, 1}, 3), Matchers.is(5));
        count = 0;
        assertThat(findTargetSumWays3(new int[]{20, 38, 6, 2, 47, 18, 50, 41, 38, 32, 24, 38, 38, 30, 5, 26, 15, 37,
                35}, 44), Matchers.is(3072));
    }

}
