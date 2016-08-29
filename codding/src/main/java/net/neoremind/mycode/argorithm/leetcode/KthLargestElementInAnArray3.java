package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.PriorityQueue;

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
 * 这个实现在leetcode上击败了60%的java。最好的是快速选择，自己实现的优先队列、二叉堆也比原生JDK的数据结构快很多，
 * 但是这个实现比较简洁。
 *
 * @author zhangxu
 * @see https://segmentfault.com/a/1190000003704825
 */
public class KthLargestElementInAnArray3 {

    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> p = new PriorityQueue<Integer>();
        for (int i = 0; i < nums.length; i++) {
            p.add(nums[i]);
            if (p.size() > k) {
                p.poll();
            }
        }
        return p.poll();
    }

    /**
     * 优化后的版本，利用peek函数，比上一个方法快一些
     */
    public int findKthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> p = new PriorityQueue<Integer>();
        for (int i = 0; i < k; i++) {
            p.add(nums[i]);
        }
        for (int i = k; i < nums.length; i++) {
            if (p.peek() < nums[i]) {
                p.add(nums[i]);
                p.poll();
            }
        }
        return p.poll();
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
