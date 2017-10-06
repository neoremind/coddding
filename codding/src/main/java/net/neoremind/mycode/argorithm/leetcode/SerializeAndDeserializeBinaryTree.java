package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Serialization is the process of converting a data structure or object into a sequence of bits so that it can be
 * stored in a file or memory buffer, or transmitted across a network connection link to be reconstructed later in
 * the same or another computer environment.
 * <p>
 * Design an algorithm to serialize and deserialize a binary tree. There is no restriction on how your
 * serialization/deserialization algorithm should work. You just need to ensure that a binary tree can be serialized
 * to a string and this string can be deserialized to the original tree structure.
 * <p>
 * For example, you may serialize the following tree
 * <p>
 * 1
 * / \
 * 2   3
 * / \
 * 4   5
 * as "[1,2,3,null,null,4,5]", just the same as how LeetCode OJ serializes a binary tree. You do not necessarily need
 * to follow this format, so please be creative and come up with different approaches yourself.
 * Note: Do not use class member/global/static variables to store states. Your serialize and deserialize algorithms
 * should be stateless.
 *
 * @author xu.zhang
 * @see https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/
 */
public class SerializeAndDeserializeBinaryTree {

    /**
     * Level Order Traversal
     * https://discuss.leetcode.com/topic/31264/short-and-straight-forward-bfs-java-code-with-a-queue
     */
    public String serialize(TreeNode root) {
        if (root == null) return "";
        Queue<TreeNode> q = new LinkedList<>();
        StringBuilder res = new StringBuilder();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null) {
                res.append("#,");
                continue;
            }
            res.append(node.val + ",");
            q.add(node.left);
            q.add(node.right);
        }
        return res.toString();
    }

    public TreeNode deserialize(String data) {
        if (data == "") return null;
        Queue<TreeNode> q = new LinkedList<>();
        String[] values = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        q.add(root);
        for (int i = 1; i < values.length; i++) {
            TreeNode parent = q.poll();
            if (!values[i].equals("#")) {
                TreeNode left = new TreeNode(Integer.parseInt(values[i]));
                parent.left = left;
                q.add(left);
            }
            if (!values[++i].equals("#")) {
                TreeNode right = new TreeNode(Integer.parseInt(values[i]));
                parent.right = right;
                q.add(right);
            }
        }
        return root;
    }

    /**
     * Preorder traversal
     * https://discuss.leetcode.com/topic/28029/easy-to-understand-java-solution
     */
    // 递归的preorder
    public String serialize2(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    //非递归的preorder
    public String serialize2_(TreeNode root) {
        if (root == null) return null;

        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        StringBuilder sb = new StringBuilder();

        while (!stack.isEmpty()) {
            TreeNode h = stack.pop();
            if (h != null) {
                sb.append(h.val + ",");
                stack.push(h.right);
                stack.push(h.left);
            } else {
                sb.append("#,");
            }
        }

        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#").append(",");
        } else {
            sb.append(node.val).append(",");
            buildString(node.left, sb);
            buildString(node.right, sb);
        }
    }

    public TreeNode deserialize2(String data) {
        Deque<String> nodes = new LinkedList<>();
        nodes.addAll(Arrays.asList(data.split(",")));
        return buildTree(nodes);
    }

    private TreeNode buildTree(Deque<String> nodes) {
        String val = nodes.poll();
        if (val.equals("#")) return null;
        else {
            TreeNode node = new TreeNode(Integer.valueOf(val));
            node.left = buildTree(nodes);
            node.right = buildTree(nodes);
            return node;
        }
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     6
     *  / \   / \
     * 1   3 5   7
     * </pre>
     */
    @Test
    public void test() {
        TreeNode root = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println("Original tree in-order: " + TreeNodeHelper.inorderTraversal(root));
        String ser = serialize(root);
        assertThat(ser, is("4,2,6,1,3,5,7,#,#,#,#,#,#,#,#,"));
        TreeNode deserNode = deserialize(ser);
        assertThat(TreeNodeHelper.preorderTraversal(deserNode), is("4,2,1,3,6,5,7,"));

        String ser2 = serialize2(root);
        String ser2_ = serialize2_(root);
        assertThat(ser2, is("4,2,1,#,#,3,#,#,6,5,#,#,7,#,#,"));
        assertThat(ser2_, is("4,2,1,#,#,3,#,#,6,5,#,#,7,#,#,"));
        TreeNode deserNode2 = deserialize2(ser2);
        assertThat(TreeNodeHelper.preorderTraversal(deserNode2), is("4,2,1,3,6,5,7,"));
    }

}
