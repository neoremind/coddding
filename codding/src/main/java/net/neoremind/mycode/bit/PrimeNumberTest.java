package net.neoremind.mycode.bit;

import org.junit.Test;

import com.google.common.primitives.Ints;

/**
 * 筛1-100的素数，
 * <p/>
 * 可以按照这个规则来：每个素数的倍数肯定不是素数
 *
 * @author zhangxu
 */
public class PrimeNumberTest {

    @Test
    public void testPrintAllPrimeNumbers() {
        int max = 100;
        boolean[] flag = new boolean[max];
        int[] primes = new int[max / 3 + 1];
        int i = 0;
        for (int j = 2; j < max; j++) {
            if (!flag[j]) {
                primes[i++] = j;
                for (int k = j; k < max; k += j) {
                    flag[k] = true;
                }
            }
        }

        System.out.println(Ints.asList(primes));
    }

    /**
     * 相比于{@link #testPrintAllPrimeNumbers()}在flag数组上得到了很大程度的压缩，用了bitmap的思想
     */
    @Test
    public void testPrintAllPrimeNumbers2() {
        int max = 100;
        int[] flag = new int[max / 32 + 1];
        int[] primes = new int[max / 3 + 1];
        int i = 0;
        for (int j = 2; j < max; j++) {
            if (((flag[j / 32] >> (j % 32)) & 1) == 0) {
                primes[i++] = j;
                for (int k = j; k < max; k += j) {
                    flag[k / 32] |= (1 << k % 32);
                }
            }
        }

        System.out.println(Ints.asList(primes));
    }

}
