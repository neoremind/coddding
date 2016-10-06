package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, return all root-to-leaf paths.
 * <p>
 * For example, given the following binary tree:
 * <p>
 * <pre>
 *    1
 *  /   \
 * 2     3
 *  \
 *  5
 *  </pre>
 * All root-to-leaf paths are:
 *
 * ["1->2->5", "1->3"]
 *
 * 标准的backtrack回溯，参考{@link NQueens}实现
 *
 * 和{@link PathSum}和{@link PathSumII}类似
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/binary-tree-paths/
 */
public class BinaryTreePaths {

    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        backtrack(root, result, new ArrayList<>());
        return result;
    }

    void backtrack(TreeNode root, List<String> result, List<Integer> tempList) {
        if (root == null) {
            return;
        } else if (root.left == null && root.right == null) {
            tempList.add(root.val);
            result.add(toPathString(tempList));
            tempList.remove(tempList.size() - 1);
        } else {
            tempList.add(root.val);
            backtrack(root.left, result, tempList);
            backtrack(root.right, result, tempList);
            tempList.remove(tempList.size() - 1);
        }
    }

    String toPathString(List<Integer> tempList) {
        return tempList.stream().map(String::valueOf).collect(Collectors.joining("->"));
//        if (tempList == null || tempList.size() == 0) {
//            return "";
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < tempList.size(); i++) {
//            sb.append(tempList.get(i));
//            if (i != tempList.size() - 1) {
//                sb.append("->");
//            }
//        }
//        return sb.toString();
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
        List<String> paths = binaryTreePaths(root);
        System.out.println(paths);

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        paths = binaryTreePaths(root);
        System.out.println(paths);

        root = TreeNodeHelper.init("6,4,9,2,#,7,10,1,3,#,8");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        paths = binaryTreePaths(root);
        System.out.println(paths);
    }

}
