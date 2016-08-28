package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 * <p>
 * 使用JDK自带的{@link PriorityQueue}来实现
 *
 * @author zhangxu
 */
public class MergeKSortedLists3 {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }

        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(lists.length, new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                if (o1.val < o2.val) {
                    return -1;
                } else if (o1.val > o2.val) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (ListNode node : lists) {
            if (node != null) {
                priorityQueue.add(node);
            }
        }

        while (!priorityQueue.isEmpty()) {
            ListNode node = priorityQueue.poll();
            curr.next = node;
            curr = curr.next;
            if (node.next != null) {
                priorityQueue.add(node.next);
            }
        }

        return dummy.next;
    }

    @Test
    public void testK3() {
        ListNode list1 = ListNodeHelper.build(new int[] {2, 3, 4});
        ListNode list2 = ListNodeHelper.build(new int[] {3, 5, 8});
        ListNode list3 = ListNodeHelper.build(new int[] {1, 6});
        ListNode[] listNodes = new ListNode[] {list1, list2, list3};
        ListNode afterHead = mergeKLists(listNodes);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("1->2->3->3->4->5->6->8->NULL"));
    }

    @Test
    public void testK2() {
        ListNode list1 = ListNodeHelper.build(new int[] {2, 3, 4});
        ListNode list2 = ListNodeHelper.build(new int[] {3, 5, 8});
        ListNode[] listNodes = new ListNode[] {list1, list2};
        ListNode afterHead = mergeKLists(listNodes);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("2->3->3->4->5->8->NULL"));
    }

}
