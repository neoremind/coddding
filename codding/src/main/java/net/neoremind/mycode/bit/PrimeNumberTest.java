package net.neoremind.mycode.bit;

import org.junit.Test;

import com.google.common.primitives.Ints;

/**
 * 筛1-100的素数，
 * <p>
 * 可以按照这个规则来：每个素数的倍数肯定不是素数
 * <p>
 * 2是最小的质数，也是唯一的偶数质数
 * <pre>
 *     2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97 101 103 107 109 113 127 131 137 139 149
 * </pre>
 *
 * @author zhangxu
 */
public class PrimeNumberTest {

    public boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

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
