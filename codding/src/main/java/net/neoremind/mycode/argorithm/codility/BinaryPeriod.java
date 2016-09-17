package net.neoremind.mycode.argorithm.codility;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class BinaryPeriod {

    @Test
    public void test() {
        assertThat(solution(955), Matchers.is(4));
        assertThat(solution(0b011111111110110), Matchers.is(-1));
        assertThat(solution(0b101010101010), Matchers.is(2));
        assertThat(solution(0b11111), Matchers.is(1));
        assertThat(solution(0b1111011110111), Matchers.is(5));
        assertThat(solution(0b1111011110110), Matchers.is(-1));
        assertThat(solution(100000000), Matchers.is(-1));
    }

    int solution(int n) {
        System.out.println(Integer.toBinaryString(n));
        System.out.println(n);
        int[] d = new int[30];
        int l = 0;
        int p;
        while (n > 0) {
            d[l] = n % 2;
            n /= 2;
            l++;
        }
        System.out.println("l=" + l);
        System.out.println(Arrays.toString(d));
        for (p = 1; p <= l >> 1; ++p) {
            int i;
            boolean ok = true;
            for (i = 0; i < l - p; ++i) {
                if (d[i] != d[i + p]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return p;
            }
        }
        return -1;
    }

    int originalSolution(int n) {
        int[] d = new int[30];
        int l = 0;
        int p;
        while (n > 0) {
            d[l] = n % 2;
            n /= 2;
            l++;
        }
        for (p = 1; p < 1 + l; ++p) {
            int i;
            boolean ok = true;
            for (i = 0; i < l - p; ++i) {
                if (d[i] != d[i + p]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return p;
            }
        }
        return -1;
    }

}
