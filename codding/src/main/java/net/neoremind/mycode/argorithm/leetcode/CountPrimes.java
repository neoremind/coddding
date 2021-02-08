package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Description:
 * <p>
 * Count the number of prime numbers less than a non-negative number, n.
 *
 * 素数的加倍也是素数。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/count-primes/
 * @see net.neoremind.mycode.bit.PrimeNumberTest
 */
public class CountPrimes {

    public int countPrimes(int n) {
        int count = 0;
        int[] notPrime = new int[n / 32 + 1];
        for (int i = 2; i < n; i++) {
            if (((notPrime[i / 32] >> i % 32) & 1) == 0) {
                //System.out.println(i);
                count++;
                for (int j = i; j < n; j = j + i) {
                    notPrime[j / 32] |= 1 << (j % 32);
                }
            }
        }
        return count;
    }

    public int countPrimes2(int n) {
        int count = 0;
        boolean[] flag = new boolean[n];  // true represents that number is not prime
        for (int i = 2; i < n; i++) {
            if (!flag[i]) {
                count++;
                for (int j = i; j < n; j = j + i) {
                    flag[j] = true;
                }
            }
            // 放下面也行，但是跑的慢一些，47ms，上面的13ms，不是素数就不用加倍的，必须是素数才加倍
//            for (int j = i; j < n; j = j + i) {
//                flag[j] = true;
//            }
        }
        return count;
    }

    public int countPrimes3(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void test() {
        assertThat(countPrimes(100), Matchers.is(25));
        assertThat(countPrimes(999983), Matchers.is(78497));
        assertThat(countPrimes2(100), Matchers.is(25));
        assertThat(countPrimes2(999983), Matchers.is(78497));
        assertThat(countPrimes3(100), Matchers.is(25));
        assertThat(countPrimes3(999983), Matchers.is(78497));
    }

}
