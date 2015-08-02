package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Reverse a singly linked list.
 * <p/>
 * Hint:
 * A linked list can be reversed either iteratively or recursively. Could you implement both?
 * <p/>
 * http://blog.csdn.net/foreverling/article/details/45768031
 *
 * @author zhangxu
 */
public class ReverseLinkedList {

    /**
     * <pre>
     * Step1:   head/pre   cur
     *            7     ->  4  ->  9  ->  6  ->  3
     *
     * Step2:  head/pre    cur
     *            7         4  ->  9  ->  6  ->  3
     *            |                /\
     *            |_________________|
     *
     * Step3:  head/pre    cur
     *            7     <-  4  ->  9  ->  6  ->  3
     *            |                /\
     *            |_________________|
     *
     * Step4:    pre    head/cur
     *            7     <-  4  ->  9  ->  6  ->  3
     *            |                /\
     *            |_________________|
     *
     * Step5:    pre       head   cur
     *            7     <-  4  ->  9  ->  6  ->  3
     *            |                /\
     *            |_________________|
     * </pre>
     *
     * @param head
     *
     * @return
     */
    public static ListNode reverseListIteratively(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode cur = head.next;
        ListNode pre = head;
        while (cur != null) {
            pre.next = cur.next;
            cur.next = head;
            head = cur;
            cur = pre.next;
        }

        return head;
    }

    public static ListNode reverseListRecursiveLy(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseListRecursiveLy(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    public static void main(String[] args) {
        ListNode beforeHead = ListNodeHelper.build(new int[] {2, 3});
        ListNode afterHead = reverseListRecursiveLy(beforeHead);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("3->2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2});
        afterHead = reverseListRecursiveLy(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {});
        afterHead = reverseListRecursiveLy(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is(""));

        beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        afterHead = reverseListRecursiveLy(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("7->4->9->6->3->2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2, 3});
        afterHead = reverseListIteratively(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("3->2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2});
        afterHead = reverseListIteratively(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {});
        afterHead = reverseListIteratively(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is(""));

        beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        afterHead = reverseListIteratively(beforeHead);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("7->4->9->6->3->2->NULL"));
    }

}
