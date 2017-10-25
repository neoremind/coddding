package net.neoremind.mycode.argorithm.other;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertThat;

/**
 * http://www.geeksforgeeks.org/in-place-convert-a-given-binary-tree-to-doubly-linked-list/
 *
 * @author xu.zhang
 */
public class TreeToDoublyLinkedList {

    TreeNode bintree2listUtil(TreeNode node) {
        // Base case
        if (node == null)
            return node;

        // Convert the left subtree and link to root
        if (node.left != null) {
            // Convert the left subtree
            TreeNode left = bintree2listUtil(node.left);

            // Find inorder predecessor. After this loop, left
            // will point to the inorder predecessor
            for (; left.right != null; left = left.right) ;

            // Make root as next of the predecessor
            left.right = node;

            // Make predecssor as previous of root
            node.left = left;
        }

        // Convert the right subtree and link to root
        if (node.right != null) {
            // Convert the right subtree
            TreeNode right = bintree2listUtil(node.right);

            // Find inorder successor. After this loop, right
            // will point to the inorder successor
            for (; right.left != null; right = right.left) ;

            // Make root as previous of successor
            right.left = node;

            // Make successor as next of root
            node.right = right;
        }

        return node;
    }

    // The main function that first calls bintree2listUtil(), then follows
    // step 3 of the above algorithm

    TreeNode bintree2list(TreeNode node) {
        // Base case
        if (node == null)
            return node;

        // Convert to DLL using bintree2listUtil()
        node = bintree2listUtil(node);

        // bintree2listUtil() returns root node of the converted
        // DLL.  We need pointer to the leftmost node which is
        // head of the constructed DLL, so move to the leftmost node
        while (node.left != null)
            node = node.left;

        return node;
    }

    public TreeNode convert(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) return root;
        TreeNode dummy = new TreeNode(0);
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        TreeNode seeker = dummy;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                //
                seeker.right = node;
                node.left = seeker;
                seeker = seeker.right;

                curr = node.right;
            }
        }
        TreeNode res =  dummy.right;
        res.left = null;
        return res;
    }

    protected void print(TreeNode node) {
        while (node != null) {
            System.out.print(node + "->");
            node = node.right;
        }
        System.out.println();
    }

    protected void printReverse(TreeNode node) {
        while (node != null) {
            System.out.print(node + "->");
            node = node.left;
        }
        System.out.println();
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
        TreeNode s = TreeNodeHelper.init("4,2,6,1,3,5,7");
        print(convert(s));
        TreeNode rightMost = getRightMost(s);
        printReverse(rightMost);

        s = TreeNodeHelper.init("4,2,6,1,3,5,7");
        print(bintree2list(s));
        rightMost = getRightMost(s);
        printReverse(rightMost);
    }

    private TreeNode getRightMost(TreeNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

}
