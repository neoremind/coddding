package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement int sqrt(int x).
 * <p>
 * Compute and return the square root of x.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/sqrtx/
 */
public class SqrtX {

    public int mySqrtBruteForce(int x) {
        for (int i = 0; i <= x; i++) {
            if ((i + 1) * (i + 1) < x) {
                continue;
            } else if ((i + 1) * (i + 1) > x) {
                return i;
            } else {
                return i + 1;
            }
        }
        throw new RuntimeException("This should not happen!");
    }

    /**
     * 按照23为例，
     * <pre>
     *     left   right   mid  mid^2         ans
     *       1     23     11    121  >  23    -
     *       1     10     5      25  >  23    -
     *       1     4      2      4   <= 23    4
     *       5     4                        quit
     * </pre>
     * <p>
     * 按照25为例，
     * <pre>
     *     left   right   mid  mid^2         ans
     *       1     25     13    169  >  25    -
     *       1     12     6      36  >  25    -
     *       1     5      3      9   <= 25    3
     *       4     5      4      16  <= 25    4
     *       5     5      5      25  <= 25    5
     *       6     5                         quit
     * </pre>
     */
    public int mySqrtBinarySearch(int x) {
        if (0 == x) {
            return 0;
        }
        int left = 1;
        int right = x;
        int ans = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid <= x / mid) {
                left = mid + 1;
                ans = mid;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    public int sqrt(int num) {
        if (num < 1) {
            return num;
        }
        long left = 1, right = num;// long type to avoid 2147483647 case

        int ans = -1;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long t = mid * mid;
            if (t > num) {
                right = mid - 1;
            } else if (t <= num) {
                left = mid + 1;
                ans = (int) mid;
            }
        }

        return ans;
    }

    @Test
    public void test() {
        assertThat(Math.sqrt(144), Matchers.is(12d));

        assertThat(mySqrtBruteForce(0), Matchers.is(0));
        assertThat(mySqrtBruteForce(1), Matchers.is(1));
        assertThat(mySqrtBruteForce(2), Matchers.is(1));
        assertThat(mySqrtBruteForce(16), Matchers.is(4));
        assertThat(mySqrtBruteForce(36), Matchers.is(6));
        assertThat(mySqrtBruteForce(144), Matchers.is(12));
        assertThat(mySqrtBruteForce(23), Matchers.is(4));
        assertThat(mySqrtBruteForce(37), Matchers.is(6));
        assertThat(mySqrtBruteForce(168), Matchers.is(12));
        assertThat(mySqrtBruteForce(169), Matchers.is(13));

        assertThat(mySqrtBinarySearch(0), Matchers.is(0));
        assertThat(mySqrtBinarySearch(1), Matchers.is(1));
        assertThat(mySqrtBinarySearch(2), Matchers.is(1));
        assertThat(mySqrtBinarySearch(16), Matchers.is(4));
        assertThat(mySqrtBinarySearch(36), Matchers.is(6));
        assertThat(mySqrtBinarySearch(144), Matchers.is(12));
        assertThat(mySqrtBinarySearch(23), Matchers.is(4));
        assertThat(mySqrtBinarySearch(37), Matchers.is(6));
        assertThat(mySqrtBinarySearch(168), Matchers.is(12));
        assertThat(mySqrtBinarySearch(169), Matchers.is(13));
        assertThat(mySqrtBinarySearch(2147483647), Matchers.is(46340));
    }
}
