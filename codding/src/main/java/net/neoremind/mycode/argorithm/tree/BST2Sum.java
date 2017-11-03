package net.neoremind.mycode.argorithm.tree;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * https://instant.1point3acres.com/thread/280997
 * 三种方法：
 * 方法1：类似array的two sum，因为不用返回index，所以使用一个hashset。
 * time: O(N)
 * space: O(N)
 * <p>
 * ```
 * if (root == null) {
 * return null;
 * }
 * Set<Integer> hashset = new HashSet<>();
 * Stack<TreeNode> stack = new Stack<>();
 * TreeNode curr = root;
 * while (curr != null || !stack.isEmpty()) {
 * if (curr != null) {
 * stack.push(curr);
 * curr = curr.left;
 * } else {
 * TreeNode node = stack.pop();
 * // 替换逻辑开始
 * if (hashset.contains(target - node.val)) {
 * return new int[]{target - node.val, node.val};
 * }
 * hashset.add(node.val);
 * // 替换逻辑结束
 * curr = node.right;
 * }
 * }
 * return null;
 * ```
 * <p>
 * 方法2：BST二分查找，进一步缩小空间复杂度
 * time: O(NlogN)
 * space: O(h)
 * ```
 * if (search(root, target - node.val, node) != null) {
 * return new int[]{target - node.val, node.val};
 * }
 * <p>
 * if (root == null) {
 * return null;
 * if (root.val == target) {
 * if (root == node) {
 * return null;
 * return target;
 * } else if (root.val < target) {
 * return search(root.right, target, node);
 * } else {
 * return search(root.left, target, node);
 * ```
 * <p>
 * <p>
 * 方法3：two pointer压缩空间复杂度到O(1)，同时时间也是最优的。
 * BSTLeftIterator和BSTRightIterator就参考BinarySearchTreeIterator即可。
 * ```
 * if (root == null) {
 * return null;
 * }
 * BSTLeftIterator leftIterator = new BSTLeftIterator(root);
 * BSTRightIterator rightIterator = new BSTRightIterator(root);
 * int left = leftIterator.next();
 * int right = rightIterator.next();
 * while (left < right) {
 * int sum = left + right;
 * if (sum == target) {
 * return new int[]{left, right};
 * }
 * if (sum < target && leftIterator.hasNext()) {
 * left = leftIterator.next();
 * } else if (sum < target && !leftIterator.hasNext()) {
 * break;
 * }
 * if (sum > target && rightIterator.hasNext()) {
 * right = rightIterator.next();
 * } else if (sum > target && !rightIterator.hasNext()) {
 * break;
 * }
 * }
 * return null;
 * ```
 *
 * @author xu.zhang
 */
public class BST2Sum {

    /**
     * time: O(N)
     * space: O(N)
     */
    public int[] find(TreeNode root, int target) {
        if (root == null) {
            return null;
        }
        Set<Integer> hashset = new HashSet<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                if (hashset.contains(target - node.val)) {
                    return new int[]{target - node.val, node.val};
                }
                hashset.add(node.val);
                curr = node.right;
            }
        }
        return null;
    }

    /**
     * time: O(NlogN)
     * space: O(h)
     */
    public int[] find2(TreeNode root, int target) {
        if (root == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                if (search(root, target - node.val, node) != null) {
                    return new int[]{target - node.val, node.val};
                }
                curr = node.right;
            }
        }
        return null;
    }

    Integer search(TreeNode root, int target, TreeNode node) {
        if (root == null) {
            return null;
        }
        if (root.val == target) {
            if (root == node) {
                return null;
            }
            return target;
        } else if (root.val < target) {
            return search(root.right, target, node);
        } else {
            return search(root.left, target, node);
        }
    }

    /**
     * time: O(N)
     * space: O(h)
     */
    public int[] find3(TreeNode root, int target) {
        if (root == null) {
            return null;
        }
        BSTLeftIterator leftIterator = new BSTLeftIterator(root);
        BSTRightIterator rightIterator = new BSTRightIterator(root);
        int left = leftIterator.next();
        int right = rightIterator.next();
        while (left < right) {
            int sum = left + right;
            if (sum == target) {
                return new int[]{left, right};
            }
            if (sum < target && leftIterator.hasNext()) {
                left = leftIterator.next();
            } else if (sum < target && !leftIterator.hasNext()) {
                break;
            }
            if (sum > target && rightIterator.hasNext()) {
                right = rightIterator.next();
            } else if (sum > target && !rightIterator.hasNext()) {
                break;
            }
        }
        return null;
    }

    class BSTLeftIterator {

        private Stack<TreeNode> stack;

        private TreeNode curr;

        public BSTLeftIterator(TreeNode root) {
            stack = new Stack<>();
            curr = root;
        }

        /**
         * @return whether we have a next smallest number
         */
        public boolean hasNext() {
            return curr != null || !stack.isEmpty();
        }

        /**
         * @return the next smallest number
         */
        public int next() {
            while (curr != null || !stack.isEmpty()) {
                if (curr != null) {
                    stack.push(curr);
                    curr = curr.left;
                } else {
                    TreeNode node = stack.pop();
                    //... visit
                    curr = node.right;
                    return node.val;
                }
            }
            throw new RuntimeException("no next left");
        }
    }

    class BSTRightIterator {

        private Stack<TreeNode> stack;

        private TreeNode curr;

        public BSTRightIterator(TreeNode root) {
            stack = new Stack<>();
            curr = root;
        }

        /**
         * @return whether we have a next smallest number
         */
        public boolean hasNext() {
            return curr != null || !stack.isEmpty();
        }

        /**
         * @return the next largest number
         */
        public int next() {
            while (curr != null || !stack.isEmpty()) {
                if (curr != null) {
                    stack.push(curr);
                    curr = curr.right;
                } else {
                    TreeNode node = stack.pop();
                    //... visit
                    curr = node.left;
                    return node.val;
                }
            }
            throw new RuntimeException("no next left");
        }
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,7,1,3,6,9");
        System.out.println("Original tree: " + TreeNodeHelper.preorderTraversal(root));
        assertThat(find(root, 9), is(new int[]{3, 6}));
        assertThat(find(root, 2), nullValue());
        assertThat(find(root, 10), is(new int[]{4, 6}));

        assertThat(find2(root, 9), is(new int[]{7, 2}));
        assertThat(find2(root, 2), nullValue());
        assertThat(find2(root, 10), is(new int[]{9, 1}));

        assertThat(find3(root, 9), is(new int[]{2, 7}));
        assertThat(find3(root, 2), nullValue());
        assertThat(find3(root, 10), is(new int[]{1, 9}));
    }

}
