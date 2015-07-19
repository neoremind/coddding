package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

import java.util.LinkedList;
import java.util.Queue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ClassName: InvertBinaryTree <br/>
 * Function: Invert a binary tree.
 * <p/>
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
 * <p/>
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
     * @param root 根节点
     * @return inverted tree root node
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode temp = root.right;
        root.right = root.left;
        root.left = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }

    public static void main(String[] args) {
        InvertBinaryTree test = new InvertBinaryTree();

        int[] arr = new int[]{4, 2, 7, 1, 3, 6, 9};
        TreeNode root = TreeNodeHelper.init(arr);

        // invert
        test.invertTree(root);

        String output = TreeNodeHelper.inOrderTraversal(root);
        System.out.println(output);

        // assert result
        assertThat(output, is("9,7,6,4,3,2,1,"));
    }

}
