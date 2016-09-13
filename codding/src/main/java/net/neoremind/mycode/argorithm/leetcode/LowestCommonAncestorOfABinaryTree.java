package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 * <p>
 * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes v and w
 * as the lowest node in T that has both v and w as descendants (where we allow a node to be a descendant of itself).”
 * <p>
 * <pre>
 *        _______3______
 *       /              \
 *   ___5__          ___1__
 *  /      \        /      \
 *  6      _2       0       8
 * /  \
 * 7   4
 * </pre>
 * For example, the lowest common ancestor (LCA) of nodes 5 and 1 is 3. Another example is LCA of nodes 5 and 4 is 5,
 * since a node can be a descendant of itself according to the LCA definition.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
 */
public class LowestCommonAncestorOfABinaryTree {

    /**
     * A Top-Down Approach (Worst case O(n2) ):
     * Let’s try the top-down approach where we traverse the nodes from the top to the bottom. First, if the current
     * node is one of the two nodes, it must be the LCA of the two nodes. If not, we count the number of nodes that
     * matches either p or q in the left subtree (which we call totalMatches). If totalMatches equals 1, then we know
     * the right subtree will contain the other node. Therefore, the current node must be the LCA. If totalMatches
     * equals 2, we know that both nodes are contained in the left subtree, so we traverse to its left child. Similar
     * with the case where totalMatches equals 0 where we traverse to its right child.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        if (root == p || root == q) {
            return root;
        }
        int totalMatches = countMatches(root.left, p, q);
        if (totalMatches == 1) {
            return root;
        } else if (totalMatches == 2) {
            return lowestCommonAncestor(root.left, p, q);
        } else {  //totalMatches = 0
            return lowestCommonAncestor(root.right, p, q);
        }
    }

    int countMatches(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return 0;
        }
        int matches = countMatches(root.left, p, q) + countMatches(root.right, p, q);
        if (root == p || root == q) {
            return 1 + matches;
        } else {
            return matches;
        }
    }

    /**
     * A Bottom-up Approach (Worst case O(n) ):
     * Using a bottom-up approach, we can improve over the top-down approach by avoiding traversing the same nodes
     * over and over again.
     * <p>
     * We traverse from the bottom, and once we reach a node which matches one of the two nodes, we pass it up to its
     * parent. The parent would then test its left and right subtree if each contain one of the two nodes. If yes,
     * then the parent must be the LCA and we pass its parent up to the root. If not, we pass the lower node which
     * contains either one of the two nodes (if the left or right subtree contains either p or q), or NULL (if both
     * the left and right subtree does not contain either p or q) up.
     */
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);
        if (left != null && right != null) {
            return root;
        } else {
            return left != null ? left : right;
        }
    }

    /**
     * <pre>
     *        _______3______
     *       /              \
     *   ___5__          ___1__
     *  /      \        /      \
     *  6      _2       0       8
     * /  \
     * 7   4
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("3,5,1,6,2,0,8,7,4");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        TreeNode ancestor = lowestCommonAncestor(root, root.left.left, root.left.right);
        assertThat(ancestor.val, is(5));

        ancestor = lowestCommonAncestor(root, root.left.left.left, root.left.left.right);
        assertThat(ancestor.val, is(6));

        ancestor = lowestCommonAncestor(root, root.left.right, root.right.right);
        assertThat(ancestor.val, is(3));

        ancestor = lowestCommonAncestor(root, root.left.left, root.left.left.right);
        assertThat(ancestor.val, is(6));
        ///////

        ancestor = lowestCommonAncestor2(root, root.left.left, root.left.right);
        assertThat(ancestor.val, is(5));

        ancestor = lowestCommonAncestor2(root, root.left.left.left, root.left.left.right);
        assertThat(ancestor.val, is(6));

        ancestor = lowestCommonAncestor2(root, root.left.right, root.right.right);
        assertThat(ancestor.val, is(3));

        ancestor = lowestCommonAncestor2(root, root.left.left, root.left.left.right);
        assertThat(ancestor.val, is(6));
    }

}
