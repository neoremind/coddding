package net.neoremind.mycode.argorithm.other;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @author xu.zhang
 */
public class ValidateCompleteTree {

    /**
     * Returns true if binary tree is complete
     *
     * @return true if tree is complete else false.
     */
    public boolean isComplete(TreeNode root) {
        if (root == null) {
            throw new NoSuchElementException();
        }
        final Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);

        boolean incompleteDetected = false;

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node.left != null) {
                if (incompleteDetected) return false;
                queue.add(node.left);
            } else {
                incompleteDetected = true;
            }

            if (node.right != null) {
                if (incompleteDetected) return false;
                queue.add(node.right);
            } else {
                incompleteDetected = true;
            }
        }
        return true;
    }

}
