package net.neoremind.mycode.argorithm.other;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 计算最大公约数
 * <p>
 * 分解质因数法
 * 短除法，得靠写在纸面上
 * 辗转相除法（欧几里德算法），可以实现为算法
 * <p>
 * https://www.idomaths.com/zh-Hans/hcflcm.php#findhcf
 * <p>
 * 另外还有一个stein算法
 *
 * @author zhangxu
 */
public class GreatestCommonDivisor {

    /**
     * <pre>
     *     m  / n   =      reminder
     *     64 / 40  = 1 ... 24
     *     40 / 24  = 1 ... 16
     *     24 / 16  = 1 ... 8
     *     16 / 8   = 2 ... 0
     *
     *      m  / n  =      reminder
     *     17 /  8  = 2 ... 1
     *     8  /  1  = 8 ... 0
     * </pre>
     */
    //思路：辗转相除法
    int gcd1(int m, int n) {    //方法一：循环法
        int temp;
        while (m % n != 0) {
            temp = n;
            n = m % n;
            m = temp;
        }
        return n;
    }

    int gcd2(int m, int n) {    //方法二：递归法
        if (m % n == 0) {
            return n;
        } else {
            return gcd2(n, m % n);
        }
    }

    @Test
    public void test() {
        assertThat(gcd1(45, 12), Matchers.is(3));
        assertThat(gcd1(18, 6), Matchers.is(6));
        assertThat(gcd1(17, 6), Matchers.is(1));

        assertThat(gcd2(45, 12), Matchers.is(3));
        assertThat(gcd2(18, 6), Matchers.is(6));
        assertThat(gcd2(17, 6), Matchers.is(1));
    }
}
