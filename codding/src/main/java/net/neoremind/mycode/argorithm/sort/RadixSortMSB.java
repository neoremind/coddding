package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * https://blog.csdn.net/xgf415/article/details/76595887
 * <p>
 * https://blog.csdn.net/u011948899/article/details/78027838
 * <p>
 * // TODO 字节实现的基于MSB思想的基数排序
 */
public class RadixSortMSB extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        doSort(array, array.length);
    }

    public void doSort(int[] array, int n) {
        int max = array[0];
        for (int i = 1; i < n; i++) {
            if (max < array[i])
                max = array[i];
        }
        int maxL = String.valueOf(max).length();  //获取数组中最长元素长度

        int k = new Double(Math.pow(10, maxL - 1)).intValue();
        int[][] t = new int[10][n];  //桶
        int[] num = new int[n];      //记录每个桶中存入数的个数

        for (int a : array) {              //按最高位入桶
            int m = (a / k) % 10;
            t[m][num[m]] = a;
            num[m]++;
        }
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (num[i] == 1) {        //如果桶中只有一个数则直接取出
                array[c++] = t[i][0];
            } else if (num[i] > 1) {   //如果桶中不止一个数，则另存如数组B递归
                int[] B = new int[num[i]];
                for (int j = 0; j < num[i]; j++) {
                    B[j] = t[i][j];
                    doSort(B, num[i]);   //递归方法
                }
            }
        }
    }

}
