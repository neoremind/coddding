package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given two binary trees, write a function to check if they are equal or not.
 * <p/>
 * Two binary trees are considered equal if they are structurally identical and the nodes have the same value.
 * <p/>
 * Tag:  Depth-first Search 递归
 */
public class SameTree {

    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p != null && q != null) {
            return p.val == q.val
                    && isSameTree(p.left, q.left)
                    && isSameTree(p.right, q.right);
        } else if (p == null && q == null) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode tree1 = TreeNodeHelper.init(arr1);
        int[] arr2 = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode tree2 = TreeNodeHelper.init(arr2);
        boolean isSame = isSameTree(tree1, tree2);
        System.out.println(isSame);
        assertThat(isSame, is(true));

        arr1 = new int[] {4, 2, 7, 1, 3, 6, 9};
        tree1 = TreeNodeHelper.init(arr1);
        arr2 = new int[] {4, 2, 7, 1, 3, 6, 9, 11};
        tree2 = TreeNodeHelper.init(arr2);
        isSame = isSameTree(tree1, tree2);
        System.out.println(isSame);
        assertThat(isSame, is(false));

        arr1 = new int[] {4, 2, 7, 1, 3, 6, 9};
        tree1 = TreeNodeHelper.init(arr1);
        arr2 = new int[] {4, 2, 7, 999, 3, 6, 9};
        tree2 = TreeNodeHelper.init(arr2);
        isSame = isSameTree(tree1, tree2);
        System.out.println(isSame);
        assertThat(isSame, is(false));

        arr1 = new int[] {49};
        tree1 = TreeNodeHelper.init(arr1);
        arr2 = new int[] {49};
        tree2 = TreeNodeHelper.init(arr2);
        isSame = isSameTree(tree1, tree2);
        System.out.println(isSame);
        assertThat(isSame, is(true));
    }

}
