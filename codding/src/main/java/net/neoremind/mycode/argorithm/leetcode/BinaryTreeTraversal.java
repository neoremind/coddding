package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, return the inorder traversal of its nodes' values.
 * <p/>
 * For example:
 * Given binary tree {1,#,2,3},
 * <pre>
 *    1
 *     \
 *      2
 *     /
 *    3
 * </pre>
 * return [1,3,2].
 * <p/>
 * Note: Recursive solution is trivial, could you do it iteratively?
 * <p/>
 * Inorder test of the follwoing tree should be 1, 2, 3, 4, 6, 7, 9
 * <pre>
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 * </pre>
 * <p/>
 * Preorder test of the follwoing tree should be 4, 2, 1, 3, 7, 6, 9
 * <pre>
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 * </pre>
 * 参考资料：http://www.cnblogs.com/dolphin0520/archive/2011/08/25/2153720.html
 */

public class BinaryTreeTraversal {

    public static List<Integer> inorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> ret = new ArrayList<Integer>();
        TreeNode node = root;
        while (!stack.empty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            if (!stack.empty()) {
                node = stack.pop();
                ret.add(node.val);
                node = node.right;
            }
        }
        return ret;
    }

    public static List<Integer> preorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> ret = new ArrayList<Integer>();
        TreeNode node = root;
        while (!stack.empty() || node != null) {
            while (node != null) {
                ret.add(node.val);
                stack.push(node);
                node = node.left;
            }
            if (!stack.empty()) {
                node = stack.pop();
                node = node.right;
            }
        }
        return ret;
    }

    // TODO
    public static List<Integer> postorderTraversal(TreeNode root) {
        return null;
    }

    @Test
    public void testPreorder() {
        int[] arr = new int[] {1, 2, 3, 4, 5};
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<Integer> res = preorderTraversal(root);
        System.out.println("Preorder traversal: " + res);
        List<Integer> expected = Lists.newArrayList(1, 2, 4, 5, 3);
        assertThat(res, is(expected));

        arr = new int[] {1};
        root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        res = preorderTraversal(root);
        System.out.println("Preorder traversal: " + res);
        expected = Lists.newArrayList(1);
        assertThat(res, is(expected));

        arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        res = preorderTraversal(root);
        System.out.println("Preorder traversal: " + res);
        expected = Lists.newArrayList(4, 2, 1, 3, 7, 6, 9);
        assertThat(res, is(expected));
    }

    @Test
    public void testInorder() {
        int[] arr = new int[] {1, 2, 3, 4, 5};
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.inorderTraversal(root));
        List<Integer> res = inorderTraversal(root);
        System.out.println("Inorder traversal: " + res);
        List<Integer> expected = Lists.newArrayList(4, 2, 5, 1, 3);
        assertThat(res, is(expected));

        arr = new int[] {1};
        root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.inorderTraversal(root));
        res = inorderTraversal(root);
        System.out.println("Inorder traversal: " + res);
        expected = Lists.newArrayList(1);
        assertThat(res, is(expected));

        arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.inorderTraversal(root));
        res = inorderTraversal(root);
        System.out.println("Inorder traversal: " + res);
        expected = Lists.newArrayList(1, 2, 3, 4, 6, 7, 9);
        assertThat(res, is(expected));
    }

}
