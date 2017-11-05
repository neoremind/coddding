package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.Interval;

/**
 * Given a set of non-overlapping intervals, insert a new interval into the intervals (merge if necessary).
 * <p>
 * You may assume that the intervals were initially sorted according to their start times.
 * <p>
 * Example 1:
 * Given intervals [1,3],[6,9], insert and merge [2,5] in as [1,5],[6,9].
 * <p>
 * Example 2:
 * Given [1,2],[3,5],[6,7],[8,10],[12,16], insert and merge [4,9] in as [1,2],[3,10],[12,16].
 * <p>
 * This is because the new interval [4,9] overlaps with [3,5],[6,7],[8,10].
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/insert-interval/
 */
public class InsertInterval {

    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
        List<Interval> result = new ArrayList<Interval>();
        // 技巧就是用interval做一个pivot为null到时候就不断的添加，这是做尾巴
        // 如果是i.end < newInterval.start就是头，也不断添加
        // 遇到overlap的时候就不断的扩张
        // 走到不能扩张的时候i.start > newInterval.end，就加进去，并且把interval=null
        for (Interval i : intervals) {
            if (newInterval == null || i.end < newInterval.start) {
                result.add(i);
            } else if (i.start > newInterval.end) {
                result.add(newInterval);
                result.add(i);
                newInterval = null;
            } else {
                newInterval.start = Math.min(newInterval.start, i.start);
                newInterval.end = Math.max(newInterval.end, i.end);
            }
        }
        if (newInterval != null) {
            result.add(newInterval);
        }
        return result;
    }

    public List<Interval> insert2(List<Interval> intervals, Interval newInterval) {
        if (intervals.size() <= 1) {
            return intervals;
        }
        List<Interval> res = new ArrayList<Interval>();
        Collections.sort(intervals, (a, b) -> Integer.compare(a.start, b.start));
        boolean hasInsert = false;
        for (Interval i : intervals) {
            if (hasInsert || newInterval.start > i.end) {
                res.add(i);
            } else if (newInterval.end < i.start) {
                res.add(newInterval);
                hasInsert = true;
                res.add(i);
            } else {
                newInterval.start = Math.min(newInterval.start, i.start);
                newInterval.end = Math.max(newInterval.end, i.end);
            }
        }
        if (!hasInsert) {
            res.add(newInterval);
        }
        return res;
    }

    @Test
    public void test() {
        List<Interval> intervals =
                Lists.newArrayList(Interval.of(1, 3), Interval.of(2, 6), Interval.of(8, 10), Interval.of(15, 18));
        System.out.println(insert(intervals, Interval.of(10, 15)));
        assertThat(insert(intervals, Interval.of(10, 15)).size(), is(3));

        intervals =
                Lists.newArrayList(Interval.of(1, 3), Interval.of(6, 9));
        System.out.println(insert(intervals, Interval.of(2, 5)));
        assertThat(insert(intervals, Interval.of(2, 5)).size(), is(2));

        intervals =
                Lists.newArrayList(Interval.of(1, 2), Interval.of(3, 5), Interval.of(6, 7), Interval.of(8, 10),
                        Interval.of(12, 16));
        System.out.println(insert(intervals, Interval.of(4, 9)));
        assertThat(insert(intervals, Interval.of(4, 9)).size(), is(3));

        intervals =
                Lists.newArrayList(Interval.of(1, 5));
        System.out.println(insert(intervals, Interval.of(2, 7)));
        assertThat(insert(intervals, Interval.of(2, 7)).size(), is(1));

        intervals =
                Lists.newArrayList(Interval.of(1, 5));
        System.out.println(insert(intervals, Interval.of(6, 8)));
        assertThat(insert(intervals, Interval.of(6, 8)).size(), is(2));
    }
}
