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
 * <p/>
 * Levelorder test of the the given binary tree {3,9,20,#,#,15,7},
 * <pre>
 *    3
 *   / \
 *  9  20
 *     / \
 *   15   7
 * </pre>
 * should return its level order traversal as:
 * <pre>
 * [
 * [3],
 * [9,20],
 * [15,7]
 * ]
 * </pre>
 * <p/>
 * Levelorder II test of the given binary tree {3,9,20,#,#,15,7},
 * <pre>
 *    3
 *   / \
 *  9  20
 *     /  \
 *    15   7
 * </pre>
 * return its bottom-up level order traversal as:
 * <pre>
 * [
 * [15,7],
 * [9,20],
 * [3]
 * ]
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

    public static List<List<Integer>> levelorderTraversal(TreeNode root) {
        if (root == null) {
            return new ArrayList<>(0);
        }
        List<List<Integer>> result = new ArrayList<>();
        List<TreeNode> level = new ArrayList<>(1);
        level.add(root);
        List<TreeNode> newLevel;
        List<Integer> levelValues;
        while (true) {
            levelValues = new ArrayList<>(level.size());
            newLevel = new ArrayList<>(level.size() << 1);
            result.add(levelValues);
            for (TreeNode treeNode : level) {
                levelValues.add(treeNode.val);
                if (treeNode.left != null) {
                    newLevel.add(treeNode.left);
                }
                if (treeNode.right != null) {
                    newLevel.add(treeNode.right);
                }
            }
            level = newLevel;
            if (level.isEmpty()) {
                break;
            }
        }
        return result;
    }

    /**
     * aka. levelOrderBottom，利用递归来实现
     */
    public static List<List<Integer>> levelorderTraversal2(TreeNode root) {
        if (root == null) {
            return new ArrayList<>(0);
        }
        List<List<Integer>> result = new ArrayList<>();
        List<TreeNode> level = new ArrayList<>(1);
        level.add(root);
        levelOrderBottom(level, result);
        return result;
    }

    private static void levelOrderBottom(List<TreeNode> level, List<List<Integer>> result) {
        List<TreeNode> newLevel = new ArrayList<>(level.size() << 1);
        List<Integer> levelValues = new ArrayList<>(level.size());
        for (TreeNode treeNode : level) {
            levelValues.add(treeNode.val);
            if (treeNode.left != null) {
                newLevel.add(treeNode.left);
            }
            if (treeNode.right != null) {
                newLevel.add(treeNode.right);
            }
        }
        if (!newLevel.isEmpty()) {
            levelOrderBottom(newLevel, result);
        }
        result.add(levelValues);
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

    @Test
    public void testLevelorder() {
        int[] arr = new int[] {1, 2, 3, 4, 5};
        TreeNode root = TreeNodeHelper.init(arr);
        List<List<Integer>> tmpRes = levelorderTraversal(root);
        List<Integer> res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal: " + res);
        List<Integer> expected = Lists.newArrayList(1, 2, 3, 4, 5);
        assertThat(res, is(expected));

        arr = new int[] {1};
        root = TreeNodeHelper.init(arr);
        tmpRes = levelorderTraversal(root);
        res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal: " + res);
        expected = Lists.newArrayList(1);
        assertThat(res, is(expected));

        arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        root = TreeNodeHelper.init(arr);
        tmpRes = levelorderTraversal(root);
        res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal: " + res);
        expected = Lists.newArrayList(4, 2, 7, 1, 3, 6, 9);
        assertThat(res, is(expected));
    }

    @Test
    public void testLevelorder2() {
        int[] arr = new int[] {1, 2, 3, 4, 5};
        TreeNode root = TreeNodeHelper.init(arr);
        List<List<Integer>> tmpRes = levelorderTraversal2(root);
        List<Integer> res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal II: " + res);
        List<Integer> expected = Lists.newArrayList(4, 5, 2, 3, 1);
        assertThat(res, is(expected));

        arr = new int[] {1};
        root = TreeNodeHelper.init(arr);
        tmpRes = levelorderTraversal2(root);
        res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal II: " + res);
        expected = Lists.newArrayList(1);
        assertThat(res, is(expected));

        arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        root = TreeNodeHelper.init(arr);
        tmpRes = levelorderTraversal2(root);
        res = Lists.newArrayList();
        for (List<Integer> tmp : tmpRes) {
            res.addAll(tmp);
        }
        System.out.println("Levelorder traversal II: " + res);
        expected = Lists.newArrayList(1, 3, 6, 9, 2, 7, 4);
        assertThat(res, is(expected));
    }

}
