package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * {@link Flatten2DVector}变种，需要支持remove
 *
 * @author zhangxu
 */
public class Flatten2DVectorWithRemove {

    static class Vector2D {
        List<List<Integer>> list;
        int row = 0;
        int col = 0;

        public Vector2D(List<List<Integer>> vec2d) {
            this.list = new ArrayList<>(vec2d.size());
            for (List<Integer> l : vec2d) {
                // 只将非空的迭代器加入数组
                if (l != null && l.size() > 0) {
                    list.add(l);
                }
            }
        }

        public int next() {
            Integer ret = list.get(row).get(col);
            if (col == list.get(row).size() - 1) {
                row++;
                col = 0;
            } else {
                col++;
            }
            return ret;
        }

        public boolean hasNext() {
            return row < list.size();
        }

        public void remove() {
            List<Integer> l;
            int tempRow;
            int tempCol;
            if (col == 0) {
                assert row > 0; // assume remove after calling next
                tempRow = row - 1;
                l = list.get(tempRow);
                tempCol = l.size() - 1;
            } else {
                tempRow = row;
                l = list.get(tempRow);
                tempCol = col - 1;
                col--;
            }
            l.remove(tempCol);
            if (l.isEmpty()) {
                list.remove(tempRow);
                row--;
            }
        }

        @Override
        public String toString() {
            return "Vector2D{" +
                    "list=" + list +
                    '}';
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

        //remove
        vec2d = Lists.newArrayList(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(3, 4, 5),
                Lists.newArrayList(6),
                Lists.newArrayList(7, 8, 9)
        );
        v = new Vector2D(vec2d);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        v.remove();
        System.out.println(v);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());

        //remove first col
        vec2d = Lists.newArrayList(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(3, 4, 5),
                Lists.newArrayList(6),
                Lists.newArrayList(7, 8, 9)
        );
        v = new Vector2D(vec2d);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        v.remove();
        System.out.println(v);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());

        //remove one element
        vec2d = Lists.newArrayList(
                Lists.newArrayList(1, 2),
                Lists.newArrayList(3, 4, 5),
                Lists.newArrayList(6),
                Lists.newArrayList(7, 8, 9)
        );
        v = new Vector2D(vec2d);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
        v.remove();
        System.out.println(v);
        System.out.println(v.next());
        System.out.println(v.next());
        System.out.println(v.next());
    }

}
