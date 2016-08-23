package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a linked list, swap every two adjacent nodes and return its head.
 * <p>
 * For example,
 * Given 1->2->3->4, you should return the list as 2->1->4->3.
 * <p>
 * Your algorithm should use only constant space. You may not modify the values in the list, only nodes itself can be
 * changed.
 * <p>
 * 一次性写对，画个图什么就都知道了。
 * <p>
 * 还是利用一个dummy作为开头。这里注意使用了curr.next.next.next看着恐怖，但是逻辑正确即可。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/swap-nodes-in-pairs/
 */
public class SwapNodesInPairs {

    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode curr = dummy;
        while (curr.next != null && curr.next.next != null) {
            ListNode t = curr.next.next;
            ListNode temp = curr.next.next.next;
            curr.next.next.next = curr.next;
            curr.next.next = temp;
            curr.next = t;
            curr = curr.next.next;
        }
        return dummy.next;
    }

    @Test
    public void test() {
        ListNode l = ListNodeHelper.build(new int[] {1});
        ListNode result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));

        l = ListNodeHelper.build(new int[] {1, 2});
        result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));

        l = ListNodeHelper.build(new int[] {1, 2, 3});
        result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));

        l = ListNodeHelper.build(new int[] {1, 2, 3, 4});
        result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));

        l = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));

        l = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5, 6});
        result = swapPairs(l);
        System.out.println(ListNodeHelper.getPrintableListNode(result));
    }
}
