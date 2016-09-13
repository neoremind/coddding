package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree and a sum, determine if the tree has a root-to-leaf path such that adding up all the values
 * along the path equals the given sum.
 * <p>
 * For example:
 * Given the below binary tree and sum = 22,
 * <pre>
 *       5
 *      / \
 *     4   8
 *    /   / \
 *   11  13  4
 *  /  \      \
 * 7    2      1
 * </pre>
 * return
 * [
 * [5,4,11,2],
 * [5,8,4,5]
 * ]
 * <p>
 * 解题思路：递归、深度优先搜索
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/path-sum-ii/
 */
public class PathSumII {

    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        helper(root, sum, new ArrayList<>(), result);
        return result;
    }

    private void helper(TreeNode node, int sum, List<Integer> tempList, List<List<Integer>> result) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null && sum - node.val == 0) {
            tempList.add(node.val);
            result.add(new ArrayList<>(tempList));
            tempList.remove(tempList.size() - 1);
            return;
        }

        tempList.add(node.val);
        helper(node.left, sum - node.val, tempList, result);
        tempList.remove(tempList.size() - 1);

        tempList.add(node.val);
        helper(node.right, sum - node.val, tempList, result);
        tempList.remove(tempList.size() - 1);
    }

    /**
     * <pre>
     *         5
     *        / \
     *       4   8
     *      /   / \
     *     11  13  4
     *    /  \    / \
     *   7    2  5   1
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("5,4,8,11,#,13,4,7,2,#,#,5,1");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        System.out.println(pathSum(root, 22));

        root = TreeNodeHelper.init("-2,#,-3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        System.out.println(pathSum(root, -5));
    }
}
