package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, determine if it is height-balanced.
 * <p>
 * For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the two subtrees
 * of every node never differ by more than 1.
 * <p>
 * Hide Tags: Tree, Depth-first Search
 * <p>
 * 给定一棵二叉树，判定它是否为平衡二叉树。
 * 算法分析：
 * 平衡二叉树（Balanced Binary Tree）又被称为AVL树（有别于AVL算法），
 * 且具有以下性质：它是一棵空树或它的左右两个子树的高度差的绝对值不超过1，
 * 并且左右两个子树都是一棵平衡二叉树。
 * 下面的代码就完全按照定义，首先得到节点左右子树的高度（递归），然后判断左右子树是否为平衡二叉树，
 * 利用递归完成整棵树的判断。完全满足条件，就返回true.
 * 这个解法是DOWN-UP方式的，时间复杂度O(N)。
 * 有另外一种是O(N^2)的，是UP-DOWN方式的。详细可以看discussion。
 *
 * @author zhangxu
 */
public class BalancedBinaryTree {

    public static boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }

        if (getHeight(root) == -1) {
            return false;
        }

        return true;
    }

    public static int getHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int left = getHeight(root.left);
        int right = getHeight(root.right);

        if (left == -1 || right == -1) {
            return -1;
        }

        if (Math.abs(left - right) > 1) {
            return -1;
        }

        return Math.max(left, right) + 1;

    }

    /**
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
    public static void main(String[] args) {
        int[] arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode tree = TreeNodeHelper.init(arr);
        assertThat(isBalanced(tree), Matchers.is(true));

        tree = TreeNodeHelper.init("6,4,9,2,5,8,10,1,3,#,5,#,#,#,5,5");
        assertThat(isBalanced(tree), Matchers.is(true));
    }

}
