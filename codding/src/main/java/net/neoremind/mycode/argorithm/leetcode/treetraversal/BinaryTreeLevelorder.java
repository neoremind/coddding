package net.neoremind.mycode.argorithm.leetcode.treetraversal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * 层序遍历，easy
 *
 * @author zhangxu
 */
public class BinaryTreeLevelorder {

    public List<List<Integer>> traverse(TreeNode root) {
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
        testOne("1,2,3,4,5", Lists.newArrayList(1, 2, 3, 4, 5));
        testOne("1", Lists.newArrayList(1));
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(4, 2, 7, 1, 3, 6, 9));
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(4, 2, 7, 1, 6, 9, 8, 10, 5));
    }

    private void testOne(String arr, List<Integer> expected) {
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<List<Integer>> res = traverse(root);
        List<Integer> res2 = res.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        System.out.println("Levelorder traversal: " + res);
        assertThat(res2, is(expected));
    }

}
