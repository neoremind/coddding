package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import net.neoremind.mycode.argorithm.leetcode.treetraversal.BinaryTreePreorder;

/**
 * Given preorder and inorder traversal of a tree, construct the binary tree.
 * <p>
 * Note:
 * You may assume that duplicates do not exist in the tree.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 */
public class ConstructBinaryTreePreorderAndInorderTraversal {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0 ||
                inorder == null || inorder.length == 0) {
            return null;
        }
        if (preorder.length != preorder.length) {
            throw new RuntimeException("Tree not same!");
        }
        return doBuildTree(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    TreeNode doBuildTree(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        // 可加可不加
//        if (preStart == preEnd) {
//            return new TreeNode(preorder[preStart]);
//        }
//        if (inStart == inEnd) {
//            return new TreeNode(inorder[inStart]);
//        }
        int nodeIndex = -1;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == preorder[preStart]) {
                nodeIndex = i;
                break;
            }
        }
        int leftChildrenLen = nodeIndex - inStart;
        if (nodeIndex < 0) {
            throw new RuntimeException("Tree not same!");
        }
        TreeNode node = new TreeNode(preorder[preStart]);
        node.left = doBuildTree(preorder, preStart + 1, preStart + leftChildrenLen, inorder, inStart,
                nodeIndex - 1);
        node.right = doBuildTree(preorder, preStart + leftChildrenLen + 1, preEnd, inorder,
                nodeIndex + 1, inEnd);
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
        int[] preorder = preorderTraverse(tree1);
        int[] inorder = inorderTraverse(tree1);
        System.out.println("Preorder: " + ArrayUtils.toString(preorder));
        System.out.println("Inorder: " + ArrayUtils.toString(inorder));
        TreeNode treeNode1 = buildTree(preorder, inorder);
        System.out.println(TreeNodeHelper.preorderTraversal(treeNode1));
        System.out.println(TreeNodeHelper.inorderTraversal(treeNode1));
    }

    int[] preorderTraverse(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        doPreorderTraverse(root, list);
        return list.stream().mapToInt(e -> e).toArray();
    }

    void doPreorderTraverse(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        list.add(node.val);
        doPreorderTraverse(node.left, list);
        doPreorderTraverse(node.right, list);
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

}
