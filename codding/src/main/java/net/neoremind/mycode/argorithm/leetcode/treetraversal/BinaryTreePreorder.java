package net.neoremind.mycode.argorithm.leetcode.treetraversal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * 前序遍历，递归和非递归版本。非递归要求使用DFS。
 *
 * @author zhangxu
 */
public class BinaryTreePreorder {

    class PreorderTraversalRecruisively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            doPreorder(root, result);
            return result;
        }

        private void doPreorder(TreeNode root, List<Integer> result) {
            if (root != null) {
                result.add(root.val);
                doPreorder(root.left, result);
                doPreorder(root.right, result);
            }
        }
    }

    class PreorderTraversalIteratively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode top = stack.pop();
                if (top != null) { // 要判空
                    result.add(top.val);
                    stack.push(top.right);
                    stack.push(top.left);
                }
            }
            return result;
        }
    }

    @Test
    public void testPreorder() {
        testPreorderTemplate(new PreorderTraversalRecruisively());
        testPreorderTemplate(new PreorderTraversalIteratively());
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
     *
     * @param orderTraversal
     */
    private void testPreorderTemplate(OrderTraversal orderTraversal) {
        System.out.println("Testing against " + orderTraversal.getClass().getSimpleName());
        testOne("1,2,3,4,5", Lists.newArrayList(1, 2, 4, 5, 3), orderTraversal);
        testOne("1", Lists.newArrayList(1), orderTraversal);
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(4, 2, 1, 3, 7, 6, 9), orderTraversal);
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(4, 2, 1, 8, 10, 7, 6, 5, 9), orderTraversal);
        System.out.println(StringUtils.repeat("-", 30));
    }

    private void testOne(String arr, List<Integer> expected, OrderTraversal orderTraversal) {
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<Integer> res = orderTraversal.traverse(root);
        System.out.println("Preorder traversal: " + res);
        assertThat(res, is(expected));
    }

}
