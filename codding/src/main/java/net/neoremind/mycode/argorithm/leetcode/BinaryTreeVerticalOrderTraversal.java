package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by
 * column).
 * <p>
 * If two nodes are in the same row and column, the order should be from left to right.
 * <p>
 * Examples:
 * Given binary tree [3,9,20,null,null,15,7],
 * <p>
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * <p>
 * <p>
 * return its vertical order traversal as:
 * <p>
 * [
 * [9],
 * [3,15],
 * [20],
 * [7]
 * ]
 * <p>
 * <p>
 * Given binary tree [3,9,20,4,5,2,7],
 * <p>
 * _3_
 * /   \
 * 9    20
 * / \   / \
 * 4   5 2   7
 * <p>
 * <p>
 * return its vertical order traversal as:
 * <p>
 * [
 * [4],
 * [9],
 * [3,5,2],
 * [20],
 * [7]
 * ]
 *
 * @author xu.zhang
 */
public class BinaryTreeVerticalOrderTraversal {

    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Map<Integer, List<Integer>> col2Val = new HashMap<>();
        if (root == null) return res;
        int min = 0, max = 0;
        Queue<TreeNode> q = new LinkedList<>();
        Queue<Integer> colQ = new LinkedList<>();
        q.add(root);
        colQ.add(0);
        while (!q.isEmpty() && !colQ.isEmpty()) {
            TreeNode node = q.poll();
            int col = colQ.poll();
            if (!col2Val.containsKey(col)) {
                col2Val.put(col, new ArrayList<>());
            }
            col2Val.get(col).add(node.val);
            if (node.left != null) {
                q.add(node.left);
                colQ.add(col - 1);
                min = Math.min(min, col - 1);
            }
            if (node.right != null) {
                q.add(node.right);
                colQ.add(col + 1);
                max = Math.max(max, col + 1);
            }
        }
        for (int i = min; i <= max; i++) {
            res.add(col2Val.get(i));
        }
        return res;
    }

    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("3,9,20,#,#,15,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        System.out.println(verticalOrder(root));
        assertThat(verticalOrder(root).size(), Matchers.is(4));
    }
}
