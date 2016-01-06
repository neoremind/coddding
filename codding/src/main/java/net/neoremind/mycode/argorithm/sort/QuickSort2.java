package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * 快速排序实现
 * <p/>
 * 参考：<a href="http://www.algolist.net/Algorithms/Sorting/Quicksort">Quicksort</a>
 * <p/>
 * {@link #quickSort(int, int, int[])}中的判断条件很重要，否则执行不成功。
 *
 * @author zhangxu
 */
public class QuickSort2 extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        quickSort(0, array.length - 1, array);
    }

    private void quickSort(int low, int high, int[] array) {
        int mid = partition(low, high, array);
        if (low < mid - 1) {
            quickSort(low, mid - 1, array);
        }
        if (high > mid) {
            quickSort(mid, high, array);
        }
    }

    private int partition(int low, int high, int[] array) {
        int i = low;
        int j = high;
        int mid = low + (high - low) / 2;
        int pivot = array[mid];   // 把数组中间元素当做枢纽
        while (i <= j) {
            while (array[i] < pivot) { // 如果从左边开始的元素值小于枢纽，则向前移动
                i++;
            }
            while (array[j] > pivot) { // 如果从右边开始的元素值大于枢纽，则向前移动
                j--;
            }
            if (i <= j) { // 交换元素
                swap(i, j, array);
                i++;
                j--;
            }
        }
        return i;
    }

}
