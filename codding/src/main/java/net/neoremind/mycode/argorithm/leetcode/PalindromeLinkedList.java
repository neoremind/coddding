package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a singly linked list, determine if it is a palindrome.
 * <p/>
 * Follow up:
 * Could you do it in O(n) time and O(1) space?
 * <p/>
 * 判断的链表是否回文
 *
 * @author zhangxu
 */
public class PalindromeLinkedList {

    /**
     * 1)首先求出链表长度
     * 2)根据#1的结果计算出中间索引mid
     * 3)遍历表，对于索引小于mid的前半部分，不用变指针，对于大于mid的后半部分，调转指针
     * 4)遍历表，依次从head何#3结果中最后的tail开始，向中间靠拢，比较值。
     * <p/>
     * 时间复杂度为O(3n)，近似于O(n)，空间复杂度O(1)。
     *
     * @param head
     *
     * @return
     */
    public static boolean isPalindromeIteratively(ListNode head) {
        ListNode curr = head;
        int count = 0;
        if (curr == null || curr.next == null) {
            return true;
        }
        while (curr != null) {
            curr = curr.next;
            count++;
        }

        curr = head;
        ListNode pre = null;
        ListNode tmp = null;
        int mid = count / 2;
        for (int i = 0; i < count; i++) {
            if (i > mid) {
                tmp = curr.next;
                curr.next = pre;
                pre = curr;
                curr = tmp;
            } else {
                pre = curr;
                curr = pre.next;
            }
        }

        for (int i = 0; i < mid; i++) {
            if (head.val != pre.val) {
                return false;
            } else {
                head = head.next;
                pre = pre.next;
            }
        }
        return true;
    }

    /**
     * bad solution:  Time Limit Exceeded
     * <p/>
     * 这个方案想法是记住head，然后遍历到tail，如果head==tail，则继续递归，head=head->next，tail删除掉。
     *
     * @param head
     *
     * @return
     */
    public static boolean isPalindromeRecursiveLy(ListNode head) {
        ListNode curr = head;
        ListNode pre = curr;
        if (curr == null) {
            return true;
        }
        while (curr.next != null) {
            pre = curr;
            curr = curr.next;
        }
        if (curr.val == head.val) {
            pre.next = null;
            return isPalindromeRecursiveLy(head.next);
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        ListNode head = ListNodeHelper.build(new int[] {2, 3});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeRecursiveLy(head), is(false));

        head = ListNodeHelper.build(new int[] {2});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeRecursiveLy(head), is(true));

        head = ListNodeHelper.build(new int[] {3, 2, 3});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeRecursiveLy(head), is(true));

        head = ListNodeHelper.build(new int[] {1, 3, 5, 3, 1});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeRecursiveLy(head), is(true));

        head = ListNodeHelper.build(new int[] {2, 3});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeIteratively(head), is(false));

        head = ListNodeHelper.build(new int[] {2});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeIteratively(head), is(true));

        head = ListNodeHelper.build(new int[] {3, 2, 3});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeIteratively(head), is(true));

        head = ListNodeHelper.build(new int[] {1, 3, 5, 3, 1});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeIteratively(head), is(true));

        head = ListNodeHelper.build(new int[] {1, 3, 4, 4, 3, 1});
        System.out.println(ListNodeHelper.getPrintableListNode(head));
        assertThat(isPalindromeIteratively(head), is(true));
    }

}
