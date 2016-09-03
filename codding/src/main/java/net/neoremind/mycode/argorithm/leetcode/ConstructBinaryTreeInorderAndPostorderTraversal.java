package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given inorder and postorder traversal of a tree, construct the binary tree.
 * <p>
 * Note:
 * You may assume that duplicates do not exist in the tree.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
 */
public class ConstructBinaryTreeInorderAndPostorderTraversal {

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder == null || postorder.length == 0 ||
                inorder == null || inorder.length == 0) {
            return null;
        }
        if (inorder.length != postorder.length) {
            throw new RuntimeException("Tree not same!");
        }
        return doBuildTree(inorder, 0, inorder.length - 1, postorder, 0, postorder.length - 1);
    }

    TreeNode doBuildTree(int[] inorder, int inStart, int inEnd, int[] postorder, int postStart, int postEnd) {
        if (inStart > inEnd || postStart > postEnd) {
            return null;
        }
        int nodeIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == postorder[postEnd]) {
                nodeIndex = i;
                break;
            }
        }
        int leftChildrenLen = nodeIndex - inStart;
        if (nodeIndex < 0) {
            throw new RuntimeException("Tree not same!");
        }
        TreeNode node = new TreeNode(postorder[postEnd]);
        node.left = doBuildTree(inorder, inStart, nodeIndex - 1, postorder, postStart,
                postStart + leftChildrenLen - 1);
        node.right = doBuildTree(inorder, nodeIndex + 1, inEnd, postorder,
                postStart + leftChildrenLen, postEnd - 1);
        return node;
    }

    /**
     * <pre>
     *        4
     *       / \
     *      2   7
     *     / \  /\
     *    1  3 6 9
     * </pre>
     */
    @Test
    public void test() {
        int[] arr1 = new int[] {4, 2, 7, 1, 3, 6, 9};
        TreeNode tree1 = TreeNodeHelper.init(arr1);
        int[] inorder = inorderTraverse(tree1);
        int[] postorder = postorderTraverse(tree1);
        System.out.println("Inorder: " + ArrayUtils.toString(inorder));
        System.out.println("Postorder: " + ArrayUtils.toString(postorder));
        TreeNode treeNode1 = buildTree(inorder, postorder);
        System.out.println(TreeNodeHelper.preorderTraversal(treeNode1));
        System.out.println(TreeNodeHelper.inorderTraversal(treeNode1));
    }

    int[] inorderTraverse(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        doInorderTraverse(root, list);
        return list.stream().mapToInt(e -> e).toArray();
    }

    void doInorderTraverse(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        doInorderTraverse(node.left, list);
        list.add(node.val);
        doInorderTraverse(node.right, list);
    }

    int[] postorderTraverse(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        doPostorderTraverse(root, list);
        return list.stream().mapToInt(e -> e).toArray();
    }

    void doPostorderTraverse(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        doPostorderTraverse(node.left, list);
        doPostorderTraverse(node.right, list);
        list.add(node.val);
    }

}
