package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Sort a linked list in O(n log n) time using constant space complexity.
 *
 * @author xu.zhang
 */
public class SortList {

    public ListNode sortList(ListNode head) {
        if (head == null) return head;
        return mergeSort(head);
    }

    public ListNode mergeSort(ListNode list) {
        if (list.next == null) {
            return list;
        } else {
            ListNode head = list, walker = list, runner = list;
            while (runner.next != null && runner.next.next != null) {
                walker = walker.next;
                runner = runner.next.next;
            }
            // cut
            ListNode mid = walker.next;
            walker.next = null;
            ListNode l = mergeSort(head);
            ListNode r = mergeSort(mid);
            return merge(l, r);
        }
    }

    public ListNode merge(ListNode l, ListNode r) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (l != null && r != null) {
            if (l.val < r.val) {
                curr.next = l;
                l = l.next;
            } else {
                curr.next = r;
                r = r.next;
            }
            curr = curr.next;
        }

        if (l != null) curr.next = l;
        if (r != null) curr.next = r;
        return dummy.next;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{0, 1, 2});
        ListNode ret = sortList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(ret));
        assertThat(ListNodeHelper.getPrintableListNode(ret), is("0->1->2->NULL"));

        head = ListNodeHelper.build(new int[]{2, 1, 0});
        ret = sortList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(ret));
        assertThat(ListNodeHelper.getPrintableListNode(ret), is("0->1->2->NULL"));

        head = ListNodeHelper.build(new int[]{3, 2, 4});
        ret = sortList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(ret));
        assertThat(ListNodeHelper.getPrintableListNode(ret), is("2->3->4->NULL"));

        head = ListNodeHelper.build(new int[]{4, 19, 14, 5, -3, 1, 8, 5, 11, 15});
        ret = sortList(head);
        System.out.println(ListNodeHelper.getPrintableListNode(ret));
        assertThat(ListNodeHelper.getPrintableListNode(ret), is("-3->1->4->5->5->8->11->14->15->19->NULL"));
    }

}
