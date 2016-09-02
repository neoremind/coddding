package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Reverse a linked list from position m to n. Do it in-place and in one-pass.
 * <p>
 * For example:
 * Given 1->2->3->4->5->NULL, m = 2 and n = 4,
 * <p>
 * return 1->4->3->2->5->NULL.
 * <p>
 * Note:
 * Given m, n satisfy the following condition:
 * 1 ≤ m ≤ n ≤ length of list.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/reverse-linked-list-ii/
 */
public class ReverseLinkedListII {

    /**
     * 复用了{@link ReverseLinkedList}的部分逻辑
     * m=2,n=4
     * <pre>
     * last/begin  curr/tail
     *     1     ->  2     ->  3  ->  4  ->  5
     *
     *   begin      tail             last   curr
     *     1   <->  2     <-  3  <-  4      5
     *
     *  begin      tail             last   curr
     *     1   <-  2     <-  3  <-  4      5
     *     |_______________________/|
     *
     *             _________________________
     *             |                      |/
     * begin      tail             last   curr
     *     1       2     <-  3  <-  4      5
     *     |_______________________/|
     *
     * </pre>
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode curr = dummy;
        ListNode last = dummy;
        for (int i = 0; i < m; i++) {  //先到那个m的起点，保存起点为curr，前一个为last
            last = curr;
            curr = curr.next;
        }

        ListNode begin = last;
        ListNode tail = curr;
        for (int i = 0; i < n - m + 1; i++) { //从起点开始往后依次反转n-m个
            ListNode next = curr.next;
            curr.next = last;
            last = curr;
            curr = next;
        }
        begin.next = last;  //begin指向最后一个反转的地方
        tail.next = curr;  //反转的起点指向新的后面的节点

        return dummy.next;
    }

    @Test
    public void test() {
        ListNode beforeHead = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        ListNode afterHead = reverseBetween(beforeHead, 2, 4);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->4->3->2->5->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        afterHead = reverseBetween(beforeHead, 3, 5);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->2->5->4->3->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        afterHead = reverseBetween(beforeHead, 1, 2);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->1->3->4->5->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        afterHead = reverseBetween(beforeHead, 1, 5);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("5->4->3->2->1->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        afterHead = reverseBetween(beforeHead, 5, 5);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->2->3->4->5->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1});
        afterHead = reverseBetween(beforeHead, 1, 1);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {});
        afterHead = reverseBetween(beforeHead, 0, 0);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is(""));
    }

}
