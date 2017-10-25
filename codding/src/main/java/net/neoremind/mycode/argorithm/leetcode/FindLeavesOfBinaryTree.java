package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given a binary tree, collect a tree's nodes as if you were doing this: Collect and remove all leaves, repeat until
 * the tree is empty.
 * <p>
 * Example:
 * Given binary tree
 * <p>
 * 1
 * / \
 * 2   3
 * / \
 * 4   5
 * Returns [4, 5, 3], [2], [1].
 *
 * @author xu.zhang
 */
public class FindLeavesOfBinaryTree {

    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        helper(result, root);
        return result;
    }

    private int helper(List<List<Integer>> result, TreeNode root) {
        if (root == null) return 0;
        int depth = 1 + Math.max(helper(result, root.left), helper(result, root.right));
        if (result.size() < depth) {
            result.add(new ArrayList<>());
        }
        result.get(depth - 1).add(root.val);
        return depth;
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     6
     *  / \   / \
     * 1   3 5   7
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        System.out.println(findLeaves(root));
    }

}
