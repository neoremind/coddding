package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * You are given an integer array nums and you have to return a new counts array. The counts array has the property
 * where counts[i] is the number of smaller elements to the right of nums[i].
 * <p>
 * Example:
 * <p>
 * Given nums = [5, 2, 6, 1]
 * <p>
 * To the right of 5 there are 2 smaller elements (2 and 1).
 * To the right of 2 there is only 1 smaller element (1).
 * To the right of 6 there is 1 smaller element (1).
 * To the right of 1 there is 0 smaller element.
 * Return the array [2, 1, 1, 0].
 * https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * <p>
 * 方法1：BST（二分查找树）
 * 树节点TreeNode记录下列信息：
 * <p>
 * 元素值：val
 * 小于该节点的元素个数：leftCnt
 * 节点自身的元素个数：cnt
 * 左孩子：left
 * 右孩子：right
 * https://discuss.leetcode.com/topic/31405/9ms-short-java-bst-solution-get-answer-when-building-bst
 * <p>
 * 方法2：DAC 归并排序merge sort
 * https://discuss.leetcode.com/topic/31554/11ms-java-solution-using-merge-sort-with-explanation
 * <p>
 * 方法3：排序后二分查找binary search left-most insertion position
 * {@link #countSmaller(int[])}结合List.add(index, value)这个操作。
 * <p>
 * 方法4：树状数组 （Binary Indexed Tree / Fenwick Tree）
 * https://discuss.leetcode.com/topic/39656/short-java-binary-index-tree-beat-97-33-with-detailed-explanation/2
 * <p>
 * https://www.topcoder.com/community/data-science/data-science-tutorials/binary-indexed-trees/
 * {@link RangeSumQueryMutable}
 * <p>
 * 错误解法：DP
 * {@link net.neoremind.mycode.argorithm.dp.SmallerNumber}
 *
 * @author zhangxu
 */
public class CountOfSmallerNumbersAfterSelf {

    public List<Integer> countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>(0);
        }
        Integer[] res = new Integer[nums.length];
        List<Integer> sorted = new ArrayList<>(nums.length);
        for (int i = nums.length - 1; i >= 0; i--) {
            int idx = findIndex(nums[i], sorted);
            sorted.add(idx, nums[i]);
            res[i] = idx;
        }
        return Arrays.asList(res);
    }

    int findIndex(int target, List<Integer> sorted) {
        if (sorted.size() == 0) {
            return 0;
        }
        int start = 0;
        int end = sorted.size() - 1;
        if (sorted.get(end) < target) {  //判断一下边界更好些
            return end + 1;
        }
        if (sorted.get(start) >= target) {
            return 0;
        }
        while (start <= end) {
            int mid = start + ((end - start) >> 1);
            int midVal = sorted.get(mid);
            if (target < midVal) {
                end = mid - 1;
            } else if (target > midVal) {
                start = mid + 1;
            } else {
                int idx = mid; //这里非常重要！！！否则结果又的总会比预期的大一点，要处理相同，找到最前面的那个位置，比如3，3，6，6，7，7，7，应该是要返回第一个6的位置，而不是第二个
                while (idx - 1 >= start && sorted.get(idx - 1) == target) {
                    idx--;
                }
                return idx;
            }
        }
        return start;
    }

    public List<Integer> countSmaller2(int[] nums) {
        List<Integer> res = new LinkedList<Integer>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        // find min value and minus min by each elements, plus 1 to avoid 0 element
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            min = (nums[i] < min) ? nums[i] : min;
        }
        int[] nums2 = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i] - min + 1;  //从开始
            max = Math.max(nums2[i], max);
        }
        int[] tree = new int[max + 1];
        for (int i = nums2.length - 1; i >= 0; i--) {
            res.add(0, get(nums2[i] - 1, tree));
            update(nums2[i], tree);
        }
        return res;
    }

    private int get(int i, int[] tree) {
        int num = 0;
        while (i > 0) {
            num += tree[i];
            i -= i & (-i);
        }
        return num;
    }

    private void update(int i, int[] tree) {
        while (i < tree.length) {
            tree[i]++;
            i += i & (-i);
        }
    }

    @Test
    public void test() {
        int[] a = new int[] {1, 2};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(0, 0)));
        assertThat(countSmaller2(a), Matchers.is(Lists.newArrayList(0, 0)));

        a = new int[] {2, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(1, 0)));

        a = new int[] {9, 5, 3, 7};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(3, 1, 0, 0)));

        a = new int[] {5, 2, 6, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(2, 1, 1, 0)));

        a = new int[] {5, 2, 6, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(2, 1, 1, 0)));

        a = new int[] {5, 2, 6, 5, 1};
        assertThat(countSmaller(a), Matchers.is(Lists.newArrayList(2, 1, 2, 1, 0)));
    }

}
