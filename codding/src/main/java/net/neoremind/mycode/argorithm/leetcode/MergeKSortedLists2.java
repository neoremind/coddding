package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 * <p/>
 * 这种方法用到了堆的数据结构，思路比较难想到，但是其实原理比较简单。
 * 维护一个大小为k的堆，每次去堆顶的最小元素放到结果中，然后读取该元素的下一个元素放入堆中，重新维护好。
 * 因为每个链表是有序的，每次又是去当前k个元素中最小的，所以当所有链表都读完时结束，这个时候所有元素按从小到大放在结果链表中。
 * 这个算法每个元素要读取一次，即是k*n次，然后每次读取元素要把新元素插入堆中要logk的复杂度，所以总时间复杂度是O(nklogk)
 * 。空间复杂度是堆的大小，即为O(k)。
 * 这个实现在leetcode上打败了71%的java代码。
 * 本解法没有用到java自带的优先队列PriorityQueue，而是自己实现的。
 *
 * @author zhangxu
 */
public class MergeKSortedLists2 {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }

        // 先把所有的输入都放到待堆排数组中，也许会包含空的ListNode，因为记录了currentHeapSize
        // 所以在下面利用System.arraycopy将空的剔除出去，重新建立一个待堆排数组
        ListNode[] heapListMightContainsEmpty = new ListNode[lists.length];
        ListNode result = null;
        ListNode pointer = null;
        int index = 0;
        for (ListNode list : lists) {
            if (list == null) {
                continue;
            }
            heapListMightContainsEmpty[index] = list;
            index++;
            currentHeapSize++;
        }
        ListNode[] heapList = new ListNode[currentHeapSize];
        System.arraycopy(heapListMightContainsEmpty, 0, heapList, 0, currentHeapSize);

        buildHeap(heapList); // 将初始的堆进行二叉堆构建一次
        // 不断的循环，把最小堆的堆顶取出来，然后继续维持这个堆的特性
        // 可以看做是这个链表的各个元素不断在堆中
        // 1）滑动，移动到后面的next
        // 2）不断的下滤，当不符合堆特性的时候，移动链表的在数组中的位置
        while (true) {
            if (result == null) {  // 处理特殊的第一次情况，因为此时结果result和pointer游标都是空的
                result = deleteMin(heapList);
                pointer = result;
            } else {
                pointer.next = deleteMin(heapList); // 去除堆顶的就是最小的元素，方法内部又重新把堆的特性维护好了
                pointer = pointer.next;
            }
            if (pointer == null) {  // 当所有的链表都遍历完了之后堆顶就是一个NULL，此时满足条件退出即可
                break;
            }
            // System.out.println(Arrays.toString(heapList));
        }
        return result;
    }

    /**
     * 记录heapList的实际大小，某些链表遍历完毕，就需要递减
     */
    int currentHeapSize;

    /**
     * 初始化建堆，此时假设K个链表的是
     * <pre>
     *     2->3->4->NULL
     *     3->5->8->NULL
     *     1->6->NULL
     * </pre>
     * 这个<code>heapList</code>的形态就是
     * <pre>
     *     [2,3,1]
     * </pre>
     * <pre>
     *              2      ->3->4->NULL
     *      /             \
     *     3->5->8->NULL   1->6->NULL
     * </pre>
     * 经过这个方法后，即建堆完成，结果就是
     * <pre>
     *     [1,3,2]
     * </pre>
     * <pre>
     *             1      ->6->NULL
     *      /             \
     *     3->5->8->NULL   2->3->4->NULL
     * </pre>
     * 也就是做了一回percolateDown下滤操作，把堆顶换成最小的，同时其他节点都维持堆的特性。
     *
     * @param heapList
     */
    private void buildHeap(ListNode[] heapList) {
        for (int i = heapList.length / 2 - 1; i >= 0; i--) {
            percolateDown(heapList, i, heapList.length);
        }
    }

    /**
     * 堆调整，使其生成最小堆，用于删除{@link #deleteMin(ListNode[])}和建堆{@link #buildHeap(ListNode[])}
     */
    private static void percolateDown(ListNode[] data, int parentNodeIndex, int heapSize) {
        // 左子节点索引 2*i+1
        int leftChildNodeIndex = parentNodeIndex * 2 + 1;
        // 右子节点索引 2*i+2
        int rightChildNodeIndex = parentNodeIndex * 2 + 2;
        // 最大节点索引
        int smallestNodeIndex = parentNodeIndex;

        // 如果左子节点小于父节点，则将左子节点作为最小节点
        if (leftChildNodeIndex < heapSize && data[leftChildNodeIndex].val < data[parentNodeIndex].val) {
            smallestNodeIndex = leftChildNodeIndex;
        }

        // 如果右子节点比最小节点还小，那么最小节点应该是右子节点
        if (rightChildNodeIndex < heapSize && data[rightChildNodeIndex].val < data[smallestNodeIndex].val) {
            smallestNodeIndex = rightChildNodeIndex;
        }

        // 最后，如果最小节点和父节点不一致，则交换他们的值
        if (smallestNodeIndex != parentNodeIndex) {
            exchange(data, parentNodeIndex, smallestNodeIndex);

            // 交换完父节点和子节点的值，对换了值的子节点检查是否符合最大堆的特性
            percolateDown(data, smallestNodeIndex, heapSize);
        }
    }

    /**
     * 交换元素
     */
    private static void exchange(ListNode[] data, int i, int j) {
        ListNode tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    /**
     * 删除最小堆的堆顶元素用于返回，然后使堆顶的链表后移，然后利用percolateDown下滤，重新维护堆的特性。
     * <p/>
     * 这里注意一旦一个链表移动最后了，也就是NULL，这里的处理方法是将链表的最后一个元素（链表）提到堆顶，然后currentHeapSize--，
     * 也就是忽略最后一个元素，然后再重新建堆，知道啥也不剩下了。
     *
     * @param heapList
     *
     * @return
     */
    private ListNode deleteMin(ListNode[] heapList) {
        ListNode ret = null;
        if (currentHeapSize > 0) {
            ret = heapList[0];
            if (ret.next == null) {
                heapList[0] = heapList[currentHeapSize - 1];
                currentHeapSize--;
            } else {
                heapList[0] = ret.next;
            }

            percolateDown(heapList, 0, currentHeapSize);
        }
        return ret;
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
