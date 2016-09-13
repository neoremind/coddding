package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a singly linked list where elements are sorted in ascending order, convert it to a height balanced BST.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/
 */
public class ConvertSortedListToBinarySearchTree {

    public TreeNode sortedListToBST(ListNode head) {
        return helper(head, null);
    }

    TreeNode helper(ListNode head, ListNode tail) {
        if (head == tail) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != tail && fast.next.next != tail) {
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode node = new TreeNode(slow.val);
        node.left = helper(head, slow);
        node.right = helper(slow.next, tail);
        return node;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[] {1, 2, 3});
        TreeNode node = sortedListToBST(head);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(node));

        head = ListNodeHelper.build(new int[] {1, 2, 3, 4});
        node = sortedListToBST(head);
        System.out.println("tree in-order: " + TreeNodeHelper.inorderTraversal(node));
    }

}
