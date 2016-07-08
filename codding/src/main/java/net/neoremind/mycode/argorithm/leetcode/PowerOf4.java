package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class PowerOf4 {

    /**
     * 1）大于0
     * 2）是2的倍数
     * 3）是2的倍数并且1在奇数位上必须。
     * <p>
     * (4 & 0x55555555) == 4
     * <p>
     * 0100
     * 0101
     * ----
     * 0100
     * <p>
     * (16 & 0x55555555) == 16
     * <p>
     * 0001 0000
     * 0101 0101
     * ---------
     * 0001 0000
     * <p>
     * (8 & 0x55555555) == 0
     * <p>
     * 1000
     * 0101
     * ----
     * 0000
     *
     * @param i
     *
     * @return
     */
    public boolean isPowerOf4(int i) {
        return i > 0 && (i & (i - 1)) == 0 && (i & 0x55555555) != 0;
    }

    @Test
    public void testPowerOf4() {
        assertThat(isPowerOf4(4), Matchers.is(true));
        assertThat(isPowerOf4(8), Matchers.is(false));
        assertThat(isPowerOf4(16), Matchers.is(true));
    }

}
