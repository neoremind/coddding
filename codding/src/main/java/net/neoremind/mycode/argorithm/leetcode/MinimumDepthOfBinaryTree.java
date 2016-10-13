package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, find its minimum depth.
 * <p>
 * The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/minimum-depth-of-binary-tree/
 */
public class MinimumDepthOfBinaryTree {

    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null || root.right == null) {
            return 1 + Math.max(minDepth(root.left), minDepth(root.right));
        }
        return 1 + Math.min(minDepth(root.left), minDepth(root.right));
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
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /\     / \
     *      2  5   8   10
     *     / \  \       \
     *    1  3  5        5
     *   /
     *  5
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(minDepth(root), is(2));

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(minDepth(root), is(3));

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(minDepth(root), is(3));

        root = TreeNodeHelper.init("6,4,9,2,5,8,10,1,3,#,5,#,#,#,5,5");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(minDepth(root), is(3));
    }
}
