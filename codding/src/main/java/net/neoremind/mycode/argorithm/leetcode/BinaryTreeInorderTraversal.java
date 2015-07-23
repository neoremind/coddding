package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
 */
public class BinaryTreeInorderTraversal {

    public static List<Integer> inorderTraversal(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        List<Integer> ret = new ArrayList<Integer>();
        TreeNode node = root;
        stack.push(node);
        Map<TreeNode, Boolean> signMap = new HashMap<TreeNode, Boolean>();
        while (!stack.empty() && node != null) {
            if (node.left != null) {
                stack.push(node);
                node = node.left;
            } else {
                ret.add(node.val);
                signMap.put(node, true);
                node = stack.pop();
                ret.add(node.val);
                signMap.put(node, true);
                node = node.right;
                if (node != null) {
                    stack.push(node);
                } else {
                    node = stack.pop();
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode root = TreeNodeHelper.init(arr);
        List<Integer> res = inorderTraversal(root);
        System.out.println(res);
        List<Integer> expected = Lists.newArrayList(1, 3, 2);
        assertThat(res, is(expected));
    }

}
