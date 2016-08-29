package net.neoremind.mycode.argorithm.dac;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 乘方a^n
 *
 * @author zhangxu
 */
public class Power {

    /**
     * 传统的做法（所谓的Naive algorithm）就是循环相乘n次，算法效率为O(n)
     */
    public int power(int num, int n) {
        int result = 1;
        for (int i = 0; i < n; i++) {
            result *= num;
        }
        return result;
    }

    /**
     * 如果采用分治法的思想，算法效率可以提高到O(lgn)
     */
    public int powerDAC(int num, int n) {
        if (n == 0) {
            return 1;
        } else if (n == 1) {
            return num;
        } else {
            if (n % 2 == 0) {
                return powerDAC(num, n / 2) * power(num, n / 2);
            } else {
                return powerDAC(num, (n - 1) / 2) * power(num, (n - 1) / 2) * num;
            }
        }
    }

    @Test
    public void test() {
        assertThat(power(2, 2), Matchers.is(4));
        assertThat(power(2, 10), Matchers.is(1 << 10));
        assertThat(power(4, 6), Matchers.is(1 << 12));
        assertThat(power(3, 5), Matchers.is(243));

        assertThat(powerDAC(2, 2), Matchers.is(4));
        assertThat(powerDAC(2, 10), Matchers.is(1 << 10));
        assertThat(powerDAC(4, 6), Matchers.is(1 << 12));
        assertThat(powerDAC(3, 5), Matchers.is(243));
    }
}
