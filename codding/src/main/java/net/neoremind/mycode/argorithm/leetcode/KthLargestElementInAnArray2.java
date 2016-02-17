package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Find the kth largest element in an unsorted array.
 * Note that it is the kth largest element in the sorted order, not the kth distinct element.
 * <p/>
 * For example,
 * Given [3,2,1,5,6,4] and k = 2, return 5.
 * <p/>
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ array's length.
 * 这个实现在leetcode上击败了99.82%的java。
 * 快速选择 Quick Select
 * 时间 Avg O(N) Worst O(N^2)
 * 空间 O(1)
 * 跟快速排序一个思路。先取一个枢纽值，将数组中小于枢纽值的放在左边，大于枢纽值的放在右边，具体方法是用左右两个指针，
 * 如果他们小于枢纽值则将他们换到对面，一轮过后记得将枢纽值赋回分界点。如果这个分界点是k，说明分界点的数就是第k
 * 个数。否则，如果分界点大于k，则在左半边做同样的搜索。如果分界点小于k，则在右半边做同样的搜索。
 *
 * @author zhangxu
 */
public class KthLargestElementInAnArray2 {

    public int findKthLargest(int[] nums, int k) {
        // 处理两个特殊的情况
        // 找第最后大的，那就是找最小的
        if (nums.length == k) {
            int min = 0;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < nums[min]) {
                    min = i;
                }
            }
            return nums[min];
        }

        // 找第一个大的，那就是最大的了
        if (k == 1) {
            int max = 0;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] > nums[max]) {
                    max = i;
                }
            }
            return nums[max];
        }

        // 先分隔数组，枢纽是mid，左边的全大于mid，右边的全小于mid
        int mid = quickSelect(nums, 0, nums.length - 1);
        int right = nums.length - 1;
        int left = 0;
        while (true) {
            if (mid == k - 1) { // 这里是k-1的原因是，Kth大的，那就是枢纽索引是K-1，否则Kth大的就是枢纽左边中最小的，不是枢纽本身
                break;
            } else if (mid < k - 1) {
                left = mid + 1;  // 缩小左边界
                mid = quickSelect(nums, mid + 1, right);
            } else {
                right = mid - 1;  // 缩小右边界
                mid = quickSelect(nums, left, mid - 1);
            }
        }
        return nums[mid];
    }

    /**
     * 快速选择，参考{@link net.neoremind.mycode.argorithm.sort.QuickSort}来实现
     *
     * @param nums 数组
     * @param low  左边界
     * @param high 右边界
     *
     * @return
     */
    private int quickSelect(int[] nums, int low, int high) {
        int pivot = nums[low];
        int i = low;
        int j = high + 1;
        while (true) {
            while (nums[++i] > pivot) {
                if (i == high) {
                    break;
                }
            }
            while (nums[--j] < pivot) {
                if (j == low) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            swap(nums, i, j);
        }
        swap(nums, low, j);
        return j;
    }

    private void swap(int[] array, int from, int to) {
        int tmp = array[from];
        array[from] = array[to];
        array[to] = tmp;
    }

    @Test
    public void test() {
        int[] nums = new int[] {3, 2, 1, 5, 6, 4};
        int k = findKthLargest(nums, 6);
        System.out.println(k);
        assertThat(k, is(1));

        k = findKthLargest(nums, 5);
        System.out.println(k);
        assertThat(k, is(2));

        k = findKthLargest(nums, 4);
        System.out.println(k);
        assertThat(k, is(3));

        k = findKthLargest(nums, 3);
        System.out.println(k);
        assertThat(k, is(4));

        k = findKthLargest(nums, 2);
        System.out.println(k);
        assertThat(k, is(5));

        k = findKthLargest(nums, 1);
        System.out.println(k);
        assertThat(k, is(6));

        nums = new int[] {5, 2, 4, 1, 3, 6, 0};
        k = findKthLargest(nums, 4);
        System.out.println(k);
        assertThat(k, is(3));

        nums = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3};
        k = findKthLargest(nums, 8);
        System.out.println(k);
        assertThat(k, is(3));

        nums = new int[] {-1, 2, 0};
        k = findKthLargest(nums, 2);
        System.out.println(k);
        assertThat(k, is(0));
    }

}
