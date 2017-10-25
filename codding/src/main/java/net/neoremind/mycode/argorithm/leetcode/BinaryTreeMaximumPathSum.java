package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, find the maximum path sum.
 * <p>
 * For this problem, a path is defined as any sequence of nodes from some starting node to any node in the tree along
 * the parent-child connections. The path does not need to go through the root.
 * <p>
 * For example:
 * Given the below binary tree,
 * <p>
 * <pre>
 *   1
 *  / \
 * 2   3
 * </pre>
 * Return 6.
 * <p>
 * 返回树中任意两点路径的最大值。只要两点间有路径联通就可以。
 * 初步一看使用递归，但一直WA。后来发现，没有分清最大值和递归函数的返回值。
 * 在求值函数里面不能把计算出来的最大值作为返回值返回回去，否则会有问题。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/binary-tree-maximum-path-sum/
 */
public class BinaryTreeMaximumPathSum {

    int max;

    public int maxPathSum(TreeNode root) {
        max = Integer.MIN_VALUE;
        maxPathDown(root);
        return max;
    }

    private int maxPathDown(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int left = Math.max(0, maxPathDown(node.left));
        int right = Math.max(0, maxPathDown(node.right));
        max = Math.max(max, left + right + node.val);
        return Math.max(left, right) + node.val;
    }

    /**
     * 求从根到底最长的路径
     */
    public int maxSinglyPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = maxSinglyPathSum(root.left);
        int right = maxSinglyPathSum(root.right);
        return root.val + Math.max(left, right);
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
     *     / \     \
     *    1  3     8
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(maxPathSum(root), is(14));
        System.out.println(maxSinglyPathSum(root));

        root = TreeNodeHelper.init("4,-2,6,1,3,-5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(maxPathSum(root), is(18));
        System.out.println(maxSinglyPathSum(root));

        root = TreeNodeHelper.init("4,-2,6,1,-9,-5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(maxPathSum(root), is(17));
        System.out.println(maxSinglyPathSum(root));

        root = TreeNodeHelper.init("6,4,9,-2,#,-7,-10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(maxPathSum(root), is(21));
        System.out.println(maxSinglyPathSum(root));
    }
}
