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
 * 中序遍历，递归和非递归版本。非递归要求使用DFS。
 *
 * @author zhangxu
 */
public class BinaryTreeInorder {

    class InorderTraversalRecruisively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            doInorder(root, result);
            return result;
        }

        private void doInorder(TreeNode root, List<Integer> result) {
            if (root != null) {
                doInorder(root.left, result);
                result.add(root.val);
                doInorder(root.right, result);
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
                    cur = cur.left;
                } else {
                    TreeNode node = stack.pop();
                    result.add(node.val);  // Add after all left children
                    cur = node.right;
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
        testOne("1,2,3,4,5", Lists.newArrayList(4, 2, 5, 1, 3), orderTraversal);
        testOne("1", Lists.newArrayList(1), orderTraversal);
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(1, 2, 3, 4, 6, 7, 9), orderTraversal);
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(8, 1, 10, 2, 4, 6, 5, 7, 9), orderTraversal);
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

