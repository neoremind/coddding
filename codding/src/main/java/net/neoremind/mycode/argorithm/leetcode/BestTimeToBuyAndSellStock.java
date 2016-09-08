package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * <p>
 * If you were only permitted to complete at most one transaction (ie, buy one and sell one share of the stock),
 * design an algorithm to find the maximum profit.
 * <p>
 * Example 1:
 * Input: [7, 1, 5, 3, 6, 4]
 * Output: 5
 * <p>
 * max. difference = 6-1 = 5 (not 7-1 = 6, as selling price needs to be larger than buying price)
 * Example 2:
 * Input: [7, 6, 4, 3, 1]
 * Output: 0
 * <p>
 * In this case, no transaction is done, i.e. max profit = 0.
 * <p>
 * 用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。 如果只允许进行一次交易，也就是说只允许买一支股票并卖掉，求最大的收益。
 * <p>
 * 动态规划法。从前向后遍历数组，记录当前出现过的最低价格，作为买入价格，并计算以当天价格出售的收益，作为可能的最大收益，整个遍历过程中，出现过的最大收益就是所求。
 * <p>
 * 时间O(n)，空间O(1)
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
 */
public class BestTimeToBuyAndSellStock {

    /**
     * O(n^2)的解法，leetcode直接TLE
     */
    public int maxProfit(int[] prices) {
        //d[i, j] = Max(Vj - Vi) (0<=i<j)
        int[] dp = new int[prices.length];
        for (int i = 0; i < prices.length - 1; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = i; j < prices.length; j++) {
                max = Math.max(max, prices[j] - prices[i]);
            }
            dp[i] = max;
        }
        int pro = Integer.MIN_VALUE;
        for (int i = 0; i < dp.length; i++) {
            pro = Math.max(pro, dp[i]);
        }
        if (pro <= 0) {
            return 0;
        } else {
            return pro;
        }
    }

    /**
     * O(N)解法。
     * 状态转移方程：
     * <pre>
     *     d(j) = Vj - Min(Vi, i in [0, j-1])
     * </pre>
     * 求最大的d(j)即可。
     */
    public int maxProfit2(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxPro = 0;
        for (int i = 0; i < prices.length; i++) {
            minPrice = Math.min(minPrice, prices[i]);
            maxPro = Math.max(prices[i] - minPrice, maxPro);
        }
        return maxPro;
    }

    /**
     * 用全排列，这些的太复杂了:-(
     */
    public int maxProfit3(int[] prices) {
        List<List<Integer>> result = new ArrayList<>();
        helper(result, new ArrayList<>(2), prices, 0, 0);
        int max = 0;
        for (int i = 0; i < result.size(); i++) {
            List<Integer> twoDay = result.get(i);
            max = Math.max(twoDay.get(1) - twoDay.get(0), max);
        }
        return max;
    }

    private void helper(List<List<Integer>> result, List<Integer> tempList, int[] prices, int start, int count) {
        if (count == 2 && tempList.size() == 2) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = start; i < prices.length; i++) {
                tempList.add(prices[i]);
                helper(result, tempList, prices, i + 1, count + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    @Test
    public void test() {
        int[] prices = new int[] {7, 1, 5, 3, 6, 4};
        assertThat(maxProfit(prices), Matchers.is(5));

        prices = new int[] {7, 6, 4, 3, 1};
        assertThat(maxProfit(prices), Matchers.is(0));

        prices = new int[] {1, 2};
        assertThat(maxProfit(prices), Matchers.is(1));

        prices = new int[] {7, 1, 5, 3, 6, 4};
        assertThat(maxProfit2(prices), Matchers.is(5));

        prices = new int[] {7, 6, 4, 3, 1};
        assertThat(maxProfit2(prices), Matchers.is(0));

        prices = new int[] {1, 2};
        assertThat(maxProfit2(prices), Matchers.is(1));

        prices = new int[] {7, 1, 5, 3, 6, 4};
        assertThat(maxProfit3(prices), Matchers.is(5));

        prices = new int[] {7, 6, 4, 3, 1};
        assertThat(maxProfit3(prices), Matchers.is(0));

        prices = new int[] {1, 2};
        assertThat(maxProfit3(prices), Matchers.is(1));
    }
}
