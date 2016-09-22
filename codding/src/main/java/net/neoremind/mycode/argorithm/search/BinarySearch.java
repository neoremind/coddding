package net.neoremind.mycode.argorithm.search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ClassName: BinarySearch </br> Function: 二分查找
 * <p>
 * http://algs4.cs.princeton.edu/11model/BinarySearch.java.html
 *
 * @author Zhang Xu
 */
public class BinarySearch {

    /**
     * 查找方法，递归或者循环
     */
    enum SearchMethod {
        RECURSIVE, LOOP;
    }

    /**
     * 假设数组有序，查找入口,没有查找到返回-1
     *
     * @param arr
     * @param target
     * @param method
     *
     * @return
     */
    public int search(int[] arr, int target, SearchMethod method) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        if (method == SearchMethod.RECURSIVE) {
            return searchRecursive(arr, 0, arr.length - 1, target);
        } else if (method == SearchMethod.LOOP) {
            return searchLoop(arr, 0, arr.length - 1, target);
        }
        throw new IllegalArgumentException("SearchMethod not specified.");
    }

    /**
     * 递归查找
     *
     * @param arr
     * @param start
     * @param end
     * @param target
     *
     * @return
     */
    private int searchRecursive(int[] arr, int start, int end, int target) {
        int mid = (start + end) / 2;
        if (start > end) {
            return -1;
        }
        if (arr[mid] > target) {
            return searchRecursive(arr, start, mid - 1, target);
        } else if (arr[mid] < target) {
            return searchRecursive(arr, mid + 1, end, target);
        } else {
            return mid;
        }
    }

    /**
     * 循环查找，参考<code>Arrays.binarySearch(arr, 4);</code>实现
     *
     * @param arr
     * @param start
     * @param end
     * @param target
     *
     * @return
     */
    private int searchLoop(int[] arr, int start, int end, int target) {
        while (start <= end) {
            int mid = (start + end) >>> 1;
            int midVal = arr[mid];

            if (midVal < target) {
                start = mid + 1;
            } else if (midVal > target) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private int searchLoop2(int[] arr, int start, int end, int target) {
        while (start < end) {
            int mid = (start + end) >>> 1;
            int midVal = arr[mid];

            if (midVal < target) {
                start = mid + 1;
            } else if (midVal > target) {
                end = mid - 1;
            } else {
                return mid;
            }
        }
        return arr[start] == target ? start : -1;
    }

    public static void main(String[] args) {
        /**
         * 验证start + end >>> 2 和 (start + end) / 2 相等
         */
        int start = 0;
        int end = 9;
        assertThat((start + end) / 2, is((start + end) >>> 1));

        start = 0;
        end = 10;
        assertThat((start + end) / 2, is((start + end) >>> 1));

        BinarySearch bs = new BinarySearch();
        int[] arr = new int[] {1, 3, 5, 6, 9, 11, 20};
        int index = bs.search(arr, 4, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 0, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 99, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 1, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(0));

        index = bs.search(arr, 5, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(2));

        index = bs.search(arr, 20, SearchMethod.RECURSIVE);
        System.out.println(index);
        assertThat(index, is(6));

        index = bs.search(arr, 4, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 0, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 99, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(-1));

        index = bs.search(arr, 1, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(0));

        index = bs.search(arr, 5, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(2));

        index = bs.search(arr, 20, SearchMethod.LOOP);
        System.out.println(index);
        assertThat(index, is(6));

    }

}
