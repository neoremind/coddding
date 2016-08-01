package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Add Two Numbers  QuestionEditorial Solution  My Submissions
 * Total Accepted: 163026
 * Total Submissions: 671408
 * Difficulty: Medium
 * You are given two linked lists representing two non-negative numbers. The digits are stored in reverse order and
 * each of their nodes contain a single digit. Add the two numbers and return it as a linked list.
 * <p>
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/add-two-numbers/
 */
public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode r = new ListNode(0);
        ListNode head = r;
        ListNode beforeEnd = r;
        while (l1 != null && l2 != null) {
            int value = r.val;
            value += l1.val + l2.val;
            r.next = new ListNode(value / 10);
            r.val = value % 10;
            beforeEnd = r;
            r = r.next;
            l1 = l1.next;
            l2 = l2.next;
        }

        ListNode left;
        if (l1 == null) {
            left = l2;
        } else {
            left = l1;
        }

        while (left != null) {
            int value = r.val;
            value += left.val;
            r.next = new ListNode(value / 10);
            r.val = value % 10;
            beforeEnd = r;
            r = r.next;
            left = left.next;
        }

        if (beforeEnd.next != null && beforeEnd.next.val == 0) {
            beforeEnd.next = null;
        }

        return head;
    }

    @Test
    public void test() {
        ListNode l1 = ListNodeHelper.build(new int[] {2, 4, 3});
        System.out.println(ListNodeHelper.getPrintableListNode(l1));
        ListNode l2 = ListNodeHelper.build(new int[] {5, 6, 4});
        System.out.println(ListNodeHelper.getPrintableListNode(l2));
        ListNode res = addTwoNumbers(l1, l2);
        System.out.println(ListNodeHelper.getPrintableListNode(res));
    }

}
