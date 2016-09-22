package net.neoremind.mycode.argorithm.sort;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 练习默写的merge sort
 *
 * @author zhangxu
 */
public class MergeSortPractice {

    @Test
    public void test() {
        int[] array = ArrayHelper.getShuffledArray(20);
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
        assertThat(array, Matchers.is(ArrayHelper.getContinuousArray(20)));
    }

    public void sort(int[] nums) {
        int[] tmp = new int[nums.length];
        mergeSort(nums, tmp, 0, nums.length - 1);
    }

    void mergeSort(int[] nums, int[] tmp, int start, int end) {
        if (start < end) {
            int mid = start + ((end - start) >> 1);
            mergeSort(nums, tmp, start, mid);
            mergeSort(nums, tmp, mid + 1, end);
            merge(nums, tmp, start, mid, mid + 1, end);
        }
    }

    void merge(int[] nums, int[] tmp, int ls, int le, int rs, int re) {
        int k = ls;
        int startPos = ls;
        while (ls <= le && rs <= re) {
            if (nums[ls] < nums[rs]) {
                tmp[k++] = nums[ls++];
            } else {
                tmp[k++] = nums[rs++];
            }
        }
        while (ls <= le) {
            tmp[k++] = nums[ls++];
        }
        while (rs <= re) {
            tmp[k++] = nums[rs++];
        }
        // 熟练使用JDK的函数，是个native方法
        System.arraycopy(tmp, startPos, nums, startPos, re - startPos + 1);
    }

}
