package net.neoremind.mycode.argorithm.leetcode.treetraversal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree, return the bottom-up level order traversal of its nodes' values. (ie, from left to right,
 * level by level from leaf to root).
 * <p>
 * For example:
 * Given binary tree [3,9,20,null,null,15,7],
 * <pre>
 *     3
 *    / \
 *   9  20
 *  /     \
 * 15     7
 * </pre>
 * return its bottom-up level order traversal as:
 * <pre>
 * [
 * [15,7],
 * [9,20],
 * [3]
 * ]
 * </pre>
 *
 * @author zhangxu
 */
public class BinaryTreeLevelorderII {

    public List<List<Integer>> traverse(TreeNode root) {
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

    public List<List<Integer>> levelOrderBottom2(TreeNode root) {
        if (root == null) {
            return new ArrayList<>(0);
        }
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        levelOrderBottom2(queue, result);
        return result;
    }

    private static void levelOrderBottom2(Queue<TreeNode> queue, List<List<Integer>> result) {
        while (!queue.isEmpty()) {
            List<TreeNode> level = new ArrayList<>();
            while (!queue.isEmpty()) {
                level.add(queue.poll());
            }
            for (TreeNode node : level) {
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            levelOrderBottom2(queue, result);
            result.add(level.stream().map(n -> n.val).collect(Collectors.toList()));
        }
    }


    static void helper(Queue<TreeNode> q, List<List<Integer>> res) {
        if (!q.isEmpty()) {
            int size = q.size();
            List<Integer> tmp = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                tmp.add(node.val);
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }
            }
            helper(q, res);
            res.add(tmp);
        }
    }


    /**
     * <pre>
     *      1
     *    /   \
     *   2     3
     *  / \
     * 4   5
     * </pre>
     * <pre>
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     * </pre>
     * <pre>
     *           4
     *         /   \
     *        2     7
     *       /     / \
     *      1     6   9
     *     / \     \
     *    8  10     5
     * </pre>
     */
    @Test
    public void testLevelorder() {
        testOne("1,2,3,4,5", Lists.newArrayList(4, 5, 2, 3, 1));
        testOne("1", Lists.newArrayList(1));
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(1, 3, 6, 9, 2, 7, 4));
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(8, 10, 5, 1, 6, 9, 2, 7, 4));
    }

    private void testOne(String arr, List<Integer> expected) {
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<List<Integer>> res = traverse(root);
        List<Integer> res2 = res.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        System.out.println("LevelorderII traversal: " + res);
        assertThat(res2, is(expected));
    }

}
