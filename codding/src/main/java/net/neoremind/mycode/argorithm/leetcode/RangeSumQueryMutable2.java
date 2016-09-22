package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
 * <p>
 * The update(i, val) function modifies nums by updating the element at index i to val.
 * Example:
 * Given nums = [1, 3, 5]
 * <p>
 * sumRange(0, 2) -> 9
 * update(1, 2)
 * sumRange(0, 2) -> 8
 * Note:
 * The array is only modifiable by the update function.
 * You may assume the number of calls to update and sumRange function is distributed evenly.
 * <p>
 * https://leetcode.com/problems/range-sum-query-mutable/
 * <p>
 * 使用segment tree，线段树
 * <p>
 * https://discuss.leetcode.com/topic/29918/17-ms-java-solution-with-segment-tree/2
 *
 * @author zhangxu
 */
public class RangeSumQueryMutable2 {

    /**
     * <pre>
     *                  [0, 2, sum=9]
     *                  /           \
     *         [0, 1,sum=4]         [2, 2,sum=5]
     *          /         \
     *  [0, 0,sum=1]    [1, 1,sum=3]
     * </pre>
     * <p>
     * <pre>
     *                                 [0, 4,sum=25]
     *                                /            \
     *                     [0, 2,sum=9]           [3, 4,sum=16]
     *                     /         \             /           \
     *            [0, 1,sum=4]    [2, 2,sum=5] [3, 3,sum=7]   [4, 4,sum=9]
     *             /     \
     *  [0, 0,sum=1]   [1, 1,sum=3]
     * </pre>
     * <p>
     * <pre>
     *                                  [0, 5,sum=36]
     *                                /              \
     *                     [0, 2,sum=9]               [3, 5,sum=27]
     *                     /         \                 /           \
     *            [0, 1,sum=4]    [2, 2,sum=5]   [3, 4,sum=16]   [5, 5,sum=11]
     *             /     \                       /          \
     *  [0, 0,sum=1]   [1, 1,sum=3]      [3, 3,sum=7]   [4, 4,sum=9]
     * </pre>
     */
    @Test
    public void test() {
        int[] nums = new int[] {1, 3, 5};
        NumArray numArray = new NumArray(nums);
        numArray.preorder();
        assertThat(numArray.sumRange(0, 2), Matchers.is(9));
        assertThat(numArray.sumRange(0, 1), Matchers.is(4));
        numArray.update(1, 2);
        assertThat(numArray.sumRange(1, 2), Matchers.is(7));

        nums = new int[] {1, 3, 5, 7, 9};
        numArray = new NumArray(nums);
        numArray.preorder();
        assertThat(numArray.sumRange(0, 2), Matchers.is(9));

        nums = new int[] {1, 3, 5, 7, 9, 11};
        numArray = new NumArray(nums);
        numArray.preorder();
        assertThat(numArray.sumRange(1, 4), Matchers.is(24));
    }

    public class NumArray {

        class SegmentTreeNode {
            int start, end;
            SegmentTreeNode left, right;
            int sum;

            public SegmentTreeNode(int start, int end) {
                this.start = start;
                this.end = end;
                this.left = null;
                this.right = null;
                this.sum = 0;
            }

            @Override
            public String toString() {
                return "[" + start + ", " + end + ",sum=" + sum + "]";
            }
        }

        public void preorder() {
            Stack<SegmentTreeNode> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                SegmentTreeNode node = stack.pop();
                if (node != null) {
                    System.out.println(node);
                    stack.push(node.right);
                    stack.push(node.left);
                }
            }
        }

        SegmentTreeNode root = null;

        public NumArray(int[] nums) {
            root = buildTree(nums, 0, nums.length - 1);
        }

        private SegmentTreeNode buildTree(int[] nums, int start, int end) {
            if (start > end) {
                return null;
            } else {
                SegmentTreeNode ret = new SegmentTreeNode(start, end);
                if (start == end) {
                    ret.sum = nums[start];
                } else {
                    int mid = start + (end - start) / 2;
                    ret.left = buildTree(nums, start, mid);
                    ret.right = buildTree(nums, mid + 1, end);
                    ret.sum = ret.left.sum + ret.right.sum;
                }
                return ret;
            }
        }

        void update(int i, int val) {
            update(root, i, val);
        }

        void update(SegmentTreeNode root, int pos, int val) {
            if (root.start == root.end) {
                root.sum = val;
            } else {
                int mid = root.start + (root.end - root.start) / 2;
                if (pos <= mid) {
                    update(root.left, pos, val);
                } else {
                    update(root.right, pos, val);
                }
                root.sum = root.left.sum + root.right.sum;
            }
        }

        public int sumRange(int i, int j) {
            return sumRange(root, i, j);
        }

        public int sumRange(SegmentTreeNode root, int start, int end) {
            if (root.end == end && root.start == start) {
                return root.sum;
            } else {
                int mid = root.start + (root.end - root.start) / 2;
                if (end <= mid) {
                    return sumRange(root.left, start, end);
                } else if (start >= mid + 1) {
                    return sumRange(root.right, start, end);
                } else {
                    return sumRange(root.right, mid + 1, end) + sumRange(root.left, start, mid);
                }
            }
        }
    }
}
