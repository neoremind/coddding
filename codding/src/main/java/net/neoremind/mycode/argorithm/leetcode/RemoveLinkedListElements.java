package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Remove all elements from a linked list of integers that have value val.
 * <p>
 * Example
 * Given: 1 --> 2 --> 6 --> 3 --> 4 --> 5 --> 6, val = 6
 * Return: 1 --> 2 --> 3 --> 4 --> 5
 *
 * @author xu.zhang
 * @see https://leetcode.com/problems/remove-linked-list-elements/description/
 */
public class RemoveLinkedListElements {

    public ListNode removeElements(ListNode head, int val) {
        if (head == null) return null;
        head.next = removeElements(head.next, val);
        return head.val == val ? head.next : head;
    }

    /**
     * 尾递归方式
     */
    public ListNode removeElementsTailRec(ListNode head, int val) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        helper(dummy, val);
        return dummy.next;
    }

    private ListNode helper(ListNode node, int val) {
        if (node == null) {
            return null;
        } else {
            ListNode temp = node.next;
            while (temp != null && temp.val == val) {
                temp = temp.next;
            }
            node.next = temp;
            return helper(temp, val);
        }
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[]{1});
        assertThat(removeElements(head, 1), Matchers.nullValue());
        assertThat(removeElementsTailRec(head, 1), Matchers.nullValue());

        head = ListNodeHelper.build(new int[]{1, 2, 6, 3, 4, 5, 6});
        assertThat(removeElements(head, 6), Matchers.is(ListNodeHelper.build(new int[]{1, 2, 3, 4, 5})));
        assertThat(removeElementsTailRec(head, 6), Matchers.is(ListNodeHelper.build(new int[]{1, 2, 3, 4, 5})));

        head = ListNodeHelper.build(new int[]{1, 1, 1, 1, 1});
        assertThat(removeElements(head, 1), Matchers.nullValue());
        assertThat(removeElementsTailRec(head, 1), Matchers.nullValue());
    }
}
