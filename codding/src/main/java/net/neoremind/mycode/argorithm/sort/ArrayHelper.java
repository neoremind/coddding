package net.neoremind.mycode.argorithm.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.primitives.Ints;

/**
 * 排序的数组帮助类
 *
 * @author zhangxu
 */
public class ArrayHelper {

    /**
     * Get shuffled int array
     *
     * @return array not in order and elements are not duplicate
     */
    public static int[] getShuffledArray(int len) {
        List<Integer> list = new ArrayList<Integer>(len);
        for (int i = 0; i < len; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return Ints.toArray(list);
    }

    /**
     * Get continuous int array
     *
     * @return array in order and elements are not duplicate
     */
    public static int[] getContinuousArray(int len) {
        List<Integer> list = new ArrayList<Integer>(len);
        for (int i = 0; i < len; i++) {
            list.add(i);
        }
        return Ints.toArray(list);
    }

}
