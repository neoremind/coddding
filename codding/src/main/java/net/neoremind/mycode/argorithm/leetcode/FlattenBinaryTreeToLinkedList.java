package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, flatten it to a linked list in-place.
 * <p>
 * For example,
 * Given
 * <p>
 * <pre>
 *     1
 *    / \
 *   2   5
 *  / \   \
 * 3   4   6
 * </pre>
 * The flattened tree should look like:
 * <pre>
 * 1
 *  \
 *  2
 *   \
 *   3
 *    \
 *    4
 *    \
 *    5
 *     \
 *     6
 * </pre>
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/flatten-binary-tree-to-linked-list/
 */
public class FlattenBinaryTreeToLinkedList {

    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode left = root.left;
        TreeNode right = root.right;

        flatten(left);
        flatten(right);

        root.right = left;
        root.left = null;
        TreeNode curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }

        curr.right = right;
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
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        flatten(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        flatten(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        flatten(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));

        root = TreeNodeHelper.init("6,4,9,2,#,8,10,1,3,#,7");
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        flatten(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));

        root = TreeNodeHelper.init("1,2,#");
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        flatten(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
    }
}
