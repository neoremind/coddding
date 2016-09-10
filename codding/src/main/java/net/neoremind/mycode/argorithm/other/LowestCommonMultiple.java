package net.neoremind.mycode.argorithm.other;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 最小公倍数
 * <p>
 * 分解质因数法
 * 短除法
 * 公式法，两个数相乘除以最大公约数
 *
 * @author zhangxu
 */
public class LowestCommonMultiple {

    GreatestCommonDivisor gcd = new GreatestCommonDivisor();

    int lcm(int m, int n) {
        return m * n / gcd.gcd1(m, n);
    }

    @Test
    public void test() {
        assertThat(lcm(45, 12), Matchers.is(180));
        assertThat(lcm(18, 6), Matchers.is(18));
        assertThat(lcm(17, 6), Matchers.is(102));
    }

}
