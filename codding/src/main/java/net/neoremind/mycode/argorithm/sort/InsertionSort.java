package net.neoremind.mycode.argorithm.sort;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertThat;

/**
 * 插入排序
 * <p/>
 * 每一趟都重新确认一个起点，暂时叫做所谓的“新人”，新人初来乍到，总得找个安身之所，
 * 所以就和前面已经按照顺序就坐的“老人”们比较，依次往前比，大的依次靠后“挪动”，直到这个新来的
 * 刚刚大于前面的兄弟，就是他的座次了，“插入”到这里即可。
 * 这里注意：不断的重复这个过程，越往后挪动的兄弟可能越多，就会越慢了。
 *
 * @author zhangxu
 */
public class InsertionSort extends SortAble {

    @Test
    public void test() {
        doTest();
    }

    @Override
    public void sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int currentValue = array[i];
            int position = i;
            for (int j = i - 1; j >= 0; j--) {
                if (array[j] > currentValue) {
                    array[j + 1] = array[j];
                    position -= 1;
                } else {
                    break;
                }
            }

            array[position] = currentValue;
        }
    }

    public void sort2(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int currentValue = array[i];
            int left = 0;
            int right = i - 1;
            while (left <= right) {
                int mid = (left + right) >>> 1;
                if (array[mid] >= currentValue) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            for (int j = i - 1; j >= left; j--) {
                array[j + 1] = array[j];
            }
            array[left] = currentValue;
        }
    }

    @Test
    public void testBinarySearchInsertionSort() {
        int[] array = ArrayHelper.getShuffledArray(8);
        System.out.println(Arrays.toString(array));
        sort2(array);
        System.out.println(Arrays.toString(array));
        assertThat(array, Matchers.is(ArrayHelper.getContinuousArray(8)));

        array = ArrayHelper.getShuffledArray(81);
        System.out.println(Arrays.toString(array));
        sort2(array);
        System.out.println(Arrays.toString(array));
        assertThat(array, Matchers.is(ArrayHelper.getContinuousArray(81)));
    }

}
