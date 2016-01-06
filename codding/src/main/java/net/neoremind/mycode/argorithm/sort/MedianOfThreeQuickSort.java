package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * Quick sort with median-of-three partitioning
 * <p/>
 * 参考：<a href="http://www.java2s.com/Tutorial/Java/0140__Collections/Quicksortwithmedianofthreepartitioning.htm">网页</a>
 * <p/>
 * 选取枢纽元pivot非常重要，通常选取第0个元素非常糟糕，如果数组是随机的可以接受，如果是预排序或者
 * 反序的，那么这样的枢纽将会造成劣质的分隔partition，所有元素不是被划入左侧就是都划入右侧。如果是预排序的，
 * 那么时间复杂度退化成O(N2)。
 * 另外，随机选择也不好，随机数的生成开销也不小。
 * 一种比较好的方式是采用三数中值分割法（Median-of-Three Partitioning）。
 * 下面的例子有两个优化的trick：
 * 1）枢纽元选择后，被放在了high-1的位置。
 * 2）比较小的元素时，采用手工排序，另外一种比较好的方式是采用插入排序，一般是元素小于10个的时候。
 *
 * @author zhangxu
 */
public class MedianOfThreeQuickSort extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        quickSort(0, array.length - 1, array);
    }

    private void quickSort(int low, int high, int[] array) {
        int size = high - low + 1;
        if (size <= 3) {
            manualSort(low, high, array);
        } else {
            int mid = partition(low, high, array);
            quickSort(low, mid - 1, array);
            quickSort(mid + 1, high, array);
        }
    }

    private int partition(int low, int high, int[] array) {
        int pivot = medianOf3(low, high, array);
        int i = low;
        int j = high - 1;
        while (true) {
            while (array[++i] < pivot) {
            }
            while (array[--j] > pivot) {
            }
            if (i < j) {
                swap(i, j, array);
            } else {
                break;
            }
        }
        swap(i, high - 1, array);
        return i;
    }

    protected int medianOf3(int left, int right, int[] array) {
        int center = (left + right) / 2;

        if (array[left] > array[center]) {
            swap(left, center, array);
        }

        if (array[left] > array[right]) {
            swap(left, right, array);
        }

        if (array[center] > array[right]) {
            swap(center, right, array);
        }

        swap(center, right - 1, array);
        return array[right - 1];
    }

    public void manualSort(int left, int right, int[] array) {
        int size = right - left + 1;
        if (size <= 1) {
            return;
        }
        if (size == 2) {
            if (array[left] > array[right]) {
                swap(left, right, array);
            }
        } else {
            if (array[left] > array[right - 1]) {
                swap(left, right - 1, array);
            }
            if (array[left] > array[right]) {
                swap(left, right, array);
            }
            if (array[right - 1] > array[right]) {
                swap(right - 1, right, array);
            }
        }
    }

}
