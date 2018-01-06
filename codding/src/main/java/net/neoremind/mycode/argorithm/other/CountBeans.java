package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * There are 4N of beans in 4 colors: N red, N yellow, N green and N blue.  We take out 1, 2 or 3 beans in the SAME color randomly each time until no beans. We record the number and the color of beans we take each time. To the end, we get a sequence of numbers. For example, if N is 3, we may get the following number sequences.
 * <p>
 * 3R 3Y 3G 3B
 * <p>
 * 3Y 3G 3B 3R
 * <p>
 * 1R 3Y 2R 3G 2B 1B
 * <p>
 * ...
 * <p>
 * How many possible colored number sequences we can get for a given N? Write a program to calculate it.
 */
public class CountBeans {
    long permutation(int N) {
        Map<String, Long> mem = new HashMap<>();
        String str = "";
        return helper(N, N, N, N, str, mem);
    }

    long helper(int a, int b, int c, int d, String str, Map<String, Long> mem) {
        if (mem.containsKey(encode(a, b, c, d))) {
            return mem.get(encode(a, b, c, d));
        }
        if (a == 0 && b == 0 && c == 0 && d == 0) {
            return 1L;
        }
        long res = 0;
        for (int k = 1; k <= 3 && a >= k; k++) {
            str += k + "A";
            res += helper(a - k, b, c, d, str, mem);
            str = str.substring(0, str.length() - 2);
        }
        for (int k = 1; k <= 3 && b >= k; k++) {
            str += k + "B";
            res += helper(a, b - k, c, d, str, mem);
            str = str.substring(0, str.length() - 2);
        }
        for (int k = 1; k <= 3 && c >= k; k++) {
            str += k + "C";
            res += helper(a, b, c - k, d, str, mem);
            str = str.substring(0, str.length() - 2);
        }
        for (int k = 1; k <= 3 && d >= k; k++) {
            str += k + "D";
            res += helper(a, b, c, d - k, str, mem);
            str = str.substring(0, str.length() - 2);
        }
        mem.put(encode(a, b, c, d), res);
        return res;
    }

    String encode(int a, int b, int c, int d) {
        return a + "-" + b + "-" + c + "-" + d;
    }

    @Test
    public void test() {
        System.out.println(permutation(1));
        System.out.println(permutation(2));
        System.out.println(permutation(3));
        System.out.println(permutation(4));
        System.out.println(permutation(5));
        System.out.println(permutation(6));
    }
}
