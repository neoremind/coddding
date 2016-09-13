package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Implement an iterator over a binary search tree (BST). Your iterator will be initialized with the root node of a BST.
 * <p>
 * Calling next() will return the next smallest number in the BST.
 * <p>
 * Note: next() and hasNext() should run in average O(1) time and uses O(h) memory, where h is the height of the tree.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/binary-search-tree-iterator/
 */
public class BinarySearchTreeIterator {

    class BSTIterator {

        private Stack<TreeNode> stack;

        private TreeNode curr;

        public BSTIterator(TreeNode root) {
            stack = new Stack<>();
            curr = root;
        }

        /**
         * @return whether we have a next smallest number
         */
        public boolean hasNext() {
            return curr != null || !stack.isEmpty();
        }

        /**
         * @return the next smallest number
         */
        public int next() {
            while (curr != null || !stack.isEmpty()) {
                if (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                } else {
                    TreeNode node = stack.pop();
                    //... visit
                    curr = node.right;
                    return node.val;
                }
            }
            throw new RuntimeException("no next left");
        }
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
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        BSTIterator iter = new BSTIterator(root);
        while (iter.hasNext()) {
            System.out.print(iter.next() + ",");
        }

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        iter = new BSTIterator(root);
        while (iter.hasNext()) {
            System.out.print(iter.next() + ",");
        }

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        iter = new BSTIterator(root);
        while (iter.hasNext()) {
            System.out.print(iter.next() + ",");
        }
    }

}
