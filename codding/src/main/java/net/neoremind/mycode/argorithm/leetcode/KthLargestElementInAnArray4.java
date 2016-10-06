package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

/**
 * Find the kth largest element in an unsorted array.
 * Note that it is the kth largest element in the sorted order, not the kth distinct element.
 * <p>
 * For example,
 * Given [3,2,1,5,6,4] and k = 2, return 5.
 * <p>
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ array's length.
 * <p>
 * Leverage jdk8 lambda, sorting
 * <p>
 * 一行搞定
 *
 * @author zhangxu
 */
public class KthLargestElementInAnArray4 {

    public int findKthLargest(int[] nums, int k) {
        return Arrays.stream(nums).sorted().toArray()[nums.length - k];
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
    }

}
