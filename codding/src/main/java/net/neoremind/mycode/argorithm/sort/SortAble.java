package net.neoremind.mycode.argorithm.sort;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;

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
        int[] array = ArrayHelper.getShuffledArray(8);
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
        assertThat(array, Matchers.is(ArrayHelper.getContinuousArray(8)));

        array = ArrayHelper.getShuffledArray(81);
        System.out.println(Arrays.toString(array));
        sort(array);
        System.out.println(Arrays.toString(array));
        assertThat(array, Matchers.is(ArrayHelper.getContinuousArray(81)));
    }

}
