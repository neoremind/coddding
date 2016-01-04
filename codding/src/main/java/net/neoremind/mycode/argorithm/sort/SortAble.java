package net.neoremind.mycode.argorithm.sort;

import java.util.Arrays;

/**
 * @author zhangxu
 */
public abstract class SortAble {

    abstract void sort(int[] array);

    protected void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    protected void doTest() {
        int[] array = ArrayHelper.getShuffledArray(10);
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
    }

}
