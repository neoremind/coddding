package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * <p>
 * Design an algorithm to find the maximum profit. You may complete at most two transactions.
 * <p>
 * Note:
 * You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).<p>
 * <p>
 * 用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。最多交易两次，手上最多只能持有一支股票，求最大收益。
 * <p>
 * 分析：动态规划法。以第i天为分界线，计算第i天之前进行一次交易的最大收益preProfit[i]，和第i天之后进行一次交易的最大收益postProfit[i]，最后遍历一遍，max{preProfit[i] +
 * postProfit[i]} (0≤i≤n-1)就是最大收益。第i天之前和第i天之后进行一次的最大收益求法同Best Time to Buy and Sell Stock I。
 * <p>
 * 代码：时间O(n)，空间O(n)。
 *
 * http://liangjiabin.com/blog/2015/04/leetcode-best-time-to-buy-and-sell-stock.html
 * 题目188 https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv 最多进行K次交易，动态规划的方程比较难写。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii
 */
public class BestTimeToBuyAndSellStockIII {

    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }

        int n = prices.length;
        int[] preProfit = new int[n];
        int[] postProfit = new int[n];

        int curMin = prices[0];
        for (int i = 1; i < n; i++) {
            curMin = Math.min(curMin, prices[i]);
            preProfit[i] = Math.max(preProfit[i - 1], prices[i] - curMin);
        }

        int curMax = prices[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            curMax = Math.max(curMax, prices[i]);
            postProfit[i] = Math.max(postProfit[i + 1], curMax - prices[i]);
        }

        int maxProfit = 0;
        for (int i = 0; i < n; i++) {
            maxProfit = Math.max(maxProfit, preProfit[i] + postProfit[i]);
        }

        return maxProfit;
    }

    @Test
    public void test() {
        int[] prices = new int[] {7, 1, 5, 3, 6, 4};
        assertThat(maxProfit(prices), Matchers.is(7));

        prices = new int[] {7, 6, 4, 3, 1};
        assertThat(maxProfit(prices), Matchers.is(0));

        prices = new int[] {1, 2};
        assertThat(maxProfit(prices), Matchers.is(1));
    }
}
