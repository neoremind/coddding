package net.neoremind.mycode.tangotest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of finding top 100 elements out of a huge int array. <br>
 * 
 * There is an array of 10000000 different int numbers. 
 * Find out its largest 100 elements. 
 * The implementation should be optimized for executing speed.
 * 
 * @author zhangxu04
 */
public class FindTopElements {

	private static final int ARRAY_LENGTH = 10000000; // big array length
	private static final int ELEMENT_NUM_PER_GROUP = 10000; // split big array into sub-array, this represents sub-array length
	private static final int TOP_ELEMENTS_NUM = 100; // top elements number
	
	private ExecutorService executorService;

	private CompletionService<int[]> completionService;

	public FindTopElements() {
		int MAX_THREAD_COUNT = 50;
		executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
		completionService = new ExecutorCompletionService<int[]>(executorService);
	}

	/**
	 * Start from here :-)
	 * @param args
	 */
	public static void main(String[] args) {
		FindTopElements findTopElements = new FindTopElements();
		
		// Get a array which is not in order and elements are not duplicate
		int[] array = getShuffledArray(ARRAY_LENGTH);
		
		// Find top 100 elements and print them by desc order in the console
		long start = System.currentTimeMillis();
		findTopElements.findTop100(array);
		long end = System.currentTimeMillis();
		System.out.println("Costs " + (end - start) + "ms");
	}

	/**
	 * Leveraging concurrent components of JDK, we can deal small parts of the huge array concurrently.
	 * The huge array are split into several sub arrays which are submitted to a thread pool one by one.
	 * By using <code>CompletionService</code>, we can take out completed result from the pool as soon as possible, 
	 * which avoid the block issue when getting future result through a future task list by using 
	 * <code>ExcutorService</code> and <code>Future</code> class. Moreover, the can optimize the performance of 
	 * the piece of code by processing the completed results once we get them, so the overall sort invocation will 
	 * not be delayed to the final moment.
	 * 
	 */
	private void findTop100(int[] arr) {
		System.out.println("Start to compute.");
		int groupNum = (ARRAY_LENGTH / ELEMENT_NUM_PER_GROUP);
		System.out.println("Split " + ARRAY_LENGTH + " elements into " + groupNum + " groups");
		for (int i = 0; i < groupNum; i++) {
			int[] toBeSortArray = new int[ELEMENT_NUM_PER_GROUP];
			System.arraycopy(arr, i * ELEMENT_NUM_PER_GROUP, toBeSortArray, 0, ELEMENT_NUM_PER_GROUP);
			completionService.submit(new FindTop100(toBeSortArray));
		}

		try {
			int[] overallArray = new int[TOP_ELEMENTS_NUM * groupNum];
			for (int i = 0; i < groupNum; i++) {
				System.arraycopy(completionService.take().get(), 0, overallArray, i * TOP_ELEMENTS_NUM, TOP_ELEMENTS_NUM);
			}
			Arrays.sort(overallArray);
			for (int i = 1; i <= TOP_ELEMENTS_NUM; i++) {
				System.out.println(overallArray[TOP_ELEMENTS_NUM * groupNum - i]);
			}
			System.out.println("Finish to output result.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		executorService.shutdown();
	}

	/**
	 * Callable of finding top 100 elements <br>
	 * The steps are as below:
	 * 1) Quick sort a array
	 * 2) Get reverse 100 elements and put them into a new array
	 * 3) return the new array
	 */
	private class FindTop100 implements Callable<int[]> {

		private int[] array;

		public FindTop100(int[] array) {
			this.array = array;
		}

		@Override
		public int[] call() throws Exception {
			int len = array.length;
			Arrays.sort(array);
			int[] result = new int[TOP_ELEMENTS_NUM];
			int index = 0;
			for (int i = 1; i <= TOP_ELEMENTS_NUM; i++) {
				result[index++] = array[len - i];
			}
			return result;
		}

	}

	/**
	 * Get shuffled int array
	 * 
	 * @return array not in order and elements are not duplicate
	 */
	private static int[] getShuffledArray(int len) {
		System.out.println("Start to generate test array... this may take several seconds.");
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
