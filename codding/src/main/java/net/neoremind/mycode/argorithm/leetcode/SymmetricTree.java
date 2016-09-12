package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.Queue;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
 * <p>
 * For example, this binary tree [1,2,2,3,4,4,3] is symmetric:
 * <p>
 * 1
 * / \
 * 2   2
 * / \ / \
 * 3  4 4  3
 * But the following [1,2,2,null,3,null,3] is not:
 * 1
 * / \
 * 2   2
 * \   \
 * 3    3
 * Note:
 * Bonus points if you could solve it both recursively and iteratively.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/symmetric-tree/
 */
public class SymmetricTree {

    /**
     * Recursive
     */
    public boolean isSymmetric1(TreeNode root) {
        return root == null || helper(root.left, root.right);
    }

    public boolean helper(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        } else if (node1 != null && node2 != null) {
            return node1.val == node2.val
                    && helper(node1.left, node2.right)
                    && helper(node1.right, node2.left);
        } else {
            return false;
        }
    }

    public boolean helper2(TreeNode p, TreeNode q) {
        if (p == null || q == null) {
            return p == q;
        } else {
            return p.val == q.val && helper(p.left, q.right)
                    && helper(p.right, q.left);
        }
    }

    /**
     * Non-recursive(use Stack)
     */
    public boolean isSymmetric2(TreeNode root) {
        if (root == null) {
            return true;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root.left);
        queue.add(root.right);
        while (queue.size() > 1) {
            TreeNode node1 = queue.poll();
            TreeNode node2 = queue.poll();
            if (node1 == null && node2 == null) {
                continue;
            } else if (node1 != null && node2 != null) {
                if (node1.val != node2.val) {
                    return false;
                }
            } else {
                return false;
            }
            queue.add(node1.left);
            queue.add(node2.right);
            queue.add(node1.right);
            queue.add(node2.left);
        }
        return true;
    }

    @Test
    public void test() {
        int[] arr1 = new int[] {1, 2, 2, 3, 4, 4, 3};
        TreeNode tree1 = TreeNodeHelper.init(arr1);
        System.out.println(TreeNodeHelper.preorderTraversal(tree1));
        assertThat(isSymmetric1(tree1), Matchers.is(true));
        assertThat(isSymmetric2(tree1), Matchers.is(true));
    }

}
