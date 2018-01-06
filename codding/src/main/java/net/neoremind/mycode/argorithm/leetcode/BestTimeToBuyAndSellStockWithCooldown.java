package net.neoremind.mycode.argorithm.leetcode;

/**
 * @author xu.zhang
 */
public class BestTimeToBuyAndSellStockWithCooldown {

    public int maxProfit(int[] prices) {
        int prev_sell = 0, sell = 0, prev_buy = Integer.MIN_VALUE, buy = prev_buy;
        for (int p : prices) {
            prev_buy = buy; // prev_buy : buy[i - 1]
            buy = Math.max(prev_buy, prev_sell - p); // prev_sell : sell[i - 2]
            prev_sell = sell; // prev_sell : sell[i - 1]
            sell = Math.max(prev_sell, prev_buy + p); // prev_buy : buy[i - 1]
        }
        return sell;
    }
}
