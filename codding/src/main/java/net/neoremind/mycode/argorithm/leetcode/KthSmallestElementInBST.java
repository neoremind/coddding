package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.
 * <p>
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
 * <p>
 * Follow up:
 * What if the BST is modified (insert/delete operations) often and you need to find the kth smallest frequently? How
 * would you optimize the kthSmallest routine?
 * <p>
 * Hint:
 * <p>
 * Try to utilize the property of a BST.Show More Hint
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/kth-smallest-element-in-a-bst/
 */
public class KthSmallestElementInBST {

    int counter = 0;

    int num = 0;

    /**
     * 递归中序遍历
     */
    public int kthSmallest(TreeNode root, int k) {
        helper(root, k);
        return num;
    }

    void helper(TreeNode node, int k) {
        if (node.left != null) {
            helper(node.left, k);
        }
        if (++counter == k) {
            num = node.val;
            return;
        }
        if (node.right != null) {
            helper(node.right, k);
        }
    }

    /**
     * 非递归中序遍历
     */
    public int kthSmallest2(TreeNode root, int k) {
        int counter = 0;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode cur = root;

        while (cur != null || !stack.empty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                TreeNode node = stack.pop();
                counter++;
                if (counter == k) {
                    return node.val;
                }
                cur = node.right;
            }
        }

        return -1;
    }

    /**
     * 二分查找
     */
    public int kthSmallest3(TreeNode root, int k) {
        int count = countNodes(root.left);
        if (k <= count) {
            return kthSmallest3(root.left, k);
        } else if (k > count + 1) {
            return kthSmallest3(root.right, k - 1 - count); // 1 is counted as current node
        }

        return root.val;
    }

    public int countNodes(TreeNode n) {
        if (n == null) {
            return 0;
        }

        return 1 + countNodes(n.left) + countNodes(n.right);
    }

    @Test
    public void testLargestNumber() {
        int[] arr1 = new int[] {4, 2, 5, 1, 3, 6, 7};
        TreeNode tree1 = TreeNodeHelper.init(arr1);
        System.out.println(TreeNodeHelper.inorderTraversal(tree1));
        assertThat(kthSmallest(tree1, 3), Matchers.is(3));
        assertThat(kthSmallest2(tree1, 3), Matchers.is(3));
        assertThat(kthSmallest3(tree1, 3), Matchers.is(3));
    }
}
