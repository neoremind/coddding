package net.neoremind.mycode.argorithm.leetcode.treetraversal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * 后序遍历，递归和非递归版本。非递归要求使用DFS。
 *
 * @author zhangxu
 */
public class BinaryTreePostorder {

    class PostorderTraversalRecruisively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            doPostorder(root, result);
            return result;
        }

        private void doPostorder(TreeNode root, List<Integer> result) {
            if (root != null) {
                doPostorder(root.left, result);
                doPostorder(root.right, result);
                result.add(root.val);
            }
        }
    }

    /**
     * 非常的巧妙，leetcode上排名靠前的都是这个解法，考虑使用一个链表，先安排最后的节点，也就是根和右节点
     */
    class PostorderTraversalIteratively implements OrderTraversal {

        @Override
        public List<Integer> traverse(TreeNode root) {
            LinkedList<Integer> result = new LinkedList<>();
            Stack<TreeNode> stack = new Stack<>();
            if (root == null) { // 必须处理
                return result;
            }

            stack.push(root);
            while (!stack.isEmpty()) {
                TreeNode cur = stack.pop();
                result.addFirst(cur.val);
                if (cur.left != null) {
                    stack.push(cur.left);
                }
                if (cur.right != null) {
                    stack.push(cur.right);
                }
            }
            return result;
        }
    }

    @Test
    public void testPostorder() {
        testPostorderTemplate(new PostorderTraversalRecruisively());
        testPostorderTemplate(new PostorderTraversalIteratively());
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
    private void testPostorderTemplate(OrderTraversal orderTraversal) {
        System.out.println("Testing against " + orderTraversal.getClass().getSimpleName());
        testOne("1,2,3,4,5", Lists.newArrayList(4, 5, 2, 3, 1), orderTraversal);
        testOne("1", Lists.newArrayList(1), orderTraversal);
        testOne("4,2,7,1,3,6,9", Lists.newArrayList(1, 3, 2, 6, 9, 7, 4), orderTraversal);
        testOne("4,2,7,1,#,6,9,8,10,#,5", Lists.newArrayList(8, 10, 1, 2, 5, 6, 9, 7, 4), orderTraversal);
        System.out.println(StringUtils.repeat("-", 30));
    }

    private void testOne(String arr, List<Integer> expected, OrderTraversal orderTraversal) {
        TreeNode root = TreeNodeHelper.init(arr);
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        List<Integer> res = orderTraversal.traverse(root);
        System.out.println("Postorder traversal: " + res);
        assertThat(res, is(expected));
    }

}
