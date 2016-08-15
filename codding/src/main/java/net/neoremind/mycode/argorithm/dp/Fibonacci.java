package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Fibonacci递归、非递归、DP
 *
 * @author zhangxu
 */
public class Fibonacci {

    public int fibonacciWithDP(int n) {
        int[] fib = new int[n + 1];
        for (int i = 0; i < fib.length; i++) {
            if (i == 0) {
                fib[i] = 0;
            } else if (i == 1) {
                fib[i] = 1;
            } else {
                fib[i] = fib[i - 2] + fib[i - 1];
            }
        }
        return fib[n];
    }

    public int nonRecursiveFibonacci(int n) {
        int f1 = 1; //初始第二个，靠后
        int f2 = 1; //初始第一个，靠前
        int r = 0;
        if (n <= 2) {
            return 1;
        } else {
            for (int i = 3; i <= n; i++) {
                r = f1 + f2;
                f2 = f1;
                f1 = r;
            }
            return r;
        }
    }

    public int recursiveFibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2);
    }

    @Test
    public void test() {
        assertThat(recursiveFibonacci(1), Matchers.is(1));
        assertThat(recursiveFibonacci(2), Matchers.is(1));
        assertThat(recursiveFibonacci(3), Matchers.is(2));
        assertThat(recursiveFibonacci(4), Matchers.is(3));
        assertThat(recursiveFibonacci(5), Matchers.is(5));
        assertThat(recursiveFibonacci(6), Matchers.is(8));
        assertThat(recursiveFibonacci(7), Matchers.is(13));
        assertThat(recursiveFibonacci(8), Matchers.is(21));
        assertThat(recursiveFibonacci(9), Matchers.is(34));
        assertThat(recursiveFibonacci(10), Matchers.is(55));

        assertThat(nonRecursiveFibonacci(10), Matchers.is(55));
        assertThat(fibonacciWithDP(10), Matchers.is(55));
    }
}
