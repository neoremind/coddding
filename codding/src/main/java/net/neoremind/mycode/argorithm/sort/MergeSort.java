package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * 归并排序
 * <p/>
 * 代码来自于<a href="https://www.cs.cmu.edu/~adamchik/15-121/lectures/Sorting%20Algorithms/code/MergeSort.java">CMU</a>
 * 也可以参考<a href="http://algs4.cs.princeton.edu/22mergesort/">PRINCETON</a>,图表示的非常到位
 * <p/>
 * 下面的例子是Top-down implementation
 *
 * @author zhangxu
 */
public class MergeSort extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        int[] tmp = new int[array.length];
        mergeSort(array, tmp, 0, array.length - 1);
    }

    private static void mergeSort(int[] a, int[] tmp, int left, int right) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, tmp, left, center);
            mergeSort(a, tmp, center + 1, right);
            merge(a, tmp, left, center + 1, right);
        }
    }

    private static void merge(int[] a, int[] tmp, int left, int right, int rightEnd) {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while (left <= leftEnd && right <= rightEnd) {
            if (a[left] <= a[right]) {
                tmp[k++] = a[left++];
            } else {
                tmp[k++] = a[right++];
            }
        }

        while (left <= leftEnd)    // Copy rest of first half
        {
            tmp[k++] = a[left++];
        }

        while (right <= rightEnd)  // Copy rest of right half
        {
            tmp[k++] = a[right++];
        }

        // Copy tmp back
        for (int i = 0; i < num; i++, rightEnd--) {
            a[rightEnd] = tmp[rightEnd];
        }
    }

}
