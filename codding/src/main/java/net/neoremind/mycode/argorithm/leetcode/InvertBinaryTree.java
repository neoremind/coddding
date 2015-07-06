package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ClassName: InvertBinaryTree <br/>
 * Function: Invert a binary tree.
 * 
 * <pre>
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 * to
 *      4
 *    /   \
 *   7     2
 *  / \   / \
 * 9   6 3   1
 * </pre>
 * 
 * Trivia: <br/>
 * This problem was inspired by this original tweet by Max Howell:<br/>
 * Google: 90% of our engineers use the software you wrote (Homebrew), but you can’t invert a binary tree on a
 * whiteboard so fuck off.
 * 
 * @author Zhang Xu
 */
public class InvertBinaryTree {

    /**
     * 反转二叉树实现
     * 
     * @param root
     * @return inverted tree root node
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode temp = root.right;
        root.right = root.left;
        root.left = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
        InvertBinaryTree test = new InvertBinaryTree();

        // init tree root node
        TreeNode t = new TreeNode(4);

        // init the whole tree
        Queue<TreeNode> s = new LinkedList<TreeNode>();
        s.offer(t);
        int[] arr = new int[] { 4, 2, 7, 1, 3, 6, 9 };
        init(s, arr, 1);

        // invert
        test.invertTree(t);

        // inorder print
        inOrder(t);

        // assert result
        assertThat(INORDER_TRAVERSE_STR, is("9,7,6,4,3,2,1,"));
    }

    /**
     * 用数组<code>int[]</code>初始化一颗树，按照树深层级，从低到高的深度，从左到右，依次填满整棵树，直到数组越界位置退出
     * <p>
     * 利用FIFO队列思路，将一棵树的某一层依次推入到队列中，利用递归，对于上一次递归完成结果的队列，poll出元素，此时也就是上一层最左边的节点，<br/>
     * 找到数组合适的索引，赋值新的左右节点，然后处理下一个节点，直到把上一层所有节点都处理完，或者数组越界而退出。
     * 
     * @param s
     *            队列，第一次调用时候需要保证已经插入的树的根节点
     * @param arr
     *            数组
     * @param index
     *            目前数组处理到的索引
     */
    private static void init(Queue<TreeNode> s, int[] arr, int index) {
        if (arr == null || arr.length == 0 || index >= arr.length) {
            return;
        }
        TreeNode t = null;
        TreeNode left = null;
        TreeNode right = null;
        while (!s.isEmpty() && (t = s.poll()) != null) {
            if (index >= arr.length) {
                break;
            }
            left = new TreeNode(arr[index++]);
            t.left = left;

            if (index >= arr.length) {
                break;
            }
            right = new TreeNode(arr[index++]);
            t.right = right;
        }

        if (left != null) {
            s.offer(left);
        }
        if (right != null) {
            s.offer(right);
        }
        init(s, arr, index);
    }

    private static String INORDER_TRAVERSE_STR = "";

    /**
     * 中序遍历
     * 
     * @param root
     */
    public static void inOrder(TreeNode root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.val + ",");
            INORDER_TRAVERSE_STR += root.val + ",";
            inOrder(root.right);
        }
    }

}
