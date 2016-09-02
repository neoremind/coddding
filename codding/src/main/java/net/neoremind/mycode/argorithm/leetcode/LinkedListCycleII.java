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
 * <p>
 * Define two pointers slow and fast. Both start at head node, fast is twice as fast as slow. If it reaches the end
 * it means there is no cycle, otherwise eventually it will eventually catch up to slow pointer somewhere in the cycle.
 * <p>
 * Let the distance from the first node to the the node where cycle begins be A, and let say the slow pointer travels
 * travels A+B. The fast pointer must travel 2A+2B to catch up. The cycle size is N. Full cycle is also how much more
 * fast pointer has traveled than slow pointer at meeting point.
 * <p>
 * A+B+N = 2A+2B
 * N=A+B
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/linked-list-cycle/
 */
public class LinkedListCycleII {

    /**
     * X是runner（快指针）和walker（慢指针）相遇的点，设
     * 1）从head到cycle起点的距离是A
     * 2）cycle起点到X的举例是B，
     * 3）环的长度是N
     * 那么根据快慢指针的追的特性可以得出：
     * A+B+N = 2A+2B
     * 那么可以推出：
     * N=A+B
     * 所以从X点再走A步，也相当于从head走A步，肯定相遇的点就是cycle的起点。
     * <p>
     * <pre>            B
     *                   o->o->X->o
     *                   |        |
     *                   o        o
     *    A              |        |
     * o->o->o->o->o->o->o        o
     *                   |        | A
     *                   |        |
     *                   o<-o<-o<-o
     * </pre>
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode walker = head;
        ListNode runner = head;
        while (runner.next != null && runner.next.next != null) {
            walker = walker.next;
            runner = runner.next.next;
            if (walker == runner) {
                ListNode slow = walker;
                while (head != slow) {
                    slow = slow.next;
                    head = head.next;
                }
                return slow;
            }
        }
        return null;
    }

    @Test
    public void test() {
        ListNode head = ListNodeHelper.build(new int[] {1, 2, 3, 4, 5});
        ListNode curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = head;
        assertThat(detectCycle(head).val, is(1));
    }
}
