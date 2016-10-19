package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class Deduplicate {

    /**
     * Deduplicate
     *
     * @param a1 A sorted list
     * @param a2 A sorted list
     *
     * @return A sorted deduplicated list including each value in either l1 or l2 exactly once
     */
    public List<Integer> deduplicate(int[] a1, int[] a2) {
        if (a1 == null || a2 == null) {
            throw new IllegalArgumentException("Input should not be null");
        }
        List<Integer> res = new ArrayList<>(); //Maybe we can predicate the size...
        int i = 0;
        int j = 0;
        while (i < a1.length && j < a2.length) {
            int temp;
            if (a1[i] < a2[j]) {
                temp = a1[i];
                i++;
            } else if (a1[i] > a2[j]) {
                temp = a2[j];
                j++;
            } else {
                temp = a1[i];
                i++;
                j++;
            }
            if (res.isEmpty() || res.get(res.size() - 1) != temp) {
                res.add(temp);
            }
        }
        while (i < a1.length) {
            if (res.isEmpty() || res.get(res.size() - 1) != a1[i]) {
                res.add(a1[i]);
            }
            i++;
        }
        while (j < a2.length) {
            if (res.isEmpty() || res.get(res.size() - 1) != a2[j]) {
                res.add(a2[j]);
            }
            j++;
        }
        return res;
    }

    @Test
    public void test() {
        int[] a1 = new int[] {1, 2, 3, 4, 5, 6, 6, 7, 7, 8};
        int[] a2 = new int[] {1, 5, 6, 8, 9, 9};
        System.out.println(deduplicate(a1, a2));
    }

}
