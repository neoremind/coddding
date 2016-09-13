package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see
 * ordered from top to bottom.
 * <p>
 * For example:
 * Given the following binary tree,
 * <pre>
 *      1            <---
 *    /   \
 *   2     3         <---
 *    \     \
 *     5     4       <---
 * </pre>
 * You should return [1, 3, 4].
 *
 * @author zhangxu
 */
public class BinaryTreeRightSideView {

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, result, 0);
        return result;
    }

    void dfs(TreeNode root, List<Integer> result, int depth) {
        if (root == null) {
            return;
        } else {
            if (result.size() == depth) {  //太巧妙了！！
                result.add(root.val);
            }
            dfs(root.right, result, depth + 1);
            dfs(root.left, result, depth + 1);
        }
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
     *   -2     6
     *  / \   / \
     * 1   3 -5   7
     * </pre>
     * <pre>
     *           6
     *         /   \
     *        4     9
     *       /     / \
     *      -2     -7   -10
     *     / \
     *    1  3
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,5,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(rightSideView(root), is(Lists.newArrayList(4, 5, 3)));

        root = TreeNodeHelper.init("4,-2,6,1,3,-5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(rightSideView(root), is(Lists.newArrayList(4, 6, 7)));

        root = TreeNodeHelper.init("6,4,9,-2,#,-7,-10,1,3");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(rightSideView(root), is(Lists.newArrayList(6, 9, -10, 3)));
    }

}
