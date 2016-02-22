package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * You are climbing a stair case. It takes n steps to reach to the top.
 * <p/>
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 * <p/>
 * 解题思路：动态规划
 * <p/>
 * 解法1：看做斐波那契数列，问题转化为公式：
 * <pre>
 *             1, n=1
 *     f(n) =  2, n=2
 *             f(n-1) + f(n-2), n>=2
 * </pre>
 * 解法2：递推
 *
 * @author zhangxu
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        return climbStairs(n - 1) + climbStairs(n - 2);
    }

    public int climbStairs2(int n) {
        if (n < 2) {
            return n;
        }
        int dp[] = new int[] {1, 1, 2};
        for (int i = 2; i <= n; i++) {
            dp[2] = dp[0] + dp[1];
            dp[0] = dp[1];
            dp[1] = dp[2];
        }
        return dp[2];
    }

    @Test
    public void test() {
        assertThat(climbStairs(1), Matchers.is(1));
        assertThat(climbStairs(2), Matchers.is(2));
        assertThat(climbStairs(3), Matchers.is(3));
        assertThat(climbStairs(4), Matchers.is(5));
        assertThat(climbStairs(5), Matchers.is(8));
        assertThat(climbStairs(6), Matchers.is(13));

        assertThat(climbStairs2(6), Matchers.is(13));
    }

}
