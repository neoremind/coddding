package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * https://stackoverflow.com/questions/742013/how-to-code-a-url-shortener
 *
 * @author xu.zhang
 */
public class Base62Converter2 {

    /**
     * Base62 characters table sorted to quickly calculate decimal equivalency by compensating.
     */
    static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Returns the base 62 string of an integer.
     *
     * @return the base 62 string of an integer
     */
    public static String base62(int value) {
        final StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, BASE62.charAt(value % 62));
            value /= 62;
        } while (value != 0);
        return sb.toString();
    }

    /**
     * Returns the base 62 value of a string.
     *
     * @return the base 62 value of a string.
     */
    public static int base62(String value) {
        int result = 0;
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            result = result * 62 + BASE62.indexOf(digit);
        }
        return result;
    }

    @Test
    public void test() {
        assertThat(base62(1667), is("A3"));
        assertThat(base62("A3"), is(1667));

        assertThat(base62(0), is("a"));
        assertThat(base62("a"), is(0));

        assertThat(base62(1), is("b"));
        assertThat(base62("b"), is(1));

        assertThat(base62(2), is("c"));
        assertThat(base62("c"), is(2));

        assertThat(base62(61), is("9"));
        assertThat(base62("9"), is(61));

        // 62 * index(b) + index(a) = 62 * 1 + 0 = 62
        assertThat(base62(62), is("ba"));
        assertThat(base62("ba"), is(62));

        // 62 * index(f) + index(W) = 62 * 5 + 48 = 358
        assertThat(base62(358), is("fW"));
        assertThat(base62("fW"), is(358));
    }
}
