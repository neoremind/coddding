package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Divide two integers without using multiplication, division and mod operator.
 * <p>
 * If it is overflow, return MAX_INT.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/divide-two-integers/
 */
public class DivideTwoIntegers {

    public int divide(int dividend, int divisor) {
        //Reduce the problem to positive long integer to make it easier.
        //Use long to avoid integer overflow cases.
        int sign = 1;
        if ((dividend > 0 && divisor < 0) || (dividend < 0 && divisor > 0)) {
            sign = -1;
        }
        long ldividend = Math.abs((long) dividend);
        long ldivisor = Math.abs((long) divisor);

        //Take care the edge cases.
        if (ldivisor == 0) {
            return Integer.MAX_VALUE;
        }
        if ((ldividend == 0) || (ldividend < ldivisor)) {
            return 0;
        }

        long lans = ldivide(ldividend, ldivisor);

        int ans;
        if (lans > Integer.MAX_VALUE) { //Handle overflow.
            ans = (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else {
            ans = (int) (sign * lans);
        }
        return ans;
    }

    /**
     * 按照421/3演示：
     * <pre>
     *     3+3=6       < 421    1+1=2个3
     *     6+6=12      < 421    2+2=4个3
     *     12+12=24    < 421    4+4=8个3
     *     24+24=48    < 421    8+8=16个3
     *     48+48=96    < 421    16+16=32个3
     *     96+96=192   < 421    32+32=64个3
     *     192+192=384 < 421    64+64=128个3
     *     384+384=768 > 421
     *     ----------------
     *     reminder is 421 - 384 = 37
     * </pre>
     * <p>
     * <pre>
     *     3+3=6       < 37    1+1=2个3
     *     6+6=12      < 37    2+2=4个3
     *     12+12=24    < 37    4+4=8个3
     *     24+24=48    > 37
     *     ----------------
     *     reminder is 37 - 24 = 13
     * </pre>
     * <p>
     * <pre>
     *     3+3=6       < 13    1+1=2个3
     *     6+6=12      < 13    2+2=4个3
     *     12+12=24    > 13
     *     ----------------
     *     reminder is 13 - 12 = 1
     * </pre>
     * <p>
     * <pre>
     *     3+3=6       > 1 退出
     * </pre>
     * <p>
     * 所以上面一共出现了128个3 + 8个3 + 4个3 = 140个3，所以结果就是140，这是*3之后最接近421的值。
     */
    private long ldivide(long ldividend, long ldivisor) {
        // Recursion exit condition
        if (ldividend < ldivisor) {
            return 0;
        }

        //  Find the largest multiple so that (divisor * multiple <= dividend),
        //  whereas we are moving with stride 1, 2, 4, 8, 16...2^n for performance reason.
        //  Think this as a binary search.
        long sum = ldivisor;
        long multiple = 1;
        while ((sum + sum) <= ldividend) {
            sum += sum;
            multiple += multiple;
        }
        //Look for additional value for the multiple from the reminder (dividend - sum) recursively.
        return multiple + ldivide(ldividend - sum, ldivisor);
    }

    @Test
    public void test() {
        assertThat(divide(421, 3), Matchers.is(140));
        System.out.println(divide(2, 1)); //2
        System.out.println(divide(1, 2)); //0
        System.out.println(divide(4, 2)); //2
        System.out.println(divide(5, 2)); //2
        System.out.println(divide(6, 2)); //3
        System.out.println(divide(6, -2)); //-3
        System.out.println(divide(-6, 2)); //-3 验证符号
        System.out.println(divide(10, 0)); //MAX_INT 验证除数是0
        System.out.println(divide(0, 10)); //0 验证被除数是0
        System.out.println(divide(Integer.MAX_VALUE, 1)); //MAX_INT
        System.out.println(divide(Integer.MIN_VALUE, -1)); //解决res>max_int的overflow case，否则就会得到min_int，是个负数，并且Math
        // .abs里面必须有long转型。
    }
}
