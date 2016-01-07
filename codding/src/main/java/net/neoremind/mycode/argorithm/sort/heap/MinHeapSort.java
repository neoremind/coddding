package net.neoremind.mycode.argorithm.sort.heap;

import java.util.Arrays;

/**
 * 二叉堆满足二个特性：

1．父结点的键值总是大于或等于（小于或等于）任何一个子节点的键值。

2．每个结点的左子树和右子树都是一个二叉堆（都是最大堆或最小堆）。

当父结点的键值总是大于或等于任何一个子节点的键值时为最大堆。当父结点的键值总是小于或等于任何一个子节点的键值时为最小堆。

gif图片过程：http://upload.wikimedia.org/wikipedia/commons/4/4d/Heapsort-example.gif

 */
public class MinHeapSort {

	public static void main(String[] args) {
		int[] array = new int[] { 6, 5, 3, 1, 0, 8, 7, 2, 4 };
		// int[] array = new int[] { 1,0,2};
		sort(array);
		System.out.println(Arrays.toString(array));
	}

	public static void sort(int[] data) {
		// 构建最大堆
		buildMinHeap(data);

		for (int i = data.length - 1; i > 0; i--) {
			// 循环，每次把根节点和最后一个节点调换位置
			exchange(data, 0, i);

			// 堆的长度减少1，排除置换到最后位置的根节点
			minHeapify(data, 0, i);
		}
	}

	// 根据输入数组构建一个最大堆
	private static void buildMinHeap(int[] data) {
		for (int i = data.length / 2 - 1; i >= 0; i--) {
			minHeapify(data, i, data.length);
		}
		System.out.println("build min heap ok:");
		System.out.println(Arrays.toString(data));
	}

	// 堆调整，使其生成最小堆
	private static void minHeapify(int[] data, int parentNodeIndex, int heapSize) {
		// 左子节点索引 2*i+1
		int leftChildNodeIndex = parentNodeIndex * 2 + 1;
		// 右子节点索引 2*i+2
		int rightChildNodeIndex = parentNodeIndex * 2 + 2;
		// 最大节点索引
		int smallestNodeIndex = parentNodeIndex;

		// 如果左子节点小于父节点，则将左子节点作为最小节点
		if (leftChildNodeIndex < heapSize && data[leftChildNodeIndex] < data[parentNodeIndex]) {
			smallestNodeIndex = leftChildNodeIndex;
		}

		// 如果右子节点比最小节点还小，那么最小节点应该是右子节点
		if (rightChildNodeIndex < heapSize && data[rightChildNodeIndex] < data[smallestNodeIndex]) {
			smallestNodeIndex = rightChildNodeIndex;
		}

		// 最后，如果最小节点和父节点不一致，则交换他们的值
		if (smallestNodeIndex != parentNodeIndex) {
			exchange(data, parentNodeIndex, smallestNodeIndex);

			// 交换完父节点和子节点的值，对换了值的子节点检查是否符合最大堆的特性
			minHeapify(data, smallestNodeIndex, heapSize);
		}
	}

	private static void exchange(int[] data, int i, int j) {
		int tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}

}
