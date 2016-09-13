package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a complete binary tree, count the number of nodes.
 * <p>
 * Definition of a complete binary tree from Wikipedia:
 * In a complete binary tree every level, except possibly the last, is completely filled, and all nodes in the last
 * level are as far left as possible. It can have between 1 and 2h nodes inclusive at the last level h.
 * <p>
 * 完全二叉树的数量=2^N - 1，其中N是树的深度
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/count-complete-tree-nodes/
 */
public class CountCompleteTreeNodes {

    public int countNodes(TreeNode root) {
        int rightDepth = rightDepth(root);
        int leftDepth = leftDepth(root);
        if (leftDepth == rightDepth) {
            return (1 << leftDepth) - 1;
        } else {
            return 1 + countNodes(root.left) + countNodes(root.right);
        }
    }

    int rightDepth(TreeNode root) {
        int res = 0;
        while (root != null) {
            res++;
            root = root.right;
        }
        return res;
    }

    int leftDepth(TreeNode root) {
        int res = 0;
        while (root != null) {
            res++;
            root = root.left;
        }
        return res;
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
     *   -2     6
     *  / \   / \
     * 1   3 -5   7
     * </pre>
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      -2     -7   -10
     *     / \
     *    1  3
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(countNodes(root), is(5));

        root = TreeNodeHelper.init("4,-2,6,1,3,-5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(countNodes(root), is(7));

        root = TreeNodeHelper.init("6,4,9,-2,#,-7,-10,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(countNodes(root), is(8));
    }

}
