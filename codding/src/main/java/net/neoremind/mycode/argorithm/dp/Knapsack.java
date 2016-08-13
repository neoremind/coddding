package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 01背包问题，动态规划经典案例
 *
 * @author zhangxu
 */
public class Knapsack {

    @Test
    public void test() {
        int[] price = new int[] {6, 4, 5, 3, 9, 7};
        int[] weights = new int[] {4, 2, 3, 1, 6, 4};
        int sackCapacity = 10;
        int[][] result = getItemsToPick(price, weights, sackCapacity);
        System.out.println(result[6][10]);
        assertThat(result[6][10], Matchers.is(19));

        // 和上面的颠倒了一些顺序，可以看出在item固定的情况下，先去挑谁都无所谓，因为这具有最优自结构的特性，所以结果不变
        price = new int[] {4, 5, 6, 9, 7, 3};
        weights = new int[] {2, 3, 4, 6, 4, 1};
        sackCapacity = 10;
        result = getItemsToPick(price, weights, sackCapacity);
        System.out.println(result[6][10]);
        assertThat(result[6][10], Matchers.is(19));
    }

    /**
     * 输出动态规划的矩阵。01背包的状态转换方程 ：
     * <pre>
     *     f[i,j] = Max{f[i-1, j-Wi] + Wi( j >= Wi ),  f[i-1, j] }
     * </pre>
     * 横坐标是背包可以承载的重量，纵坐标是可以拿的item数量
     * <pre>
     *    0  1  2  3  4  5  6  7  8  9  10
     * 0 [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
     * 1 [0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6]
     * 2 [0, 0, 4, 4, 6, 6, 10, 10, 10, 10, 10]
     * 3 [0, 0, 4, 5, 6, 9, 10, 11, 11, 15, 15]
     * 4 [0, 3, 4, 7, 8, 9, 12, 13, 14, 15, 18]
     * 5 [0, 3, 4, 7, 8, 9, 12, 13, 14, 16, 18]
     * 6 [0, 3, 4, 7, 8, 10, 12, 14, 15, 16, 19]
     * </pre>
     *
     * @param price        value of the items
     * @param weights,     weight of the items
     * @param sackCapacity - the maximum weight knapsack can carry
     *
     * @return 动态规划后的计算矩阵
     */
    private static int[][] getItemsToPick(int[] price, int[] weights, int sackCapacity) {
        int nItems = price.length;
        //dp[i][w] - the maximum value of sub problem with i items and with w sack capacity.
        //no need to initialize with zeros as in java, the defalt values are 0 for int premitive type.
        //dpTable index starts at 1, so dpTable[0][..] = 0 and dpTable[..][0] = 0
        int[][] dpTable = new int[nItems + 1][sackCapacity + 1];

        //iterate through all of the items
        for (int i = 1; i <= nItems; i++) {
            //calculate sub problem for all weights
            for (int w = 1; w <= sackCapacity; w++) {
                if (weights[i - 1] > w) {
                    // we cannt take this weight as it exceeds sub problem with weight w and i items
                    dpTable[i][w] = dpTable[i - 1][w];
                } else {
                    dpTable[i][w] = Math.max(price[i - 1] + dpTable[i - 1][w - weights[i - 1]], dpTable[i - 1][w]);
                }
            }
        }
        //printing dpTable
        Arrays.asList(dpTable).stream().forEach(e -> System.out.println(Arrays.toString(e)));
        return dpTable;
    }

}
