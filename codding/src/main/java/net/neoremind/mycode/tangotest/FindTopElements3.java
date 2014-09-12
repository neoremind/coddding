package net.neoremind.mycode.tangotest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of finding top 100 elements out of a huge int array. <br>
 * 
 * There is an array of 10000000 different int numbers. Find out its largest 100
 * elements. The implementation should be optimized for executing speed. <br>
 * 
 * Note: This is the third version of implementation, this time I make the best out
 * of the heap sort algorithm by using a minimum heap. The heap maintains the top biggest
 * numbers that guarantees the minimum number is removed every time a new number is added
 * to the heap. It saves memory usage to the limit by just using an array which size is 101
 * and a few temp elements. However, the performance is not as good as the bit map way but 
 * better than the multiple thread way. 
 * 
 * @author zhangxu04
 */
public class FindTopElements3 {

	private static final int ARRAY_LENGTH = 10000000; // big array length

	public static void main(String[] args) {
		FindTopElements3 fte = new FindTopElements3();

		// Get a array which is not in order and elements are not duplicate
		int[] array = getShuffledArray(ARRAY_LENGTH);

		// Find top 100 elements and print them by desc order in the console
		long start = System.currentTimeMillis();
		fte.findTop100(array);
		long end = System.currentTimeMillis();
		System.out.println("Costs " + (end - start) + "ms");
	}

	public void findTop100(int[] arr) {
		MinimumHeap heap = new MinimumHeap(100);
		for (Integer i : arr) {
			heap.add(i);
			if (heap.size() > 100) {
				heap.deleteTop();
			}
		}
		for (int i = 0; i < 100; i++) {
			System.out.println(heap.deleteTop());
		}
	}

	/**
	 * Get shuffled int array
	 * 
	 * @return array not in order and elements are not duplicate
	 */
	private static int[] getShuffledArray(int len) {
		System.out
				.println("Start to generate test array... this may take several seconds.");
		List<Integer> list = new ArrayList<Integer>(len);
		for (int i = 0; i < len; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		int[] ret = new int[len];
		for (int i = 0; i < len; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}

}

class MinimumHeap {

	int[] items;
	int size;

	public MinimumHeap(int size) {
		items = new int[size + 1];
		size = 0;
	}

	void shiftUp(int index) {
		int intent = items[index];
		while (index > 0) {
			int pindex = (index - 1) / 2;
			int parent = items[pindex];
			if (intent < parent) {
				items[index] = parent;
				index = pindex;
			} else {
				break;
			}
		}
		items[index] = intent;
	}

	void shiftDown(int index) {
		int intent = items[index];
		int leftIndex = 2 * index + 1;
		while (leftIndex < size) {
			int minChild = items[leftIndex];
			int minIndex = leftIndex;

			int rightIndex = leftIndex + 1;
			if (rightIndex < size) {
				int rightChild = items[rightIndex];
				if (rightChild < minChild) {
					minChild = rightChild;
					minIndex = rightIndex;
				}
			}

			if (minChild < intent) {
				items[index] = minChild;
				index = minIndex;
				leftIndex = index * 2 + 1;
			} else {
				break;
			}
		}
		items[index] = intent;
	}

	public void add(int item) {
		items[size++] = item;
		shiftUp(size - 1);
	}

	public int deleteTop() {
		if (size < 1) {
			return 0;
		}
		int maxItem = items[0];
		int lastItem = items[size - 1];
		size--;
		if (size < 1) {
			return lastItem;
		}
		items[0] = lastItem;
		shiftDown(0);
		return maxItem;
	}

	public boolean isEmpty() {
		return size < 1;
	}

	public int size() {
		return size;
	}

	/**
	 * MinimumHeap main test
	 * @param args
	 */
	public static void main(String[] args) {
		MinimumHeap heap = new MinimumHeap(7);
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