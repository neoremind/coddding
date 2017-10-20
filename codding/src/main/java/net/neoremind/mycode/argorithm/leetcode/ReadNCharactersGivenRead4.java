package net.neoremind.mycode.argorithm.leetcode;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * The API: int read4(char *buf) reads 4 characters at a time from a file.
 * The return value is the actual number of characters read. For example, it returns 3 if there is only 3 characters
 * left in the file.
 * By using the read4 API, implement the function int read(char *buf, int n) that reads n characters from the file.
 * Note:
 * The read function will only be called once for each test case.
 * <p>
 * 这道题给了我们一个Read4函数，每次可以从一个文件中最多读出4个字符，如果文件中的字符不足4个字符时，
 * 返回准确的当前剩余的字符数。现在让我们实现一个最多能读取n个字符的函数。
 * <p>
 * https://discuss.leetcode.com/topic/6400/accepted-clean-java-solution
 *
 * @author xu.zhang
 */
public class ReadNCharactersGivenRead4 {

    /**
     * @param buf Destination buffer
     * @param n   Maximum number of characters to read
     * @return The number of characters read
     */
    public int read(char[] buf, int n) {
        char[] buffer = new char[4];
        boolean endOfFile = false;
        int readBytes = 0;

        while (readBytes < n && !endOfFile) {
            int size = read4(buffer);
            if (size != 4) {
                endOfFile = true;
            }
            int length = Math.min(n - readBytes, size);
            for (int i = 0; i < length; i++) {
                buf[readBytes + i] = buffer[i];
            }
            readBytes += length;
        }
        return readBytes;
    }

    private int read4(char[] buf) {
        for (int i = 0; i < 4; i++) {
            buf[i] = CHARS[offset % 4];
            if (offset++ > limit) {
                return i + 1;
            }
        }
        return 4;
    }

    private static final char[] CHARS = {'a', 'b', 'c', 'd'};

    private int offset = 0;

    private int limit = 10;

    @Before
    public void prepare() {
        offset = 0;
    }

    @Test
    public void test() {
        limit = 10;
        char[] buf = new char[limit];
        int res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
        assertThat(res, Matchers.is(10));
        assertThat(buf, Matchers.is(new char[]{'a', 'b', 'c', 'd', 'a', 'b', 'c', 'd', 'a', 'b'}));

        limit = 0;
        offset = 0;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
        assertThat(res, Matchers.is(0));
        assertThat(buf, Matchers.is(new char[]{}));

        limit = 3;
        offset = 0;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
        assertThat(res, Matchers.is(3));
        assertThat(buf, Matchers.is(new char[]{'a', 'b', 'c'}));

        limit = 4;
        offset = 0;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
        assertThat(res, Matchers.is(4));
        assertThat(buf, Matchers.is(new char[]{'a', 'b', 'c', 'd'}));

        limit = 5;
        offset = 0;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
        assertThat(res, Matchers.is(5));
        assertThat(buf, Matchers.is(new char[]{'a', 'b', 'c', 'd', 'a'}));
    }
}
