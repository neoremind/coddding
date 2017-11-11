package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

/**
 * Given a positive integer num, write a function which returns True if num is a perfect square else False.
 * <p>
 * Note: Do not use any built-in library function such as sqrt.
 * <p>
 * Example 1:
 * <p>
 * Input: 16
 * Returns: True
 * Example 2:
 * <p>
 * Input: 14
 * Returns: False
 * <p>
 * <p>
 * https://leetcode.com/problems/valid-perfect-square/
 *
 * @author zhangxu
 */
public class ValidPerfectSquare {

    public boolean isPerfectSquare(int num) {
        if (num < 1) {
            return false;
        }
        long left = 1, right = num;// long type to avoid 2147483647 case

        while (left <= right) {
            long mid = left + (right - left) / 2;
            long t = mid * mid;
            if (t > num) {
                right = mid - 1;
            } else if (t < num) {
                left = mid + 1;
            } else {
                return true;
            }
        }

        return false;
    }

    @Test
    public void test() {
        for (int i = 0; i < 19; i++) {
            System.out.println(i + " " + isPerfectSquare(i));
        }
    }

}
