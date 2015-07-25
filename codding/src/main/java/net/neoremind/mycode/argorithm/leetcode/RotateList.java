package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given a list, rotate the list to the right by k places, where k is non-negative.
 * <p/>
 * For example:
 * Given
 * <pre>
 * 1->2->3->4->5->NULL and k = 2,
 * return 4->5->1->2->3->NULL.
 * </pre>
 * 思路解法：利用two pointer
 * 1）可以把链表变为环，然后按照滑动窗口的概念，node=head，然后node先跑，每跑一个节点计数+1，最后跑到k就是未来要上位的起始head节点。
 * 这种做法的弊端就是如果提供k非常大的测试用例：
 * <pre>
 *     1->2->NULL and k = 2000000000
 * </pre>
 * 那么就会浪费很多次徒劳的跑动。
 * <p/>
 * 2）基于#1的思路，起始跑完第一个来回，我们已经直到这个list的长度是多少了，这时候可以利用取模的方法重置k=k%length，这时k一定小于length，
 * 这样跑再多跑第二趟就可以找到这个滑动窗口的概念，node直到跑到k，runner再开始跑，node跑到最后的节点，runner也就停留在了合适的位置，
 * 也就是未来上位的开始的head。时间复杂度是O(2n)。
 * <p/>
 * 注意：对于边界值要考虑好。比如size和k的比较关系。
 */
public class RotateList {

    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        ListNode node = head;
        ListNode dest = head;
        int size = 1;
        // 直到跑到最后一个节点
        while (node.next != null) {
            // 将size看成runner，直到跑到k，runner再启动
            if (size > k) {
                dest = dest.next;
            }
            node = node.next;
            size++;
            // 当发现跑到最后一个节点，如果还没有跑到k，说明k大于size，这时重新计算k，一切重置，跑第二趟就可以结束了。
            if (node.next == null && size < k) {
                k = k % size;
                node = head;
                dest = head;
                size = 1;
            }
        }
        // 这里k<size，用于对付k%size=0的情况。
        if (k < size) {
            node.next = head;
            head = dest.next;
            dest.next = null;
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        ListNode afterHead = rotateRight(beforeHead, 2);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("4->7->2->3->6->9->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        afterHead = rotateRight(beforeHead, 0);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->3->6->9->4->7->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        afterHead = rotateRight(beforeHead, 5);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("3->6->9->4->7->2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2, 3, 6, 9, 4, 7});
        afterHead = rotateRight(beforeHead, 6);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->3->6->9->4->7->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {2});
        afterHead = rotateRight(beforeHead, 1);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->NULL"));

        beforeHead = ListNodeHelper.build(new int[] {1, 2});
        afterHead = rotateRight(beforeHead, 3);
        after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->1->NULL"));
    }

}
