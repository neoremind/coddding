package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * https://stackoverflow.com/questions/742013/how-to-code-a-url-shortener
 *
 * @author xu.zhang
 */
public class BaseTransfer2 {

    static final String BASE36 = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String base(int value, int base) {
        final StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, BASE36.charAt(value % base));
            value /= base;
        } while (value != 0);
        return sb.toString();
    }

    public static int base(String value, int base) {
        int result = 0;
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            result = result * base + BASE36.indexOf(digit);
        }
        return result;
    }


    public String ten2binary(int n) {
        return base(n, 1);
    }

    public String ten2Octal(int n) {
        return base(n, 8);
    }

    public String ten2hex(int n) {
        return base(n, 16);
    }

    public int hex2ten(String value) {
        return base(value, 16);
    }

    @Test
    public void test() {
        assertThat(ten2hex(0), is("0"));
        assertThat(ten2hex(1), is("1"));
        assertThat(ten2hex(15), is("f"));
        assertThat(ten2hex(16), is("10"));
        assertThat(ten2hex(17), is("11"));
        assertThat(ten2hex(32), is("20"));
        assertThat(ten2hex(150), is("96"));

        for (int i = 0; i < 100000; i++) {
            // System.out.println(ten2hex(i));
            assertThat(ten2hex(i), is(Integer.toHexString(i)));
            assertThat(i, is(hex2ten(ten2hex(i))));
        }
    }
}
