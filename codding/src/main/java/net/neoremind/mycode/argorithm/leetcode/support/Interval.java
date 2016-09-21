package net.neoremind.mycode.argorithm.leetcode.support;

/**
 * @author zhangxu
 */
public class Interval {
    public int start;
    public int end;

    public Interval() {
        start = 0;
        end = 0;
    }

    public static Interval of(int s, int e) {
        return new Interval(s, e);
    }

    public Interval(int s, int e) {
        start = s;
        end = e;
    }

    @Override
    public String toString() {
        return "[" + start +
                "," + end +
                ']';
    }
}
