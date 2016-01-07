package net.neoremind.mycode.argorithm.sort.heap;

import java.util.Arrays;

public class MaxHeapSort {

    public static void main(String[] args) {
        int[] array = new int[] {6, 5, 3, 1, 0, 8, 7, 2, 4};
        //int[] array = new int[] { 1,0,2};
        sort(array);
        System.out.println(Arrays.toString(array));
    }

    public static void sort(int[] data) {
        // 构建最大堆
        buildMaxHeap(data);

        for (int i = data.length - 1; i > 0; i--) {
            // 循环，每次把根节点和最后一个节点调换位置
            exchange(data, 0, i);

            // 堆的长度减少1，排除置换到最后位置的根节点
            maxHeapify(data, 0, i);
        }
    }

    // 根据输入数组构建一个最大堆
    private static void buildMaxHeap(int[] data) {
        for (int i = data.length / 2 - 1; i >= 0; i--) {
            maxHeapify(data, i, data.length);
        }
        System.out.println("build max heap ok:");
        System.out.println(Arrays.toString(data));
    }

    // 堆调整，使其生成最大堆
    private static void maxHeapify(int[] data, int parentNodeIndex, int heapSize) {
        // 左子节点索引 2*i+1
        int leftChildNodeIndex = parentNodeIndex * 2 + 1;
        // 右子节点索引 2*i+2
        int rightChildNodeIndex = parentNodeIndex * 2 + 2;
        // 最大节点索引
        int largestNodeIndex = parentNodeIndex;

        // 如果左子节点大于父节点，则将左子节点作为最大节点
        if (leftChildNodeIndex < heapSize && data[leftChildNodeIndex] > data[parentNodeIndex]) {
            largestNodeIndex = leftChildNodeIndex;
        }

        // 如果右子节点比最大节点还大，那么最大节点应该是右子节点
        if (rightChildNodeIndex < heapSize && data[rightChildNodeIndex] > data[largestNodeIndex]) {
            largestNodeIndex = rightChildNodeIndex;
        }

        // 最后，如果最大节点和父节点不一致，则交换他们的值
        if (largestNodeIndex != parentNodeIndex) {
            exchange(data, parentNodeIndex, largestNodeIndex);

            // 交换完父节点和子节点的值，对换了值的子节点检查是否符合最大堆的特性
            maxHeapify(data, largestNodeIndex, heapSize);
        }
    }

    private static void exchange(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

}
