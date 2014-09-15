package net.neoremind.mycode.sort.quick;

import java.util.Arrays;

public class QuickSort {

	public static void main(String[] args) {
		int[] arr = new int[]{5,8,1,3,6,2};
		QuickSort qs = new QuickSort();
		qs.quickSort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}
	
	/**
	 * 递归的过程，将数组一分为二，进行拆分，左边的全小于枢纽，右边的全大于枢纽，然后再分别继续递归
	 * @param arr
	 * @param low
	 * @param high
	 */
	private void quickSort(int[] arr, int low, int high) {
		if (low >= high) {
			return;
		}
		int mid = adjustAndReturnMid(arr, low, high);
		quickSort(arr, low, mid - 1);
		quickSort(arr, mid + 1, high);
	}
	
	/**
	 * 看成pivot，高位（最先小于pivot），低位（最先大于pivot）三者之间的交换。
	 * 不断的向中间聚拢，为pivot腾出位置安置的过程。
	 * @param arr
	 * @param low
	 * @param high
	 * @return
	 */
	private int adjustAndReturnMid(int[] arr, int low, int high) {
		int pivot = arr[low];
		while (low < high) {
			if (low < high && pivot <= arr[high]) { //<=很重要，否则相同的数字会遇到死循环
				high--;
			}
			arr[low] = arr[high];
			
			if (low < high && pivot >= arr[low]) {
				low++;
			}
			arr[high] = arr[low];
		}
		arr[low] = pivot;
		return low;
	}
	
}
