package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Implement an iterator to flatten a 2d vector.
 * <p>
 * For example, Given 2d vector =
 * <p>
 * [
 * [1,2],
 * [3],
 * [4,5,6]
 * ]
 * By calling next repeatedly until hasNext returns false, the order of elements returned by next should be: [1,2,3,
 * 4,5,6].
 * <p>
 * PAID
 *
 * @author zhangxu
 */
public class Flatten2DVector {

    static class Vector2D {
        List<Iterator<Integer>> its;
        int curr = 0;

        public Vector2D(List<List<Integer>> vec2d) {
            this.its = new ArrayList<>();
            for (List<Integer> l : vec2d) {
                // 只将非空的迭代器加入数组
                if (l != null && l.size() > 0) {
                    this.its.add(l.iterator());
                }
            }
        }

        public int next() {
            Integer res = its.get(curr).next();
            // 如果该迭代器用完了，换到下一个
            if (!its.get(curr).hasNext()) {
                curr++;
            }
            return res;
        }

        public boolean hasNext() {
            return curr < its.size() && its.get(curr).hasNext();
        }
    }

    @Test
    public void test() {
        List<List<Integer>> vec2d = Lists.newArrayList(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(3, 4, 5),
                Lists.newArrayList(6),
                Lists.newArrayList(7, 8, 9)
        );
        Vector2D v = new Vector2D(vec2d);
        int i = 1;
        while (v.hasNext()) {
            assert v.next() == i++;
        }

        // for null and empty
        vec2d = Lists.newArrayList(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(3, 4, 5),
                null,
                Lists.newArrayList(6),
                Lists.newArrayList(),
                Lists.newArrayList(7, 8, 9)
        );
        v = new Vector2D(vec2d);
        i = 1;
        while (v.hasNext()) {
            assert v.next() == i++;
        }

        // corner case
        vec2d = Lists.newArrayList(
                Lists.newArrayList()
        );
        v = new Vector2D(vec2d);
        assert v.hasNext() == false;
    }

}
