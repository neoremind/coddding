package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a linked list, remove the nth node from the end of list and return its head.
 * <p>
 * For example,
 * <p>
 * Given linked list: 1->2->3->4->5, and n = 2.
 * <p>
 * After removing the second node from the end, the linked list becomes 1->2->3->5.
 * Note:
 * Given n will always be valid.
 * Try to do this in one pass.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/remove-nth-node-from-end-of-list/
 */
public class RemoveNthNodeFromEndOfList {

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode header = new ListNode(0);
        header.next = head;
        ListNode start = header;
        ListNode end = header;
        for (int i = 1; i <= n + 1; i++) {
            end = end.next;
        }
        while (end != null) {
            start = start.next;
            end = end.next;
        }
        start.next = start.next.next;
        return header.next;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[] {1});
        assertThat(removeNthFromEnd(head, 1), Matchers.nullValue());
        head = ListNodeHelper.build(new int[] {1, 2});
        assertThat(removeNthFromEnd(head, 1), Matchers.is(ListNodeHelper.build(new int[] {1})));
        head = ListNodeHelper.build(new int[] {1, 2, 3});
        assertThat(removeNthFromEnd(head, 1), Matchers.is(ListNodeHelper.build(new int[] {1, 2})));
        head = ListNodeHelper.build(new int[] {1, 2, 3});
        assertThat(removeNthFromEnd(head, 3), Matchers.is(ListNodeHelper.build(new int[] {2, 3})));
        head = ListNodeHelper.build(new int[] {1, 2, 3});
        assertThat(removeNthFromEnd(head, 2), Matchers.is(ListNodeHelper.build(new int[] {1, 3})));
    }

}
