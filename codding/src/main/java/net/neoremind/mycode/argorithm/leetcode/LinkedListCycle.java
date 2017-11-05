package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a linked list, determine if it has a cycle in it.
 * <p>
 * Follow up:
 * Can you solve it without using extra space?
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/linked-list-cycle/
 */
public class LinkedListCycle {

    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode walker = head;
        ListNode runner = head;
        int times = 0;
        while (runner.next != null && runner.next.next != null) {
            times++;
            walker = walker.next;
            runner = runner.next.next;
            if (walker == runner) {
                System.out.println("times " + times);
                return true;
            }
        }
        return false;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        System.out.println(head);
        assertThat(hasCycle(head), is(false));

        head = ListNodeHelper.build(new int[]{1, 2, 3, 4, 5});
        ListNode curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = head;
        assertThat(hasCycle(head), is(true));

        head = ListNodeHelper.build(new int[]{1});
        curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = head;
        assertThat(hasCycle(head), is(true));
    }
}
