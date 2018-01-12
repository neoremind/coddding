package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * // 这个方法是“Construct BST from given preorder traversal”的O(n)解法，使用 MIN-MAX 思想，此题还有O(n^2)解法。
 * // 参见 http://www.geeksforgeeks.org/construct-bst-from-given-preorder-traversa/
 * <p>
 * // For example, if the given traversal is {10, 5, 1, 7, 40, 50},
 * // then the output should be root of following tree.
 * //      10
 * //    /   \
 * //   5     40
 * //  /  \      \
 * // 1    7      50
 */
public class ConstructBinaryTreeFromPreorderList {

    public int idx = 0;

    private TreeNode constructBST(int[] pre) {
        return constructBSTfromPreorder(pre, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private TreeNode constructBSTfromPreorder(int[] pre, int min, int max) {
        if (idx >= pre.length) return null;
        if (pre[idx] <= min || pre[idx] >= max) return null;
        TreeNode root = new TreeNode(pre[idx++]);
        root.left = constructBSTfromPreorder(pre, min, root.val);
        root.right = constructBSTfromPreorder(pre, root.val, max);
        return root;
    }

    /**
     * <pre>
     *        4
     *       / \
     *      2   7
     *     / \  /\
     *    1  3 6 9
     * </pre>
     */
    @Test
    public void test() {
        int[] arr1 = new int[]{4, 2, 1, 3, 7, 6, 9};
        TreeNode treeNode1 = constructBST(arr1);
        System.out.println(TreeNodeHelper.preorderTraversal(treeNode1));
    }

}
