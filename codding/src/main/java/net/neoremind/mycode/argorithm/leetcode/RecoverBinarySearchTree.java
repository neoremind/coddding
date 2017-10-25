package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Before;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Two elements of a binary search tree (BST) are swapped by mistake.
 * <p>
 * Recover the tree without changing its structure.
 * <p>
 * Note:
 * A solution using O(n) space is pretty straight forward. Could you devise a constant space solution?
 * <p>
 * //TODO Morris Traversal
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/recover-binary-search-tree/
 */
public class RecoverBinarySearchTree {

    TreeNode prev = new TreeNode(Integer.MIN_VALUE);
    TreeNode first = null;
    TreeNode second = null;

    public void recoverTree(TreeNode root) {
        inorder(root);
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }

    void inorder(TreeNode root) {
        if (root == null) {
            return;
        }

        inorder(root.left);

        // Start of "do some business",
        // If first element has not been found, assign it to prevElement (refer to 6 in the example above)
        if (first == null && prev.val >= root.val) {
            first = prev;
        }

        // If first element is found, assign the second element to the root (refer to 2 in the example above)
        if (first != null && prev.val >= root.val) {
            second = root;
        }
        prev = root;

        // End of "do some business"

        inorder(root.right);
    }

    @Before
    public void clear() {
        prev = new TreeNode(Integer.MIN_VALUE);
        first = null;
        second = null;
    }

    /**
     * 2和7位置互换了
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      7     8   10
     *     / \   /
     *    1  3  2
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("6,4,9,7,#,8,10,1,3,2,#");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        recoverTree(root);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(root));
    }

}
