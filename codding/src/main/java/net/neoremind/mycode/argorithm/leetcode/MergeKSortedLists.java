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
 * 另外这个实现在leetcode上打败了79%的java代码。
 *
 * @author zhangxu
 */
public class MergeKSortedLists {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        // 处理lists为空或者长度为0的情况，否则就会StackOverFlow
        return doMergeKLists(lists, 0, lists.length - 1);
    }

    public ListNode doMergeKLists(ListNode[] lists, int left, int right) {
        if (left >= right) {
            return lists[left];
        } else {
            int mid = left + ((right - left) >> 1);
            ListNode l1 = doMergeKLists(lists, left, mid);
            ListNode l2 = doMergeKLists(lists, mid + 1, right);
            return mergeTwoSortedList(l1, l2);  //参考题目21
        }
    }

    ListNode mergeTwoSortedList(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (a != null && b != null) {
            if (a.val < b.val) {
                curr.next = a;
                a = a.next;
            } else {
                curr.next = b;
                b = b.next;
            }
            curr = curr.next;
        }
        if (a != null) {
            curr.next = a;
        }
        if (b != null) {
            curr.next = b;
        }
        return dummy.next;
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
        ListNode afterHead = mergeTwoSortedList(list1, list2);
        String after = ListNodeHelper.getPrintableListNode(afterHead);
        System.out.println(after);
    }

}
