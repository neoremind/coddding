package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * <p>
 * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
 * <p>
 * Example 1:
 * nums1 = [1, 3]
 * nums2 = [2]
 * <p>
 * The median is 2.0
 * Example 2:
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 * <p>
 * The median is (2 + 3)/2 = 2.5
 *
 * @author zhangxu
 * @see http://www.07net01.com/2015/07/871155.html
 * @see https://leetcode.com/submissions/detail/69094393/
 */
public class MedianTwoSortedArrays {

    @Test
    public void test() {
        int[] nums1 = new int[] {1, 3, 5, 7, 9};
        int[] nums2 = new int[] {2, 4, 6, 8, 10};
        double res = findMedianSortedArrays(nums1, nums2);
        assertThat(res, Matchers.is(5.5d));
    }

    /**
     * 分治法 Divide and Conquer
     * <p>
     * 题目要求O(log(m+n))的时间复杂度，一般来说都是分治法或者二分搜索。
     * <p>
     * 抽象为“搜索两个有序序列的第k个元素”的问题。
     * <p>
     * 假设序列都是从小到大排列，对于第一个序列中前p个元素和第二个序列中前q个元素，我们想要的最终结果是：p+q等于k-1,
     * 且一序列第p个元素和二序列第q个元素都小于总序列第k个元素。
     * 因为总序列中，必然有k-1个元素小于等于第k个元素。这样第p+1个元素或者第q+1个元素就是我们要找的第k个元素。
     * <p>
     * 边界条件corner conditions非常多，写递归完成，退出条件要定义好。
     * 1）保证arr1的长度较为小
     * 2）arr1为空了，则直接在arr2里面直接索引定位
     * 3）如果k=1，那么直接取arr1和arr2的头元素比较，小的就是要找的。
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int k = (m + n) / 2;
        if ((m + n) % 2 == 0) {
            return (findKth(nums1, nums2, 0, 0, m, n, k) + findKth(nums1, nums2, 0, 0, m, n, k + 1)) / 2;
        } else {
            return findKth(nums1, nums2, 0, 0, m, n, k + 1);
        }

    }

    private double findKth(int[] arr1, int[] arr2, int start1, int start2, int len1, int len2, int k) {
        if (len1 > len2) {
            return findKth(arr2, arr1, start2, start1, len2, len1, k);
        }
        if (len1 == 0) {
            return arr2[start2 + k - 1];
        }
        if (k == 1) {
            return Math.min(arr1[start1], arr2[start2]);
        }
        int p1 = Math.min(k / 2, len1);
        int p2 = k - p1;
        if (arr1[start1 + p1 - 1] < arr2[start2 + p2 - 1]) {
            return findKth(arr1, arr2, start1 + p1, start2, len1 - p1, len2, k - p1);
        } else if (arr1[start1 + p1 - 1] > arr2[start2 + p2 - 1]) {
            return findKth(arr1, arr2, start1, start2 + p2, len1, len2 - p2, k - p2);
        } else {
            return arr1[start1 + p1 - 1];
        }
    }

    /**
     * 归并计数法 Merge and Count，时间复杂度是O(K)，但是K比较大的时候，还是会退换成O(M+N)，即O(N)。
     * <p>
     * 另外最笨的方法就是排序下时间复杂度是O(NLogN)
     * <p>
     * 在leetcode提交这个效率也比较高
     */
    public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int total = len1 + len2;
        if (total % 2 == 0) {
            return (findKth2(nums1, nums2, total / 2) + findKth2(nums1, nums2, total / 2 + 1)) / 2.0;
        } else {
            return findKth2(nums1, nums2, total / 2 + 1);
        }
    }

    private int findKth2(int[] nums1, int[] nums2, int k) {
        int p = 0, q = 0;
        for (int i = 0; i < k - 1; i++) {
            if (p >= nums1.length && q < nums2.length) {
                q++;
            } else if (q >= nums2.length && p < nums1.length) {
                p++;
            } else if (nums1[p] > nums2[q]) {
                q++;
            } else {
                p++;
            }
        }
        if (p >= nums1.length) {
            return nums2[q];
        } else if (q >= nums2.length) {
            return nums1[p];
        } else {
            return Math.min(nums1[p], nums2[q]);
        }
    }

}
