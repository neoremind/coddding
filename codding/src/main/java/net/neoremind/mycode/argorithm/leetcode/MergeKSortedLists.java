package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 * <p/>
 * 这道题目在分布式系统中非常常见，来自不同client的sorted list要在central server上面merge起来。这个题目一般有两种做法，下面一一介绍并且分析复杂度。
 * 第一种做法比较容易想到，就是有点类似于MergeSort的思路,就是分治法，不了解MergeSort的朋友，请参见 归并排序-维基百科 ，是一个比较经典的O(nlogn)
 * 的排序算法，还是比较重要的。思路是先分成两个子任务，然后递归求子任务，最后回溯回来。这个题目也是这样，先把k个list分成两半，然后继续划分，知道剩下两个list就合并起来，合并时会用到 Merge Two Sorted
 * Lists 这道题。
 * 我们来分析一下上述算法的时间复杂度。假设总共有k个list，每个list的最大长度是n，那么运行时间满足递推式T(k) = 2T(k/2)+O(n*k)。
 * 根据主定理，可以算出算法的总复杂度是O(nklogk)
 * 。如果不了解主定理的朋友，可以参见 主定理-维基百科 。空间复杂度的话是递归栈的大小O(logk)。
 * 很多参考了{@link net.neoremind.mycode.argorithm.sort.MergeSort}的代码。
 * 另外这个实现在leetcode上打败了80%的java代码。
 *
 * @author zhangxu
 */
public class MergeKSortedLists {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        return mergeKLists(lists, 0, lists.length - 1);
    }

    /**
     * 递归、回溯思想合并K个lists
     *
     * @param lists
     * @param start
     * @param end
     *
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start < end) {
            int center = (start + end) / 2;
            ListNode list1 = mergeKLists(lists, start, center);
            ListNode list2 = mergeKLists(lists, center + 1, end);
            return mergeTwoLists(list1, list2);
        }
        return lists[start];  //这种情况下start=end，证明就剩下一个node，直接返回即可。
    }

    /**
     * 合并两个链表。
     * <p/>
     * 没有用到额外的空间，均是pointer的操作
     *
     * @param listNode1
     * @param listNode2
     *
     * @return
     */
    public ListNode mergeTwoLists(ListNode listNode1, ListNode listNode2) {
        if (listNode1 == null) {
            return listNode2;
        }
        if (listNode2 == null) {
            return listNode1;
        }
        ListNode result = null;
        ListNode pointer = null;
        // 遍历两个list，小的往前排，pointer不断的在两个链表上移动，直到一个链表遍历完毕，把剩下的直接追加即可。
        while (listNode1 != null && listNode2 != null) {
            if (listNode1.val <= listNode2.val) {
                if (result == null) {  // 特殊处理下对于第一次的情况
                    result = listNode1;
                    pointer = result;
                } else {
                    pointer.next = listNode1;
                    pointer = pointer.next;
                }
                listNode1 = listNode1.next;
            } else {
                if (result == null) {
                    result = listNode2;
                    pointer = result;
                } else {
                    pointer.next = listNode2;
                    pointer = pointer.next;
                }
                listNode2 = listNode2.next;
            }
        }

        // 下面可以看做是甩尾操作
        if (listNode1 != null) {
            pointer.next = listNode1;
        }

        if (listNode2 != null) {
            pointer.next = listNode2;
        }

        return result;
    }

    @Test
    public void test() {
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
    public void testMergeTwoLists() {
        ListNode list1 = ListNodeHelper.build(new int[] {2, 3, 4});
        ListNode list2 = ListNodeHelper.build(new int[] {3, 5, 8});
        ListNode afterHead = mergeTwoLists(list1, list2);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
    }

}
