package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Reverse a singly linked list.
 * <p>
 * Hint:
 * A linked list can be reversed either iteratively or recursively. Could you implement both?
 * <p>
 * http://blog.csdn.net/foreverling/article/details/45768031
 *
 * @author zhangxu
 */
public class ReverseLinkedList {

    /**
     * while(head != null)
     * <pre>
     * Step1: newHead   head
     *          null      7    ->  4  ->  9  ->  6  ->  3
     *
     * Step2: newHead   head      next
     *          null      7    ->  4  ->  9  ->  6  ->  3
     *
     * Step3: newHead   head      next
     *          null  <-  7    ->  4  ->  9  ->  6  ->  3
     *
     * Step4:           newHead   head
     *          null  <-  7    ->  4  ->  9  ->  6  ->  3
     * </pre>
     * <p>
     * <pre>
     * Step1:          newHead   head
     *          0   <-   7   ->  4  ->  9  ->  6  ->  3
     *
     * Step2:          newHead   head  next
     *          0   <-   7   ->  4  ->  9  ->  6  ->  3
     *
     * Step3:          newHead   head  next
     *          0   <-   7   <-  4  ->  9  ->  6  ->  3
     *
     * Step3       :          newHead  head
     *          0   <-   7   <-  4  ->  9  ->  6  ->  3
     * </pre>
     *
     * @param head
     *
     * @return
     */
    public static ListNode reverseListIteratively(ListNode head) {
        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }

    public static ListNode reverseListRecursiveLy(ListNode head) {
        return doReverseListRecursiveLy(head, null);
    }

    public static ListNode doReverseListRecursiveLy(ListNode head, ListNode newHead) {
        if (head == null) {
            return newHead;
        }
        ListNode next = head.next;
        head.next = newHead;
        return doReverseListRecursiveLy(next, head);
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
