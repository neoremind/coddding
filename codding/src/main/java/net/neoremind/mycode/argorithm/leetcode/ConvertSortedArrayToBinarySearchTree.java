package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given an array where elements are sorted in ascending order, convert it to a height balanced BST.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/
 */
public class ConvertSortedArrayToBinarySearchTree {

    public TreeNode sortedArrayToBST(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    TreeNode helper(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) >> 1;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = helper(nums, start, mid - 1);
        node.right = helper(nums, mid + 1, end);
        return node;
    }

    @Test
    public void test() {
        int[] nums = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
        TreeNode node = sortedArrayToBST(nums);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(node));
    }

}
