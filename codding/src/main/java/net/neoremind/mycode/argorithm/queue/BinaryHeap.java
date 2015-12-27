package net.neoremind.mycode.argorithm.queue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

/**
 * 优先队列至少有以下两种操作：
 * 1）insert，插入，也就是enqueue
 * 2）deleteMin，删除最小者，也就是dequeue
 * <p/>
 * 优先队列的四种方案：
 * 1）链表，插入O(1)，删除O(N)
 * 2）有序链表，插入O(N)，删除O(1)
 * 3）二叉查找树binary search tree，平均O(logN)，但是太重了，不需要严格有序，不需要链
 * 4）二叉堆，插入最坏情况O(logN)，删除最坏情况O(logN)，相比于BST，比较轻量，使用数组即可。
 * 下面的例子便是使用二叉堆的一个实现。
 * <p/>
 * 二叉堆的概念和性质：
 * 1）一颗从左到右填满的二叉树
 * 2）任意节点i，左儿子在2i，右儿子在2i+1，父亲在i/2上，因此可以位移移动节点，效率高。
 * 3）对于每一个节点i，i的父亲值小于等于i的值，根节点除外。
 * <p/>
 * 应用场景：
 * 1）打印机作业，重要任务优先执行
 * 2）操作系统调度，有任务优先级，并不完全是一个FIFO的队列
 * 3）典型题目，N个数，求第k大的元素，
 * #3使用二叉堆解决的话，构造堆0(N)，然后进行k次deleteMin操作，O(k*logN)，总的是O(N+k*logN)，
 * 当k较大时为O(k*logN)，较小时为O(N)
 * <p/>
 * JDK中的实现是{@linkplain java.util.PriorityQueue}
 *
 * @author zhangxu
 */
public class BinaryHeap {

    /**
     * 数组
     */
    private int[] array;

    /**
     * 当前位置从1开始，array[0]空缺
     */
    private int currentSize = 0;

    /**
     * array大小
     */
    private int size;

    public BinaryHeap() {
        currentSize = 1;
        size = 1 << 3;
        array = new int[size];
    }

    /**
     * 扩容
     */
    private void resize() {
        size <<= 1;
        int[] oldArray = array;
        array = new int[size];
        System.arraycopy(oldArray, 0, array, 0, oldArray.length);
    }

    /**
     * 插入新元素n
     * <p/>
     * 在最后的元素位置上建立一个空穴，放入n，
     * 然后比较n和空穴父亲的值，把“大的爸爸”下移，以此类推，直到n放置到合适的位置
     *
     * @param n
     */
    public void insert(int n) {
        if (currentSize >= size) {
            resize();
        }
        int hole = currentSize++;
        array[hole] = n;
        percolateUp(hole);
    }

    /**
     * 上滤
     * <p/>
     * 应用于{@link #insert(int)}插入操作，新元素在堆中不断的上浮（找到父亲），直到找到正确的位置
     *
     * @param hole
     */
    private void percolateUp(int hole) {
        int tmp = array[hole];
        for (; hole > 1 && tmp < array[hole / 2]; hole = hole / 2) {
            array[hole] = array[hole / 2];
        }
        array[hole] = tmp;
    }

    /**
     * 找到最小元素很容易，就是第一个元素，难的是恢复堆的性质：
     * 需要假设把最后一个元素放到第一个元素的位置，也就是根节点，
     * 然后依次下沉，把较小的孩子换上来的思想，直到到达合适的位置。
     *
     * @return
     */
    public int deleteMin() {
        if (array.length == 0) {
            throw new RuntimeException("No more to delete");
        }
        int ret = array[1];
        array[1] = array[--currentSize];
        array[currentSize] = 0;
        percolateDown(1);
        return ret;
    }

    /**
     * 下滤
     * <p/>
     * 用于{@link #deleteMin()}删除和{@link #buildHeap(int[])}建堆。
     *
     * @param hole
     */
    private void percolateDown(int hole) {
        int tmp = array[hole];
        for (int child; hole * 2 < currentSize; hole = child) {
            child = hole * 2;
            if (child + 1 < currentSize && array[child] > array[child + 1]) {
                child++;
            }
            if (array[child] < tmp) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    /**
     * 建堆
     * <p/>
     * 每个insert O(1)
     * 下滤最坏情况是O(logN)，
     * 因此总的时间复杂度是O(N)，而不是O(NlogN)
     *
     * @param from
     */
    public void buildHeap(int[] from) {
        if (from == null || from.length == 0) {
            throw new RuntimeException("Input array should not be empty");
        }
        this.array = new int[from.length + 2];
        System.arraycopy(from, 0, this.array, 1, from.length);
        currentSize = from.length + 1;
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    public static void main(String[] args) {
        BinaryHeap bh = new BinaryHeap();
        int[] arr = new int[] {4, 2, 1, 3};
        for (int i : arr) {
            bh.insert(i);
        }
        System.out.println(bh);
        assertThat(bh.deleteMin(), is(1));
        assertThat(bh.deleteMin(), is(2));
        assertThat(bh.deleteMin(), is(3));
        assertThat(bh.deleteMin(), is(4));

        arr = new int[] {6, 0, 1, 4, 20, 2, 7, 3, 30, 5, 10};
        for (int i : arr) {
            bh.insert(i);
        }
        System.out.println(bh);
        assertThat(bh.deleteMin(), is(0));
        assertThat(bh.deleteMin(), is(1));
        assertThat(bh.deleteMin(), is(2));
        assertThat(bh.deleteMin(), is(3));
        assertThat(bh.deleteMin(), is(4));
        assertThat(bh.deleteMin(), is(5));
        assertThat(bh.deleteMin(), is(6));
        assertThat(bh.deleteMin(), is(7));
        assertThat(bh.deleteMin(), is(10));
        assertThat(bh.deleteMin(), is(20));
        assertThat(bh.deleteMin(), is(30));

        arr = new int[] {4, 2, 1, 3};
        bh.buildHeap(arr);
        System.out.println(bh);
        assertThat(bh.deleteMin(), is(1));
        assertThat(bh.deleteMin(), is(2));
        assertThat(bh.deleteMin(), is(3));
        assertThat(bh.deleteMin(), is(4));

        bh.buildHeap(new int[] {150, 80, 40, 30, 10, 70, 110, 100, 20, 90, 60});
        System.out.println(bh);
        assertThat(bh.deleteMin(), is(10));
        assertThat(bh.deleteMin(), is(20));
        assertThat(bh.deleteMin(), is(30));
        assertThat(bh.deleteMin(), is(40));
        assertThat(bh.deleteMin(), is(60));
        assertThat(bh.deleteMin(), is(70));
        assertThat(bh.deleteMin(), is(80));
        assertThat(bh.deleteMin(), is(90));
        assertThat(bh.deleteMin(), is(100));
        assertThat(bh.deleteMin(), is(110));
        assertThat(bh.deleteMin(), is(150));

        bh.buildHeap(new int[] {150, 80, 40, 30, 10, 70, 110, 100, 20, 90, 60, 50, 120, 140, 130});
        System.out.println(bh);
        assertThat(bh.deleteMin(), is(10));
        assertThat(bh.deleteMin(), is(20));
        assertThat(bh.deleteMin(), is(30));
        assertThat(bh.deleteMin(), is(40));
        assertThat(bh.deleteMin(), is(50));
        assertThat(bh.deleteMin(), is(60));
        assertThat(bh.deleteMin(), is(70));
        assertThat(bh.deleteMin(), is(80));
        assertThat(bh.deleteMin(), is(90));
        assertThat(bh.deleteMin(), is(100));
        assertThat(bh.deleteMin(), is(110));
        assertThat(bh.deleteMin(), is(120));
        assertThat(bh.deleteMin(), is(130));
        assertThat(bh.deleteMin(), is(140));
        assertThat(bh.deleteMin(), is(150));
    }
}
