package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertThat;

/**
 * Given a non-empty binary search tree and a target value, find the value in the BST that is closest to the target.
 * <p>
 * Note:
 * <p>
 * Given target value is a floating point.
 * You are guaranteed to have only one unique value in the BST that is closest to the target.
 * <p>
 * 方法1：in-order遍历，一个一个的比较，维护一个最小值，不停的更新，直到找到临界点。
 * 方法2：下面这种递归的写法和上面迭代的方法思路相同，都是根据二分搜索树的性质来优化查找，但是递归的写法用的是回溯法，先遍历到叶节点，然后一层一层的往回走，把最小值一层一层的运回来
 * 方法3：和方法2类似的二分思想，实际我们可以利用二分搜索树的特点(左<根<右)
 * 来快速定位，由于根节点是中间值，我们在往下遍历时，我们根据目标值和根节点的值大小关系来比较，如果目标值小于节点值，则我们应该找更小的值，于是我们到左子树去找，反之我们去右子树找
 *
 * @author xu.zhang
 */
public class ClosestBinarySearchTreeValue {

    int closestValue(TreeNode root, double target) {
        if (root == null) {
            return 0;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        boolean flag = false;
        TreeNode pre = null;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                if (flag) {
                    if (target >= pre.val && target <= node.val) {
                        return Math.abs(target - pre.val) < Math.abs(target - node.val) ? pre.val : node.val;
                    }
                } else {
                    flag = true;
                }
                pre = node;
                curr = node.right;
            }
        }
        return pre.val;
    }

    int closestValue2(TreeNode root, double target) {
        if (root == null) {
            return 0;
        }
        TreeNode child = root.val < target ? root.right : root.left;
        if (child == null) {
            return root.val;
        }
        int childVal = closestValue2(child, target);
        return Math.abs(target - childVal) < Math.abs(target - root.val) ? childVal : root.val;
    }

    int closestValue3(TreeNode root, double target) {
        int ret = root.val;
        while (root != null) {
            if (Math.abs(target - root.val) < Math.abs(target - ret)) {
                ret = root.val;
            }
            if (root.val > target) {
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return ret;
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
        assertThat(closestValue(root, 2.51), Matchers.is(3));
        assertThat(closestValue(root, 2.4), Matchers.is(2));
        assertThat(closestValue(root, 2.6), Matchers.is(3));

        assertThat(closestValue2(root, 2.51), Matchers.is(3));
        assertThat(closestValue2(root, 2.4), Matchers.is(2));
        assertThat(closestValue2(root, 2.6), Matchers.is(3));

        assertThat(closestValue3(root, 2.51), Matchers.is(3));
        assertThat(closestValue3(root, 2.4), Matchers.is(2));
        assertThat(closestValue3(root, 2.6), Matchers.is(3));

        root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(closestValue(root, 4.33), Matchers.is(4));
        assertThat(closestValue(root, 4.89), Matchers.is(5));
        assertThat(closestValue(root, 6.12), Matchers.is(6));

        assertThat(closestValue2(root, 4.33), Matchers.is(4));
        assertThat(closestValue2(root, 4.89), Matchers.is(5));
        assertThat(closestValue2(root, 6.12), Matchers.is(6));

        assertThat(closestValue3(root, 4.33), Matchers.is(4));
        assertThat(closestValue3(root, 4.89), Matchers.is(5));
        assertThat(closestValue3(root, 6.12), Matchers.is(6));

        root = TreeNodeHelper.init("4");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        assertThat(closestValue(root, 4.33), Matchers.is(4));
        assertThat(closestValue(root, 4.89), Matchers.is(4));
        assertThat(closestValue(root, 3454325435.0), Matchers.is(4));

        assertThat(closestValue2(root, 4.33), Matchers.is(4));
        assertThat(closestValue2(root, 4.89), Matchers.is(4));
        assertThat(closestValue2(root, 3454325435.0), Matchers.is(4));

        assertThat(closestValue3(root, 4.33), Matchers.is(4));
        assertThat(closestValue3(root, 4.89), Matchers.is(4));
        assertThat(closestValue3(root, 3454325435.0), Matchers.is(4));
    }

}
