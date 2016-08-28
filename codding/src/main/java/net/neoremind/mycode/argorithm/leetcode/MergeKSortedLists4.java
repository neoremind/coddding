package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Comparator;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 * <p>
 * 使用自己写的优先队列Binary Heap来实现
 *
 * @author zhangxu
 */
public class MergeKSortedLists4 {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }

        BinaryHeap<ListNode> priorityQueue = new BinaryHeap<>(lists.length, new Comparator<ListNode>() {
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

    class BinaryHeap<T> {
        Object[] array;
        int currentSize;
        int size;
        Comparator<? super T> comparator;

        public BinaryHeap(int size, Comparator<? super T> comparator) {
            array = new Object[size + 2];
            currentSize = 1;
            this.size = size;
            this.comparator = comparator;
        }

        public boolean isEmpty() {
            return currentSize == 1;
        }

        public void add(T t) {
            int hole = currentSize++;
            array[hole] = t;
            siftUp(hole);
        }

        public T poll() {
            T ret = (T) array[1];
            array[1] = array[--currentSize];
            array[currentSize] = null;
            siftDown(1);
            return ret;
        }

        private void siftUp(int hole) {
            T tmp = (T) array[hole];
            for (; hole > 1 && comparator.compare(tmp, (T) array[hole / 2]) < 0; hole = hole / 2) {
                array[hole] = array[hole / 2];
            }
            array[hole] = tmp;
        }

        private void siftDown(int hole) {
            T tmp = (T) array[hole];
            for (int child; hole * 2 < currentSize; hole = child) {
                child = hole * 2;
                if (child + 1 < currentSize && comparator.compare((T) array[child], (T) array[child + 1]) > 0) {
                    child++;
                }
                if (comparator.compare((T) array[child], (T) tmp) < 0) {
                    array[hole] = array[child];
                } else {
                    break;
                }
            }
            array[hole] = tmp;
        }

    }

    @Test
    public void testK4() {
        ListNode list1 = ListNodeHelper.build(new int[] {-8, -7, -7, -5, 1, 1, 3, 4});
        ListNode list2 = ListNodeHelper.build(new int[] {-2});
        ListNode list3 = ListNodeHelper.build(new int[] {-10, -10, -7, 0, 1, 3});
        ListNode list4 = ListNodeHelper.build(new int[] {2});
        ListNode[] listNodes = new ListNode[] {list1, list2, list3, list4};
        ListNode afterHead = mergeKLists(listNodes);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
        assertThat(after, is("-10->-10->-8->-7->-7->-7->-5->-2->0->1->1->1->2->3->3->4->NULL"));
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
