package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;

/**
 * Sort a linked list using insertion sort.
 * <p>
 * https://leetcode.com/problems/insertion-sort-list/
 *
 * @author zhangxu
 */
public class InsertionSortList {

    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = head;
        ListNode curr = head.next;
        while (curr != null) {
            ListNode nextAfterCurr = curr.next;
            ListNode ins = dummy;
            while (ins.next.val < curr.val) {
                ins = ins.next;
            }
            if (ins.next == curr) {
                pre = curr;
            } else {
                pre.next = curr.next;
                curr.next = ins.next;
                ins.next = curr;
            }
            curr = nextAfterCurr;
        }
        return dummy.next;
    }

}
