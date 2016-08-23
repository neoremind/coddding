package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Merge two sorted linked lists and return it as a new list. The new list should be made by splicing together the
 * nodes of the first two lists.
 *
 * @author zhangxu
 */
public class MergeTwoSortedLists {

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }

        if (l1 != null) {
            curr.next = l1;
        }
        if (l2 != null) {
            curr.next = l2;
        }
        return dummy.next;
    }

    @Test
    public void test() {
        ListNode l1 = ListNodeHelper.build(new int[] {1, 3, 5});
        ListNode l2 = ListNodeHelper.build(new int[] {2, 4});
        ListNode result = mergeTwoLists(l1, l2);
        System.out.println(ListNodeHelper.getPrintableListNode(result));
    }
}
