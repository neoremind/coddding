package net.neoremind.mycode.argorithm.list;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.junit.Test;

import java.util.List;

/**
 * @author xu.zhang
 */
public class InsertSortedList {

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5, 7, 8, 9});
        int value = 6;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1});
        value = 2;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1});
        value = 0;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{});
        value = 0;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1, 2});
        value = 3;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1, 2});
        value = 0;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1, 2, 3});
        value = 4;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1, 2, 3});
        value = 0;
        head = insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
    }

    private ListNode insert(ListNode head, int value) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        helper(dummy, value);
        return dummy.next;
    }

    private void helper(ListNode head, int value) {
        if (head.next == null) {
            head.next = new ListNode(value);
            return;
        }
        if (head.next.val > value) {
            doInsert(head, value);
        } else {
            helper(head.next, value);
        }
    }

    private void doInsert(ListNode head, int value) {
        ListNode tmp = head.next;
        head.next = new ListNode(value);
        head.next.next = tmp;
    }
}
