package net.neoremind.mycode.argorithm.leetcode.support;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

import net.neoremind.mycode.util.ThreadContext;

/**
 * 二叉树工具帮助类
 */
public class TreeNodeHelper {

    public static void main(String[] args) {
        int[] arr = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode root = init(arr);
        String result = preorderTraversal(root);
        System.out.println("preorder:" + result);
        assertThat(result, is("4,2,1,3,7,6,9,"));
        result = inorderTraversal(root);
        System.out.println("inorder:" + result);
        assertThat(result, is("1,2,3,4,6,7,9,"));
        result = postorderTraversal(root);
        System.out.println("postorder:" + result);
        assertThat(result, is("1,3,2,6,9,7,4,"));

        arr = new int[] {4, 2, 7, 1, 3, 6, 9, 10, 11, 12};
        root = init(arr);
        result = inorderTraversal(root);
        System.out.println(result);
        assertThat(result, is("10,1,11,2,12,3,4,6,7,9,"));
    }

    /**
     * 前序遍历一棵树
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     *
     * @see #doPreorderTraversal(TreeNode)
     */
    public static String preorderTraversal(TreeNode root) {
        try {
            ThreadContext.init();
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, "");
            return doPreorderTraversal(root);
        } finally {
            ThreadContext.clean();
        }
    }

    /**
     * 中序遍历一棵树
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     *
     * @see #doInorderTraversal(TreeNode)
     */
    public static String inorderTraversal(TreeNode root) {
        try {
            ThreadContext.init();
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, "");
            return doInorderTraversal(root);
        } finally {
            ThreadContext.clean();
        }
    }

    /**
     * 后序遍历一棵树
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     *
     * @see #doPostorderTraversal(TreeNode)
     */
    public static String postorderTraversal(TreeNode root) {
        try {
            ThreadContext.init();
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, "");
            return doPostorderTraversal(root);
        } finally {
            ThreadContext.clean();
        }
    }

    /**
     * 前序遍历一棵树
     * <p/>
     * 例如，一棵树为
     * * <pre>
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     * </pre>
     * <p/>
     * 打印结果为
     * <pre>
     *     4,2,1,3,7,6,9
     * </pre>
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     */
    private static String doPreorderTraversal(TreeNode root) {
        if (root != null) {
            String str = ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, str + root.val + ",");
            doPreorderTraversal(root.left);
            doPreorderTraversal(root.right);
            return ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 中序遍历一棵树
     * <p/>
     * 例如，一棵树为
     * * <pre>
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     * </pre>
     * <p/>
     * 打印结果为
     * <pre>
     *     1,2,3,4,6,7,9,
     * </pre>
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     */
    private static String doInorderTraversal(TreeNode root) {
        if (root != null) {
            doInorderTraversal(root.left);
            String str = ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, str + root.val + ",");
            doInorderTraversal(root.right);
            return ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 后序遍历一棵树
     * <p/>
     * 例如，一棵树为
     * * <pre>
     *      4
     *    /   \
     *   2     7
     *  / \   / \
     * 1   3 6   9
     * </pre>
     * <p/>
     * 打印结果为
     * <pre>
     *     1,3,2,6,7,9,4
     * </pre>
     *
     * @param root 树根节点
     *
     * @return 一棵树的字符串
     */
    private static String doPostorderTraversal(TreeNode root) {
        if (root != null) {
            doPostorderTraversal(root.left);
            doPostorderTraversal(root.right);
            String str = ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
            ThreadContext.putContext(THREAD_LOCAL_STRING_KEY, str + root.val + ",");
            return ThreadContext.getContext(THREAD_LOCAL_STRING_KEY);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 初始化一棵树
     *
     * @param arr 树节点的值，按照层次排序
     *
     * @return 树根节点
     *
     * @see #build(Queue, int[], int)
     */
    public static TreeNode init(int[] arr) {
        Preconditions.checkArgument(!ArrayUtils.isEmpty(arr), "arr should not be empty");

        // init tree root node
        TreeNode root = new TreeNode(arr[0]);

        // init the whole tree
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);

        build(queue, arr, 1);

        return root;
    }

    /**
     * 用数组<code>int[]</code>初始化一颗树，按照树深层级，从低到高的深度，从左到右，依次填满整棵树，直到数组越界位置退出
     * <p/>
     * 利用FIFO队列思路，将一棵树的某一层依次推入到队列中，利用递归，对于上一次递归完成结果的队列，poll出元素，此时也就是上一层最左边的节点，<br/>
     * 找到数组合适的索引，赋值新的左右节点，然后处理下一个节点，直到把上一层所有节点都处理完，或者数组越界而退出。
     *
     * @param queue 队列，第一次调用时候需要保证已经插入的树的根节点
     * @param arr   数组
     * @param index 目前数组处理到的索引
     */
    private static void build(Queue<TreeNode> queue, int[] arr, int index) {
        if (arr == null || arr.length == 0 || index >= arr.length) {
            return;
        }
        TreeNode t;
        List<TreeNode> ready2QueuedNodes = new ArrayList<TreeNode>();
        while (!queue.isEmpty() && (t = queue.poll()) != null) {
            if (index >= arr.length) {
                break;
            }
            TreeNode left = new TreeNode(arr[index++]);
            t.left = left;
            ready2QueuedNodes.add(left);

            if (index >= arr.length) {
                break;
            }
            TreeNode right = new TreeNode(arr[index++]);
            t.right = right;
            ready2QueuedNodes.add(right);
        }

        for (TreeNode node : ready2QueuedNodes) {
            queue.offer(node);
        }

        build(queue, arr, index);
    }

    /**
     * 为打印而使用，本地线程缓存树的字面字符串，用于递归内获取使用
     *
     * @see #inorderTraversal(TreeNode)
     */
    private static final String THREAD_LOCAL_STRING_KEY = "_tree_node";

}
