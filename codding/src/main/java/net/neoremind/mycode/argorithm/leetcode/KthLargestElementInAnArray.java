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
 * 这个实现在leetcode上击败了95%的java。
 * <p/>
 * 时间 O(NlogK)
 * 空间 O(K)
 * <p/>
 * 另外的解法
 * 1）纯排序：
 * 时间 O(NlogN)，时间不可接受
 * 2）快速选择 Quick Select
 * 时间 Avg O(N) Worst O(N^2)
 * 空间 O(1)
 * 跟快速排序一个思路。先取一个枢纽值，将数组中小于枢纽值的放在左边，大于枢纽值的放在右边，具体方法是用左右两个指针，
 * 如果他们小于枢纽值则将他们换到对面，一轮过后记得将枢纽值赋回分界点。如果这个分界点是k，说明分界点的数就是第k
 * 个数。否则，如果分界点大于k，则在左半边做同样的搜索。如果分界点小于k，则在右半边做同样的搜索。
 *
 * @author zhangxu
 */
public class KthLargestElementInAnArray {

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

        /**
         * 这里非常非常重要的一点，一个陷阱：
         * 最小堆的大小必须是K+1，因为要始终保证在维护最小堆的性质的时候，那个Kth大的元素，不会落在堆顶，
         * 否则就在某些步骤而淘汰出去。
         */
        int[] minHeap = new int[k + 1];
        int index = 0;
        for (int i = 0; i < k + 1; i++, index++) {
            minHeap[i] = nums[i];
        }
        buildHeap(minHeap);  // 初始化建堆
        for (int i = index; i < nums.length; i++) { // 遍历剩余的数组，替换堆顶元素，然后下滤，就是让最小的浮上来。
            minHeap[0] = nums[i];  // 删除操作，一般就会配合下滤来
            percolateDown(minHeap, 0, minHeap.length);
        }
        // 最后堆顶的是Kth + 1小的，那么Kth小的肯定是在第一层了，非1即2，判断下然后返回。
        return minHeap[1] < minHeap[2] ? minHeap[1] : minHeap[2];
    }

    /**
     * 建堆
     */
    private void buildHeap(int[] minHeap) {
        for (int i = minHeap.length / 2; i >= 0; i--) {
            percolateDown(minHeap, i, minHeap.length);
        }
    }

    /**
     * 堆调整，使其生成最小堆，这个操作一般用于删除和建堆
     */
    private void percolateDown(int[] minHeap, int elemIndex, int heapSize) {
        int elemValue = minHeap[elemIndex];
        int leftChildNodeIndex = elemIndex * 2 + 1;
        int rightChildNodeIndex = elemIndex * 2 + 2;

        int minNodeIndex = elemIndex;
        if (leftChildNodeIndex < heapSize && minHeap[leftChildNodeIndex] < elemValue) {
            minNodeIndex = leftChildNodeIndex;
        }

        // 技巧非常重要，minHeap[rightChildNodeIndex] < minHeap[minNodeIndex]，这样才能找孩子最小的出来
        if (rightChildNodeIndex < heapSize && minHeap[rightChildNodeIndex] < minHeap[minNodeIndex]) {
            minNodeIndex = rightChildNodeIndex;
        }

        if (minNodeIndex != elemIndex) {
            swap(minHeap, elemIndex, minNodeIndex);
            percolateDown(minHeap, minNodeIndex, heapSize);
        }
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
    }

}
