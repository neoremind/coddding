package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which
 * sum to n.
 * <p>
 * For example, given n = 12, return 3 because 12 = 4 + 4 + 4; given n = 13, return 2 because 13 = 4 + 9.
 * <p>
 * https://leetcode.com/problems/perfect-squares/
 * <p>
 * 动态规划和coin change的思想一样
 *
 * @author zhangxu
 */
public class PerfectSquares {

    /**
     * 1）初始化一个数字，n+1大小，递增，假设都是只有1这个因子
     * 2）找到离n最近的那个平方数，用Math.sqrt来。
     * 3）写动态规划的递推公式：
     * <pre>
     * for every square less than i
     *     dp[i] = min(dp[i], dp[i - square] + 1)
     *     </pre>
     * 4）返回最后的dp[n]即可。
     * n    combination     count
     * 1 => 1                1
     * 2 => 1 + 1            2
     * 3 => 1 + 1 + 1        3
     * 4 => 4                1
     * 5 => 4 + 1            2
     * 6 => 4 + 1 + 1        3
     * 7 => 4 + 1 + 1 + 1    4
     * 8 => 4 + 4            2
     * 9 => 9                1
     * 10 => 9 + 1           2
     * 11 => 9 + 1 + 1       3
     * 12 => 4 + 4 + 4       3
     * 13 => 9 + 4           2
     * 14 => 9 + 4 + 1       3
     * 15 => 9 + 4 + 1 + 1   4
     * 16 => 16              1
     */
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = i;
        }
        for (int i = 0; i <= n; i++) {
            int m = (int) Math.sqrt(i);
            for (int j = m; j >= 1; j--) {
                int square = j * j;
                if (i >= square) {
                    dp[i] = Math.min(dp[i], dp[i - square] + 1);
                }
            }
            System.out.println(Arrays.toString(dp));
        }
        return dp[n];
    }

    public int numSquares2(int n) {
        // d[i] = min(d[i - j*j] + 1), j in [0, i] && j * j <= i
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }

    /**
     * 补充问题，输出某个数字的所有perfect square组合，使用回溯方法
     */
    public List<List<Integer>> getAllValid(int n) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(res, new ArrayList<>(), n);
        return res.stream().distinct().collect(Collectors.toList());
    }

    void backtrack(List<List<Integer>> res, List<Integer> temp, int n) {
        if (n == 0) {
            List<Integer> result = new ArrayList<>(temp);
            Collections.sort(result, (a, b) -> Integer.compare(b, a));
            if (!res.contains(result)) {
                res.add(result);
            }
        } else {
            int m = (int) Math.sqrt(n);
            for (int j = m; j >= 1; j--) {
                int square = j * j;
                if (n >= square) {
                    temp.add(square);
                    backtrack(res, temp, n - square);
                    temp.remove(temp.size() - 1);
                }
            }
        }
    }

    @Test
    public void test() {
        assertThat(numSquares(1), Matchers.is(1));
        assertThat(numSquares(2), Matchers.is(2));
        assertThat(numSquares(3), Matchers.is(3));
        assertThat(numSquares(4), Matchers.is(1));
        assertThat(numSquares(5), Matchers.is(2));
        assertThat(numSquares(6), Matchers.is(3));
        assertThat(numSquares(7), Matchers.is(4));
        assertThat(numSquares(8), Matchers.is(2));
        assertThat(numSquares(9), Matchers.is(1));
        assertThat(numSquares(16), Matchers.is(1));
        assertThat(numSquares(17), Matchers.is(2));
        assertThat(numSquares(26), Matchers.is(2)); //5^5 + 1
    }

    @Test
    public void test2() {
        List<List<Integer>> res = getAllValid(16);
        res.stream().forEach(System.out::println);
    }
}
