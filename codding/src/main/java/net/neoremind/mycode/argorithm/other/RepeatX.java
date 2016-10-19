package net.neoremind.mycode.argorithm.other;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 实现一个function， repeat("x", a) ---> xxxxx， 能有多少种解法，就给多少种解法，最快的是logn的，大家可以去google一下，很好找的
 * <p>
 * http://www.1point3acres.com/bbs/thread-199066-1-1.html
 *
 * @author zhangxu
 */
public class RepeatX {

    String repeat1(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("x");
        }
        return sb.toString();
    }

    /**
     * recruisive, logN
     */
    String repeat2(int n) {
        if (n == 0) {
            return "";
        }
        int s = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("x");
        while (s + s <= n) {
            s += s;
            sb.append(sb.toString());
        }
        return sb.toString() + repeat2(n - s);
    }

    /**
     * dynamic programming
     */
    //TODO
    String repeat3(int n) {
        return "";
    }

    @Test
    public void test() {
        for (int i = 0; i < 1000; i++) {
            assertThat(repeat2(i), Matchers.is(repeat1(i)));
        }
    }
}
