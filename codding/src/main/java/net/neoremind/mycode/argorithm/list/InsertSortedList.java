package net.neoremind.mycode.argorithm.list;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.junit.Test;

/**
 * @author xu.zhang
 */
public class InsertSortedList {

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5, 7, 8, 9});
        int value = 6;
        insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1});
        value = 6;
        insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{9});
        value = 6;
        insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5, 7, 8, 9});
        value = 10;
        insert(head, value);
        System.out.println(ListNodeHelper.getPrintableListNode(head));
    }

    private void insert(ListNode head, int value) {
        if (head.next == null) {

        }
        if (head.next.val > value) {
            doInsert(head, value);
        } else {
            insert(head.next, value);
        }
    }

    private void doInsert(ListNode head, int value) {
        ListNode tmp = head.next;
        head.next = new ListNode(value);
        head.next.next = tmp;
    }
}
