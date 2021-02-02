package net.neoremind.mycode.argorithm.sort;

import java.util.Arrays;

/**
 * 计数排序
 *
 * @author zhangxu
 */
public class CountSort {

    public static void sort(int[] a, int maxVal) {
        int[] bucket = new int[maxVal + 1];

        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = 0;
        }

        for (int i = 0; i < a.length; i++) {
            bucket[a[i]]++;
        }

        int outPos = 0;
        for (int i = 0; i < bucket.length; i++) {
            for (int j = 0; j < bucket[i]; j++) {
                a[outPos++] = i;
            }
        }
    }

    public static void main(String[] args) {
        int maxVal = 5;
        int[] data = {5, 3, 0, 2, 4, 1, 0, 5, 2, 3, 1, 4};

        System.out.println("Before: " + Arrays.toString(data));
        sort(data, maxVal);
        System.out.println("After:  " + Arrays.toString(data));
    }
}
