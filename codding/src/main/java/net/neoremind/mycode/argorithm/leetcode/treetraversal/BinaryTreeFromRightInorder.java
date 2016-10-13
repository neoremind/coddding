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
 * 从右往左的中序遍历，递归和非递归版本。非递归要求使用DFS。
 *
 * @author zhangxu
 */
public class BinaryTreeFromRightInorder {

    class InorderTraversalRecruisively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            doInorder(root, result);
            return result;
        }

        private void doInorder(TreeNode root, List<Integer> result) {
            if (root != null) {
                doInorder(root.right, result);
                result.add(root.val);
                doInorder(root.left, result);
            }
        }
    }

    /**
     * 有点技巧，第一次没写对，stackoverflow了
     */
    class InorderTraversalIteratively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<Integer>();

            Stack<TreeNode> stack = new Stack<TreeNode>();
            TreeNode cur = root;

            while (cur != null || !stack.empty()) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.right;
                } else {
                    TreeNode node = stack.pop();
                    result.add(node.val);  // Add after all left children
                    cur = node.left;
                }
            }

            return result;
        }
    }

    @Test
    public void testInorder() {
        testInorderTemplate(new InorderTraversalRecruisively());
        testInorderTemplate(new InorderTraversalIteratively());
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
    private void testInorderTemplate(OrderTraversal orderTraversal) {
        System.out.println("Testing against " + orderTraversal.getClass().getSimpleName());
        testOne("1,2,3,4,5", Lists.newArrayList(3, 1, 5, 2, 4), orderTraversal);
        testOne("1", Lists.newArrayList(1), orderTraversal);
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(9, 7, 6, 4, 3, 2, 1), orderTraversal);
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(9, 7, 5, 6, 4, 2, 10, 1, 8),
                orderTraversal);
        System.out.println(StringUtils.repeat("-", 30));
    }

    private void testOne(String arr, List<Integer> expected, OrderTraversal orderTraversal) {
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<Integer> res = orderTraversal.traverse(root);
        System.out.println("Inorder traversal: " + res);
        assertThat(res, is(expected));
    }

}

