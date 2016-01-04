package net.neoremind.mycode.argorithm.sort;

import org.junit.Test;

/**
 * 选择排序
 * <p/>
 * n趟遍历，每一趟都往后找，找一个最小的元素，和这一趟的起点元素交换，
 * 这样n-i趟后，就会有前i个元素就是排序好的，本例中始终找最小的。
 *
 * @author zhangxu
 */
public class SelectionSort extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            if (i != min) {
                swap(i, min, array);
            }
        }
    }

}
