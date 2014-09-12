package net.neoremind.mycode.sort.heap;

import java.util.ArrayList;

/**
 * 最小堆数据结构
 * 
 * @author wangchongjie
 * @since 2012-09-05
 */
public class MinimumHeap<T extends Comparable<T>> {
	ArrayList<T> items;
	int cursor; // 用于计数

	public MinimumHeap(int size) {
		items = new ArrayList<T>(size);
		cursor = -1;
	}

	public MinimumHeap() {
		items = new ArrayList<T>();
		cursor = -1;
	}

	/**
	 * 上移操作
	 * 
	 * @param index
	 *            被上移元素的起始位置。
	 */
	void siftUp(int index) {
		T intent = items.get(index);
		while (index > 0) {
			int pindex = (index - 1) / 2;
			T parent = items.get(pindex);
			if (intent.compareTo(parent) < 0) {// 上移的条件，比父节点小
				items.set(index, parent);
				index = pindex;
			} else
				break;
		}
		items.set(index, intent);
	}

	/**
	 * 下移操作
	 * 
	 * @param index
	 *            被下移的元素的起始位置
	 */
	void siftDown(int index) {
		T intent = items.get(index);
		int leftIndex = 2 * index + 1;
		while (leftIndex < items.size()) {
			T minChild = items.get(leftIndex);
			int minIndex = leftIndex;

			int rightIndex = leftIndex + 1;
			if (rightIndex < items.size()) {
				T rightChild = items.get(rightIndex);
				if (rightChild.compareTo(minChild) < 0) {
					minChild = rightChild;
					minIndex = rightIndex;
				}
			}

			if (minChild.compareTo(intent) < 0) {
				items.set(index, minChild);
				index = minIndex;
				leftIndex = index * 2 + 1;
			} else
				break;

		}
		items.set(index, intent);
	}

	public void add(T item) {
		// 先添加到最后
		items.add(item);
		// 循环上移，以完成重构
		siftUp(items.size() - 1);
	}

	public T deleteTop() {
		if (items.isEmpty()) {
			return null;
		}
		// 先获取顶部节点
		T maxItem = items.get(0);
		T lastItem = items.remove(items.size() - 1);
		if (items.isEmpty()) {
			return lastItem;
		}
		// 将尾部的节点放置顶部，下移，完成重构
		items.set(0, lastItem);
		siftDown(0);
		return maxItem;
	}

	public T next() {
		if (cursor < 0 || cursor == (items.size() - 1)) {
			return null;
		}
		cursor++;
		return items.get(cursor);
	}

	public T first() {
		if (items.size() == 0)
			return null;
		cursor = 0;
		return items.get(0);
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public int size() {
		return items.size();
	}

	public void clear() {
		items.clear();
	}

	public static void main(String[] args) {
		MinimumHeap<Integer> heap = new MinimumHeap<Integer>();
		heap.add(2);
		heap.add(3);
		heap.add(5);
		heap.add(1);
		heap.add(4);
		heap.add(7);
		heap.add(6);

		heap.deleteTop();
		heap.deleteTop();

		while (!heap.isEmpty()) {
			System.out.println(heap.deleteTop());
		}
	}

}
