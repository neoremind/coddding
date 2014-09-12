package net.neoremind.mycode.tangotest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Remove duplicate elements from array. <br> Present three different ways.
 * 
 * @author zhangxu04
 */
public class ArrayTest {

	public static void main(String[] args) {
		int[] arr = new int[] { 1, 5, 4, 2, 7, 2, 6, 5 };

		/*
		 * By using JDK collection object - list, elements can be 
		 * easily removed by testing whether one elements is still
		 * in the array through invoking <code>List.contains()</code> method. <br>
		 * The performance is relatively not very satisfying when dealing 
		 * with a huge int array because the time complexity is O(n^2). 
		 */
		Object result = removeDuplicateByUsingList(arr);
		printResult(result);
		
		/*
		 * This time, by using simple manipulation of array rather than collection's 
		 * functions, we can remove duplicates without time cost like wrapping from 
		 * primitive to object and collection's own complexity. Meanwhile, there is
		 * one trick here as the tmp array grows as we iterate the original array, that
		 * can do some good to performance. At last, the time complexity is still O(n^2)
		 * by better than the previous one. 
		 */
		result = removeDuplicateBySimpleAlgorithm(arr);
		printResult(result);
		
		/*
		 * At last, we are back to leverage the benefits brought by JDK collection.
		 * By using LinkedHashSet, we maintain the order of the elements tested and 
		 * duplicates can not be added successfully because of the unique feature of Set.
		 * The code style here is very elegant. The time complexity is still O(n).
		 */
		result = removeDuplicateByUsingSet(arr);
		printResult(result);
	}

	/**
	 * Remove duplicate elements by using List <br>
	 */
	public static Integer[] removeDuplicateByUsingList(int[] arr) {
		List<Integer> list = new ArrayList<Integer>(arr.length);
		for (int i = 0; i < arr.length; i++) {
			if (!list.contains(arr[i])) {
				list.add(arr[i]);
			}
		}
		return (Integer[]) list.toArray(new Integer[list.size()]);
	}
	
	/**
	 * Remove duplicate elements by array manipulation
	 */
	private static int[] removeDuplicateBySimpleAlgorithm(int[] arr) {
		int[] tmp = new int[arr.length];
		boolean pivot = false;
		int resultIndex = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < resultIndex; j++) {
				if (tmp[j] == arr[i]) {
					pivot = true;
					break;
				}
			}
			if (pivot == false) {
				tmp[resultIndex++] = arr[i];
			}
			pivot = false;
		}
		
		int[] result = new int[resultIndex];
		System.arraycopy(tmp, 0, result, 0, result.length);
		return result;
	}

	/**
	 * Remove duplicate elements by using LinkedHashSet
	 */
	public static Integer[] removeDuplicateByUsingSet(int[] arr) {
		Set<Integer> set = new LinkedHashSet<Integer>(arr.length);
		for (int num : arr) {
			set.add(num);
		}
		return set.toArray(new Integer[set.size()]);
	}

	/**
	 * Print array after removing duplicate elements
	 * 
	 * @param result
	 * @throws IllegalArgumentException
	 */
	private static void printResult(Object result) {
		if (result == null) {
			return;
		}
		if (result instanceof Integer[]) {
			System.out.println(Arrays.toString((Integer[])result));
		} else if (result instanceof int[]) {
			System.out.println(Arrays.toString((int[])result));
		} else {
			throw new IllegalArgumentException("Argument type is wrong");
		}
	}



}
