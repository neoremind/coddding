package net.neoremind.mycode.argorithm.other;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.junit.Test;

import java.util.Stack;

/**
 * @author xu.zhang
 */
public class ReversePrintLinkedList {
    // Input:
// [0] -> [1] -> [4]
// Output:
// 4
// 1
// 0

    void reverse(ListNode node) {
        Stack<Integer> stack = new Stack<>();
        while (node != null) {
            stack.push(node.val);
            node = node.next;
        }
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    // time: O(N)
// space: O(1)
    void reverse2(ListNode node) {
        ListNode newHead = null;
        while (node != null) {
            ListNode next = node.next;
            node.next = newHead;
            newHead = node;
            node = next;
        }
        while (newHead != null) {
            System.out.println(newHead.val);
            newHead = newHead.next; //
        }
    }

    // time: O(N),
// space: O(N) include system stack
    void reverse3(ListNode node) {
        if (node == null) {
            return;
        }
        reverse3(node.next);
        System.out.println(node.val);
    }


    // 错误1：传错了i
    // 错误2：i++
    void reverse4(ListNode node) {
        // count how manys nodes we have
        int n = count(node);
        for (int i = 1; i <= n; i++) {
            printlnLastNNode(node, i);
        }
    }

    // 错误1：传错了i
    // 错误2：方法内部错误，fast为空，会抛NPE
    void reverse5(ListNode node) {
        // count how manys nodes we have
        int n = count(node);
        for (int i = n - 1; i >= 0; i--) {
            printlnLastNNode2_(node, i);
        }
    }

    public int count(ListNode node) {
        int count = 0;
        while (node != null) {
            node = node.next;
            count++;
        }
        return count;
    }

//slow  fast
// 1->2->3->4->5
// last 4

    /**
     * @param node
     * @param n    the last n node in the linked list - node
     */
    void printlnLastNNode(ListNode node, int n) {
        ListNode slow = node;
        ListNode fast = node;
        int step = 0;
        while (step++ < n && fast != null) {
            fast = fast.next;
        }
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }
        System.out.println(slow.val);
    }

    void printlnLastNNode2(ListNode node, int n) {
        ListNode fast = node;
        int step = 0;
        while (step++ < n && fast != null) {
            fast = fast.next;
        }
        System.out.println(fast.val);
    }

    void printlnLastNNode2_(ListNode node, int n) {
        ListNode fast = node;
        int step = 0;
        while (step++ < n && fast != null) {
            fast = fast.next;
        }
        if (fast == null) {
            return;
        }
        System.out.println(fast.val);
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reverse(head);
        System.out.println("=======");

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reverse2(head);
        System.out.println("=======");

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reverse3(head);
        System.out.println("=======");

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reverse5(head);
        System.out.println("=======");

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        reverse4(head);
        System.out.println("=======");
    }

}
