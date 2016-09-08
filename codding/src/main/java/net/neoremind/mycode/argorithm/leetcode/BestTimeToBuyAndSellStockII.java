package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * <p>
 * Design an algorithm to find the maximum profit. You may complete as many transactions as you like (ie, buy one and
 * sell one share of the stock multiple times). However, you may not engage in multiple transactions at the same time
 * (ie, you must sell the stock before you buy again).
 * <p>
 * 用一个数组表示股票每天的价格，数组的第i个数表示股票在第i天的价格。交易次数不限，但一次只能交易一支股票，也就是说手上最多只能持有一支股票，求最大收益。
 * <p>
 * 贪心法。从前向后遍历数组，只要当天的价格高于前一天的价格，就算入收益。
 * <p>
 * 时间O(n)，空间O(1)。
 * <p>
 * 注意这里是T+0的交易
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
public class BestTimeToBuyAndSellStockII {

    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int diff = prices[i] - prices[i - 1];
            if (diff > 0) {
                maxProfit += diff;
            }
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
