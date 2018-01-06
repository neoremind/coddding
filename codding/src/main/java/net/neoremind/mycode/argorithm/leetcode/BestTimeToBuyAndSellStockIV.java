package net.neoremind.mycode.argorithm.leetcode;

/**
 * Say you have an array for which the ith element is the price of a given stock on day i.
 * <p>
 * Design an algorithm to find the maximum profit. You may complete at most k transactions.
 * <p>
 * Note:
 * You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
 *
 * @author xu.zhang
 */
//TODO 还无法理解
public class BestTimeToBuyAndSellStockIV {
    /**
     * dp[i, j] represents the max profit up until prices[j] using at most i transactions.
     * dp[i, j] = max(dp[i, j-1], prices[j] - prices[jj] + dp[i-1, jj]) { jj in range of [0, j-1] }
     * = max(dp[i, j-1], prices[j] + max(dp[i-1, jj] - prices[jj]))
     * dp[0, j] = 0; 0 transactions makes 0 profit
     * dp[i, 0] = 0; if there is only one price data point you can't make any transaction.
     */

    public int maxProfit(int k, int[] prices) {
        int n = prices.length;
        if (n <= 1) return 0;
        if (k >= n / 2) {
            int max = 0;
            for (int i = 1; i < n; i++)
                if (prices[i] > prices[i - 1])
                    max += prices[i] - prices[i - 1];
            return max;
        }
        int[][] dp = new int[k + 1][n];
        for (int i = 1; i <= k; i++) {
            int local = 0;
            for (int j = 1; j < n; j++) {
                local = Math.max(dp[i - 1][j - 1] + Math.max(0, prices[j] - prices[j - 1]), local + prices[j] - prices[j - 1]);
                dp[i][j] = Math.max(dp[i][j - 1], local);
            }
        }
        return dp[k][n - 1];
    }

}
