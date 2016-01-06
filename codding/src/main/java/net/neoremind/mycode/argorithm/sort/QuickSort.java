package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * 快速排序
 * <p/>
 * 使用分治算法策略（divide and conquer）实现。
 * 步骤如下：
 * 1）选择枢纽pivot。可以使第0个元素，也可以是中间的。
 * 2）分组并且交换。将数组一分为二，进行拆分，左边的全小于枢纽，右边的全大于枢纽。
 * 3）递归地再分别继续对左、右两段进行前两部。
 * <p/>
 * The steps are:
 * <p/>
 * 1) Pick an element, called a pivot, from the array.
 * 2) Partitioning: reorder the array so that all elements with values less than the pivot come before the pivot, while
 * all elements with values greater than the pivot come after it (equal values can go either way). After this
 * partitioning, the pivot is in its final position. This is called the partition operation.
 * 3) Recursively apply the above steps to the sub-array of elements with smaller values and separately to the sub-array
 * of elements with greater values.
 * The base case of the recursion is arrays of size zero or one, which never need to be sorted.
 * <p/>
 * 参考：<a href="http://algs4.cs.princeton.edu/23quicksort/Quick.java.html">princeton.edu</a>
 *
 * @author zhangxu
 */
public class QuickSort extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        quickSort(0, array.length - 1, array);
    }

    private void quickSort(int low, int high, int[] array) {
        if (low < high) {
            int mid = partition(low, high, array);
            quickSort(low, mid - 1, array);
            quickSort(mid + 1, high, array);
        }
    }

    private int partition(int low, int high, int[] array) {
        // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
        // and return the index j.
        int i = low;
        int j = high + 1;
        int pivot = array[low];
        while (true) {
            while (array[++i] < pivot) {
                if (i == high) {
                    break;
                }
            }
            while (array[--j] > pivot) {
                if (j == low) {  // redundant since a[low] acts as sentinel?
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swap(i, j, array);
        }
        swap(low, j, array); // put partitioning item v at a[j]
        return j; // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
    }

}
