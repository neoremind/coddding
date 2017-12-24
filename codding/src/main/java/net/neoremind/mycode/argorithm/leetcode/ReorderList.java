package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Given a singly linked list L: L0→L1→…→Ln-1→Ln,
 * reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…
 * <p>
 * You must do this in-place without altering the nodes' values.
 * <p>
 * For example,
 * Given {1,2,3,4}, reorder it to {1,4,2,3}.
 * <p>
 * <pre>
 *     1 -> 2 -> 3 -> 4
 *                 *
 *     1>4>2  -> 3
 * </pre>
 * <p>
 * <pre>
 *     1 -> 2 -> 3 -> 4 -> 5
 *                 *
 *     1>5>2 >4->3
 * </pre>
 * <p>
 * <pre>
 *     1 -> 2 -> 3 -> 4 -> 5 -> 6
 *                      *
 *     1>6>2 >5->3 -> 4
 * </pre>
 *
 * @author xu.zhang
 */
public class ReorderList {

    public void reorderList(ListNode head) {
        if (head == null) return;
        // 可以利用slow和fast找到中点，而不用count
//        int count = countList(head);
//        int cut = count / 2 + 1;
//        int i = 1;
//        ListNode cursor = head;
//        while (i++ < cut) {
//            cursor = cursor.next;
//        }

        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode cursor = slow;
        ListNode tail = reverse(cursor);
        ListNode curr = head;
        if (cursor.next == null) {
            return;
        }
        cursor.next.next = null;
        cursor.next = null;
        while (tail != null) {
            ListNode currNext = curr.next;
            ListNode tailNext = tail.next;
            curr.next = tail;
            tail.next = currNext;
            curr = currNext;
            tail = tailNext;
        }
    }

    int countList(ListNode node) {
        int res = 0;
        while (node != null) {
            node = node.next;
            res++;
        }
        return res;
    }

    ListNode reverse(ListNode node) {
        ListNode newHead = node;
        ListNode head = node.next;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1, 2, 3, 4});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(ListNodeHelper.getPrintableListNode(head), is("1->4->2->3->NULL"));

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(ListNodeHelper.getPrintableListNode(head), is("1->5->2->4->3->NULL"));

        head = ListNodeHelper.build(new int[]{1, 2, 3});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(ListNodeHelper.getPrintableListNode(head), is("1->3->2->NULL"));

        head = ListNodeHelper.build(new int[]{1, 2});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(ListNodeHelper.getPrintableListNode(head), is("1->2->NULL"));

        head = ListNodeHelper.build(new int[]{1});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(ListNodeHelper.getPrintableListNode(head), is("1->NULL"));

        head = ListNodeHelper.build(new int[]{});
        reorderList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
    }

}
