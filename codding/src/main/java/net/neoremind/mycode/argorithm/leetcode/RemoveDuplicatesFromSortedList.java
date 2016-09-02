package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a sorted linked list, delete all duplicates such that each element appear only once.
 * <p>
 * For example,
 * Given 1->1->2, return 1->2.
 * Given 1->1->2->3->3, return 1->2->3.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/remove-duplicates-from-sorted-list/
 */
public class RemoveDuplicatesFromSortedList {

    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        while (head != null) {
            ListNode begin = head;
            while (head.next != null && head.next.val == head.val) {
                head = head.next;
            }
            begin.next = head.next;
            head = head.next;
        }
        return dummy.next;
    }

    /**
     * 从后往前倒，保留最后一个重复的节点
     */
    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        head.next = deleteDuplicates(head.next);
        return head.val == head.next.val ? head.next : head;
    }

    @Test
    public void test() {
        ListNode beforeHead = ListNodeHelper.build(new int[] {1, 1, 2, 5, 5});
        ListNode afterHead = deleteDuplicates(beforeHead);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->2->5->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 1, 2});
        afterHead = deleteDuplicates(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->2->NULL"));
    }
}
