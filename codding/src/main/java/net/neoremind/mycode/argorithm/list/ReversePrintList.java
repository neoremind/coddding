package net.neoremind.mycode.argorithm.list;

import java.util.Stack;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * 逆序打印一个链表
 *
 * @author zhangxu
 */
public class ReversePrintList {

    public void reversePrintList(ListNode l) {
        if (l != null) {
            reversePrintList(l.next);
            System.out.println(l.val);
        }
    }

    public void reversePrintList2(ListNode l) {
        Stack<Integer> stack = new Stack<>();
        while (l != null) {
            stack.push(l.val);
            l = l.next;
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    @Test
    public void test() {
        ListNode l = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        reversePrintList(l);

        l = ListNodeHelper.build(new int[] {1});
        reversePrintList(l);

        l = ListNodeHelper.build(new int[] {});
        reversePrintList(l);

        l = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        reversePrintList2(l);

        l = ListNodeHelper.build(new int[] {1});
        reversePrintList2(l);

        l = ListNodeHelper.build(new int[] {});
        reversePrintList2(l);
    }
}
