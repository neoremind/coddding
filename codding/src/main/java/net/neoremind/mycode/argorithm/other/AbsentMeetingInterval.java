package net.neoremind.mycode.argorithm.other;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.argorithm.leetcode.support.Interval;

/**
 * 给一组meetings（每个meeting由start和end时间组成）。求出在所有输入meeting时间段内没有会议，也就是空闲的时间段。
 * 每个subarray都已经sort好
 * 举例：
 * [
 * [[1, 3], [6, 7]],
 * [[2, 4]],
 * [[2, 3], [9, 12]]
 * ]
 * 返回.
 * [[4, 6], [7, 9]]
 *
 * @author zhangxu
 */
public class AbsentMeetingInterval {

    public List<Interval> absentInterval(List<Interval> intervals) {
        if (intervals.size() <= 1) {
            return intervals;
        }

        // Sort by ascending starting point using an anonymous Comparator
        Collections.sort(intervals, (i1, i2) -> Integer.compare(i1.start, i2.start));

        List<Interval> result = new LinkedList<>();
        int start = intervals.get(0).start;
        int end = intervals.get(0).end;

        for (Interval interval : intervals) {
            if (interval.start <= end) { // Overlapping intervals, move the end if needed
                end = Math.max(end, interval.end);
            } else {                     // Disjoint intervals, add the previous one and reset bounds
                result.add(new Interval(end, interval.start));
                start = interval.start;
                end = interval.end;
            }
        }

        // Add the last interval
        result.add(new Interval(start, end));
        return result;
    }

    @Test
    public void test() {
        List<Interval> intervals =
                Lists.newArrayList(Interval.of(1, 3), Interval.of(6, 7), Interval.of(2, 4), Interval.of(2, 3),
                        Interval.of(4, 6), Interval.of(9, 12));
        System.out.println(intervals);
        List<Interval> res = absentInterval(intervals);
        System.out.println(res);
        assertThat(res.size(), is(2));
    }

}
