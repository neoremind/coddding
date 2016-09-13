package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeLinkNode;

/**
 * Follow up for problem "Populating Next Right Pointers in Each Node".
 * <p>
 * What if the given tree could be any binary tree? Would your previous solution still work?
 * <p>
 * Note:
 * <p>
 * You may only use constant extra space.
 * For example,
 * Given the following binary tree,
 * <pre>
 *     1
 *    /  \
 *   2    3
 *  / \    \
 * 4  5     7
 * </pre>
 * After calling your function, the tree should look like:
 * <pre>
 *     1 -> NULL
 *    /  \
 *   2 -> 3 -> NULL
 *  / \   \
 * 4->5   ->7 -> NULL
 *  </pre>
 *
 * 利用上一层已经是linkedlist的特性，不用level order traversal
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/populating-next-right-pointers-in-each-node-ii/
 * @see PopulatingNextRightPointersInEachNode
 */
public class PopulatingNextRightPointersInEachNodeII {

    public void connect(TreeLinkNode root) {
        TreeLinkNode head = root;
        TreeLinkNode prev = null;
        while (head != null) {
            TreeLinkNode curr = head;
            TreeLinkNode nextLevelHead = null;
            while (curr != null) {
                if (curr.left != null) {
                    if (prev == null) {
                        prev = curr.left;
                        nextLevelHead = prev;
                    } else {
                        prev.next = curr.left;
                        prev = prev.next;
                    }
                }
                if (curr.right != null) {
                    if (prev == null) {
                        prev = curr.right;
                        nextLevelHead = prev;
                    } else {
                        prev.next = curr.right;
                        prev = prev.next;
                    }
                }
                curr = curr.next;
            }
            prev = null;
            head = nextLevelHead;
        }
    }

}
