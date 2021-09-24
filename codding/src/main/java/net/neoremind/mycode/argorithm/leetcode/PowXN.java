package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Implement pow(x, n).
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/powx-n/
 * @see net.neoremind.mycode.argorithm.dac.Power
 */
public class PowXN {

    /**
     * 通用的二分查找方式
     * <pre>
     * 6^2 = 6^1 * 6^1 = 6 * 6 = 36
     * 6^1 = 6 * 6^0 * 6^0 = 6 * 1 * 1 = 6
     * 6^0 = 1
     *
     * 6^3 = 6 * 6^2 * 6^2
     *
     * 6 ^-3 = ( 6^-1 * 6^-1) / 6
     *
     * stack
     * x   n
     * 6 ^ 4 n%2==0 && n>0 return 36*36=1296
     * 6 ^ 2 n%2==0 && n>0 return 6*6=36
     * 6 ^ 1 n%2!=0 && n>0 return 6*1*1=6
     * 6 ^ 0 return 1
     *
     *
     * x   n
     * 6 ^ -3 n%2!=0 && n<0 return (1/6*1/6) / 6
     * 6 ^ -1 n%2!=0 && n<0 return (1*1)/6=1/6
     * 6 ^ 0 return 1
     * </pre>
     * <p>
     * {@link net.neoremind.mycode.argorithm.dac.Power}只支持整数以及正数的DAC
     */
    public double myPow(double x, int n) {
        if (n == 0) {
            return 1;
        }
        // 下面的可加可不加
//        if (x == 0d) {
//            return 0;
//        }
//        if (n == 1) {
//            return x;
//        }
//        if (n == -1) {
//            return 1 / x;
//        }
        //
        double temp = myPow(x, n / 2);
        if (n % 2 == 0) {
            return temp * temp;
        } else {
            if (n > 0) {
                return x * temp * temp;
            } else {
                return (temp * temp) / x;
            }
        }
    }

    @Test
    public void test() {
        assertThat(myPow(36, -2), Matchers.is(1296d));
        //assertThat(myPow(36, -2), Matchers.is(1296d));
    }
}
