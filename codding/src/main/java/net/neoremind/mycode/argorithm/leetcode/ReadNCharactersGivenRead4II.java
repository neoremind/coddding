package net.neoremind.mycode.argorithm.leetcode;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * The API: int read4(char *buf) reads 4 characters at a time from a file.
 * <p>
 * The return value is the actual number of characters read. For example, it returns 3 if there is only 3 characters
 * left in the file.
 * <p>
 * By using the read4 API, implement the function int read(char *buf, int n) that reads n characters from the file.
 * <p>
 * Note: The read function may be called multiple times.
 * <p>
 * 这道题是之前那道Read N Characters Given Read4的拓展，那道题说read函数只能调用一次，而这道题说read函数可以调用多次，那么难度就增加了，为了更简单直观的说明问题，我们举个简单的例子吧，比如：
 * <p>
 * buf = "ab", [read(1),read(2)]，返回 ["a","b"]
 * <p>
 * 那么第一次调用read(1)后，从buf中读出一个字符，那么就是第一个字符a，然后又调用了一个read(2)，想取出两个字符，但是buf中只剩一个b了，所以就把取出的结果就是b。再来看一个例子：
 * <p>
 * buf = "a", [read(0),read(1),read(2)]，返回 ["","a",""]
 * <p>
 * 第一次调用read(0)，不取任何字符，返回空，第二次调用read(1)，取一个字符，buf中只有一个字符，取出为a，然后再调用read(2)，想取出两个字符，但是buf中没有字符了，所以取出为空。
 *
 * @author xu.zhang
 */
public class ReadNCharactersGivenRead4II {

    /**
     * @param buf Destination buffer
     * @param n   Maximum number of characters to read
     * @return    The number of characters read
     */
    char[] prevBuf = new char[4];
    int prevSize = 0;
    int prevIndex = 0;

    public int read(char[] buf, int n) {
        int counter = 0;

        while (counter < n) {
            if (prevIndex < prevSize) {
                buf[counter++] = prevBuf[prevIndex++];
            } else {
                prevSize = read4(prevBuf);
                prevIndex = 0;
                if (prevSize == 0) {
                    // no more data to consume from stream
                    break;
                }
            }
        }
        return counter;
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
        limit = 2;
        char[] buf = new char[limit];
        int res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
//        assertThat(res, Matchers.is(10));
//        assertThat(buf, Matchers.is(new char[]{'a', 'b', 'c', 'd', 'a', 'b', 'c', 'd', 'a', 'b'}));

        limit = 0;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));

        limit = 3;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));

        limit = 4;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));

        limit = 1;
        buf = new char[limit];
        res = read(buf, limit);
        System.out.println(ArrayUtils.toString(buf));
    }
}
