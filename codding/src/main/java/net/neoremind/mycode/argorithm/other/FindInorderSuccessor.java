package net.neoremind.mycode.argorithm.other;

import java.util.Stack;

/**
 * binary tree
 * left right
 * parent pointer
 * <p>
 * node
 * in order successor
 * <p>
 * 1
 * / \
 * 2   3
 * / \   /
 * 4   5  6
 * / \
 * 9  7
 * \
 * 8
 * inorder: 425163
 * given 5 return 1
 * given 6 return 3
 * given 4 return 2
 * given 3 return null
 * <p>
 * <p>
 * o parent
 * |
 * o node
 * / \
 * left  o    o right
 * <p>
 * // handle node null
 * if node == null
 * return null
 * if node.right == null
 * temp = node;
 * while temp.parent != null && temp.parent.right == temp
 * temp = temp.parent;
 * return temp.parent;
 * else
 * temp = node.right;
 * while temp.left != null
 * temp = temp.left;
 * return temp;
 * <p>
 * node root.
 */
public class FindInorderSuccessor {


    /**
     * node number = N
     * Time complexity: O(N)
     * Space complexity: O(lgN) hight of the tree
     * <p>
     * worst linked list
     * 1
     * /
     * 2
     * /
     * 3
     */
    public TreeNode findSuccessor(TreeNode anyNode) {
        // assert anyNode != null;

        // find root
        TreeNode root = anyNode;
        while (root.parent != null) {
            root = root.parent;
        }

        // inorder traversal
        // iterative way
        boolean foundAnyNode = false;
        TreeNode curr = root;
        Stack<TreeNode> stack = new Stack<>();
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode node = stack.pop();
                if (node == anyNode) {
                    foundAnyNode = true;
                }
                if (node != anyNode && foundAnyNode) {
                    return node;
                }
                curr = node.right;
            }
        }
        return null;
    }

    public TreeNode optimizedVersion(TreeNode anyNode) {
        if (anyNode.right != null) {
            return anyNode.right;
        } else {
            return anyNode.left;
        }
        // how to handle end node like 3?
    }

    class TreeNode {
        TreeNode left;
        TreeNode right;
        TreeNode parent;
    }

}
