package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one sorted array.
 * <p>
 * Note:
 * You may assume that nums1 has enough space (size that is greater or equal to m + n) to hold additional elements
 * from nums2. The number of elements initialized in nums1 and nums2 are m and n respectively.
 * <p>
 * 一开始限于从前往后merge的思路里，需要nums1不断的交换和后面的元素，非常难于解决。
 * <p>
 * 如果从后往前走，那么将豁然开朗！
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/merge-sorted-array/
 */
public class MergeSortedArray {

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;
        int j = n - 1;
        int k = m + n - 1;
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                System.out.println(String.format("nums1[%d] = nums1[%d]", k, i));
                nums1[k--] = nums1[i--];
                System.out.println(Arrays.toString(nums1));
            } else {
                System.out.println(String.format("nums1[%d] = nums2[%d]", k, j));
                nums1[k--] = nums2[j--];
                System.out.println(Arrays.toString(nums1));
            }
        }
        while (j >= 0) {
            System.out.println(String.format("nums1[%d] = nums2[%d]", k, j));
            nums1[k--] = nums2[j--];
            System.out.println(Arrays.toString(nums1));
        }
    }

    void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void test() {
        int[] nums1 = new int[] {1, 3, 5, 7, 9, 0, 0, 0, 0, 0};
        int[] nums2 = new int[] {2, 4, 6, 8, 10};
        merge(nums1, 5, nums2, 5);
        assertThat(nums1, Matchers.is(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));

        nums1 = new int[] {3, 5, 7, 10, 11, 0, 0, 0, 0, 0};
        nums2 = new int[] {2, 4, 6, 8, 9};
        merge(nums1, 5, nums2, 5);
        assertThat(nums1, Matchers.is(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));

        nums1 = new int[] {1, 2, 3};
        nums2 = new int[] {};
        merge(nums1, 3, nums2, 0);
        assertThat(nums1, Matchers.is(new int[] {1, 2, 3}));

        nums1 = new int[] {0, 0, 0};
        nums2 = new int[] {1, 2, 3};
        merge(nums1, 0, nums2, 3);
        assertThat(nums1, Matchers.is(new int[] {1, 2, 3}));

        nums1 = new int[] {};
        nums2 = new int[] {};
        merge(nums1, 0, nums2, 0);
        assertThat(nums1, Matchers.is(new int[] {}));
    }
}
