package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree containing digits from 0-9 only, each root-to-leaf path could represent a number.
 * <p>
 * An example is the root-to-leaf path 1->2->3 which represents the number 123.
 * <p>
 * Find the total sum of all root-to-leaf numbers.
 * <p>
 * For example,
 * <p>
 * <pre>
 *   1
 *  / \
 * 2   3
 * </pre>
 * The root-to-leaf path 1->2 represents the number 12.
 * The root-to-leaf path 1->3 represents the number 13.
 * <p>
 * Return the sum = 12 + 13 = 25.
 * <p>
 * 回溯法 DFS
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/sum-root-to-leaf-numbers/
 */
public class SumRootToLeafNumbers {

    public int sumNumbers(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        backtrack(root, new ArrayList<>(), result);
        return result.stream().reduce(0, (a, b) -> a + b);
    }

    void backtrack(TreeNode root, List<Integer> tempList, List<Integer> result) {
        if (root == null) {
            return;
        } else if (root.left == null && root.right == null) {
            tempList.add(root.val);
            result.add(toInt(tempList));
            tempList.remove(tempList.size() - 1);
        } else {
            tempList.add(root.val);
            backtrack(root.left, tempList, result);
            backtrack(root.right, tempList, result);
            tempList.remove(tempList.size() - 1);
        }
    }

    int toInt(List<Integer> tempList) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : tempList) {
            sb.append(i);
        }
        return Integer.parseInt(sb.toString());
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     5
     *  / \
     * 1   3
     * </pre>
     * <pre>
     *      4
     *    /   \
     *   2     6
     *  / \   / \
     * 1   3 5   7
     * </pre>
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      2     7   10
     *     / \     \
     *    1  3     8
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(sumNumbers(root), is(889));

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(sumNumbers(root), is(1776));

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(sumNumbers(root), is(26732));
    }
}
