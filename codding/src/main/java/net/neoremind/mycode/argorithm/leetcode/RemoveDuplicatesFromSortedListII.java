package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the
 * original list.
 * <p>
 * For example,
 * Given 1->2->3->3->4->4->5, return 1->2->5.
 * Given 1->1->1->2->3, return 2->3.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/
 */
public class RemoveDuplicatesFromSortedListII {

    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        while (head != null) {
            while (head.next != null && head.next.val == head.val) {
                head = head.next;
            }
            if (pre.next == head) {
                pre = pre.next;
            } else {
                pre.next = head.next;
            }
            head = head.next;
        }
        return dummy.next;
    }

    @Test
    public void test() {
        ListNode beforeHead = ListNodeHelper.build(new int[] {1, 1, 2, 5, 5});
        ListNode afterHead = deleteDuplicates(beforeHead);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 1, 2, 2, 5, 5});
        afterHead = deleteDuplicates(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is(""));

        beforeHead = ListNodeHelper.build(new int[] {1, 1, 2});
        afterHead = deleteDuplicates(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->NULL"));
    }
}
