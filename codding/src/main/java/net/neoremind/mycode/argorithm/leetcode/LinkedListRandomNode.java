package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;

import java.util.Random;

/**
 * Given a singly linked list, return a random node's value from the linked list. Each node must have the same probability of being chosen.
 * <p>
 * Follow up:
 * What if the linked list is extremely large and its length is unknown to you? Could you solve this efficiently without using extra space?
 * <p>
 * Example:
 * <p>
 * // Init a singly linked list [1,2,3].
 * ListNode head = new ListNode(1);
 * head.next = new ListNode(2);
 * head.next.next = new ListNode(3);
 * Solution solution = new Solution(head);
 * <p>
 * // getRandom() should return either 1, 2, or 3 randomly. Each element should have equal probability of returning.
 * solution.getRandom();
 *
 * @author xu.zhang
 */
public class LinkedListRandomNode {

    ListNode head;

    Random random;

    /**
     * @param head The linked list's head.
     *             Note that the head is guaranteed to be not null, so it contains at least one node.
     */
    public LinkedListRandomNode(ListNode head) {
        this.head = head;
        random = new Random();
    }

    /**
     * Returns a random node's value.
     */
    public int getRandom() {
        if (head == null) {
            return -1;
        }
        int count = 1;
        ListNode curr = head;
        int res = curr.val;
        while (curr != null) {
            if (random.nextInt(count++) == 0) {
                res = curr.val;
            }
            curr = curr.next;
        }
        return res;
    }

}
