package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, determine if it is a valid binary search tree (BST).
 * <p>
 * Assume a BST is defined as follows:
 * <p>
 * The left subtree of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 * Example 1:
 * 2
 * / \
 * 1   3
 * Binary tree [2,1,3], return true.
 * Example 2:
 * 1
 * / \
 * 2   3
 * Binary tree [1,2,3], return false.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/validate-binary-search-tree/
 */
public class ValidateBinarySearchTree {

    /**
     * [1,1]过不了OJ，OJ要求返回false
     */
    public boolean isValidBST(TreeNode root) {
        return helper(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    boolean helper(TreeNode node, int max, int min) {
        if (node != null) {
            return (node.val <= max && node.val >= min)
                    && helper(node.left, node.val, min)
                    && helper(node.right, max, node.val);
        }
        return true;
    }

    /**
     * [1,1]过不了OJ，OJ要求返回false
     *
     * @see net.neoremind.mycode.argorithm.leetcode.treetraversal.BinaryTreeInorder.InorderTraversalRecruisively
     */
    public boolean isValidBST2(TreeNode root) {
        TreeNode prev = null;
        return helper2(root, prev);
    }

    boolean helper2(TreeNode node, TreeNode prev) {
        if (node == null) {
            return true;
        }
        if (!helper2(node.left, prev)) {
            return false;
        }
        if (prev != null && prev.val >= node.val) {
            return false;
        }
        prev = node;
        return helper2(node.right, prev);
    }

    /**
     * OJ可以过
     *
     * @see {@link net.neoremind.mycode.argorithm.leetcode.treetraversal.BinaryTreeInorder.InorderTraversalIteratively}
     */
    public boolean isValidBST3(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        TreeNode prev = null;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                if (prev != null && node.val < prev.val) {
                    return false;
                }
                prev = node;
                curr = node.right;
            }
        }
        return true;
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     5
     *  / \
     * 1   3
     * </pre>
     * <pre>
     *      4
     *    /   \
     *   2     6
     *  / \   / \
     * 1   3 5   7
     * </pre>
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      2     7   10
     *     / \     \
     *    1  3     8
     * </pre>
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      2     8   10
     *     / \     \
     *    1  3     7
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(isValidBST(root), is(true));
        assertThat(isValidBST2(root), is(true));
        assertThat(isValidBST3(root), is(true));

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(isValidBST(root), is(true));
        assertThat(isValidBST2(root), is(true));
        assertThat(isValidBST3(root), is(true));

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(isValidBST(root), is(true));
        assertThat(isValidBST2(root), is(true));
        assertThat(isValidBST3(root), is(true));

        root = TreeNodeHelper.init("6,4,9,2,#,8,10,1,3,#,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(isValidBST(root), is(false));
        assertThat(isValidBST2(root), is(false));
        assertThat(isValidBST3(root), is(false));
    }
}
