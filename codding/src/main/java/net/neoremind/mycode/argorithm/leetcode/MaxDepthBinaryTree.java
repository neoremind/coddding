package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given a binary tree, find its maximum depth.
 * <p/>
 * The maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.
 */
public class MaxDepthBinaryTree {

    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth;
        int rightDepth;
        leftDepth = maxDepth(root.left) + 1;
        rightDepth = maxDepth(root.right) + 1;

        if (leftDepth >= rightDepth) {
            return leftDepth;
        } else {
            return rightDepth;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[]{4, 2, 7, 1, 3, 6, 9};
        TreeNode root = TreeNodeHelper.init(arr);
        int maxDepth = maxDepth(root);
        System.out.println(maxDepth);
        assertThat(maxDepth, is(3));

        arr = new int[]{4, 2, 7, 1, 3, 6, 9, 11};
        root = TreeNodeHelper.init(arr);
        maxDepth = maxDepth(root);
        System.out.println(maxDepth);
        assertThat(maxDepth, is(4));

        arr = new int[]{0};
        root = TreeNodeHelper.init(arr);
        maxDepth = maxDepth(root);
        System.out.println(maxDepth);
        assertThat(maxDepth, is(1));
    }

}
