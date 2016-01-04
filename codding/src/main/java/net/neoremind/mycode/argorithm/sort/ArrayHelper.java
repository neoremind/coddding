package net.neoremind.mycode.argorithm.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
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

        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

}
