package net.neoremind.mycode.argorithm.leetcode;

/**
 * You are a product manager and currently leading a team to develop a new product. Unfortunately, the latest version
 * of your product fails the quality check. Since each version is developed based on the previous version, all the
 * versions after a bad version are also bad.
 * <p>
 * Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one, which causes all the
 * following ones to be bad.
 * <p>
 * You are given an API bool isBadVersion(version) which will return whether version is bad. Implement a function to
 * find the first bad version. You should minimize the number of calls to the API.
 *
 * @author xu.zhang
 */
public class FirstBadVersion {

    public int firstBadVersion(int n) {
        int m = 1;
        while (m < n) {
            int mid = (m + n) >>> 1;
            if (isBadVersion(mid)) {
                n = mid;
            } else {
                m = mid + 1;
            }
        }
        return m;
    }

    // mocked
    boolean isBadVersion(int n) {
        return true;
    }
}
