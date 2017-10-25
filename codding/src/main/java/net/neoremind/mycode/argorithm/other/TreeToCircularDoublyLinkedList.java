package net.neoremind.mycode.argorithm.other;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.Stack;

/**
 * http://www.geeksforgeeks.org/convert-a-binary-tree-to-a-circular-doubly-link-list/
 *
 * @author xu.zhang
 */
public class TreeToCircularDoublyLinkedList {

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
        TreeNode head = dummy.right;
        TreeNode tail = getRightMost(root, null);
        tail.right = head;
        head.left = tail;
        return head;
    }

    protected void print(TreeNode node, TreeNode endFlag) {
        int index = 0;
        while (node != null) {
            if (index > 0 && node == endFlag) {
                break;
            }
            System.out.print(node + "->");
            node = node.right;
            index++;
        }
        System.out.println();
    }

    protected void printReverse(TreeNode node, TreeNode endFlag) {
        int index = 0;
        while (node != null) {
            if (index > 0 && node == endFlag) {
                break;
            }
            System.out.print(node + "->");
            node = node.left;
            index++;
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
        TreeNode head = convert(s);
        print(head, head);
        TreeNode rightMost = getRightMost(s, head);
        printReverse(rightMost, rightMost);
    }

    private TreeNode getRightMost(TreeNode node, TreeNode endFlag) {
        int index = 0;
        while (node.right != null) {
            if (index > 0 && node.right == endFlag) {
                break;
            }
            node = node.right;
            index++;
        }
        return node;
    }

}
