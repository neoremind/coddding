package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;
import java.util.Comparator;

import net.neoremind.mycode.argorithm.leetcode.support.Interval;

/**
 * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei),
 * determine if a person could attend all meetings.
 * <p>
 * For example, Given [[0, 30],[5, 10],[15, 20]], return false.
 * <p>
 * Paid test
 * <p>
 * 这题和Merge Intervals很像，我们按开始时间把这些Interval
 * 都给排序后，就挨个检查是否有冲突就行了。有冲突的定义是开始时间小于之前最晚的结束时间。这里之前最晚的结束时间不一定是上一个的结束时间，所以我们更新的时候要取最大值。
 *
 * @author zhangxu
 */
public class MeetingRooms {

    public boolean canAttendMeetings(Interval[] intervals) {
        if (intervals == null) {
            return false;
        }

        // Sort the intervals by start time
        Arrays.sort(intervals, new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                return a.start - b.start;
            }
        });

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i].start < intervals[i - 1].end) {
                return false;
            }
        }

        return true;
    }

}
