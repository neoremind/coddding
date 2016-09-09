package net.neoremind.mycode.argorithm.other;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * 十进制转换，包括10、8、2进制。
 * <p>
 * 参考JDK6的{@link Integer#toBinaryString(int)}等方法
 *
 * @author zhangxu
 */
public class BaseTransfer {

    final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    public String ten2binary(int n) {
        return toUnsignedString(n, 1);
    }

    public String ten2Octal(int n) {
        return toUnsignedString(n, 3);
    }

    public String ten2hex(int n) {
        return toUnsignedString(n, 4);
    }

    public String toUnsignedString(int n, int base) {
        char[] res = new char[32];
        int idx = 32;
        int mask = (1 << base) - 1;
        do {
            res[--idx] = digits[n & mask];
            n >>>= base;
        } while (n != 0);

        return new String(res, idx, 32 - idx);
    }

    @Test
    public void test() {
        System.out.println(Integer.MIN_VALUE);
        System.out.println(Integer.MIN_VALUE >>> 31); //1
        System.out.println(ten2binary(10));
        assertThat(ten2binary(10), Matchers.is(Integer.toBinaryString(10)));
        System.out.println(ten2binary(61234));
        assertThat(ten2binary(61234), Matchers.is(Integer.toBinaryString(61234)));
        System.out.println(ten2binary(Integer.MAX_VALUE));
        assertThat(ten2binary(Integer.MAX_VALUE), Matchers.is(Integer.toBinaryString(Integer.MAX_VALUE)));
        System.out.println(ten2binary(Integer.MAX_VALUE - 1));
        assertThat(ten2binary(Integer.MAX_VALUE - 1), Matchers.is(Integer.toBinaryString(Integer.MAX_VALUE - 1)));
        System.out.println(ten2binary(Integer.MIN_VALUE + 1));
        assertThat(ten2binary(Integer.MIN_VALUE + 1), Matchers.is(Integer.toBinaryString(Integer.MIN_VALUE + 1)));
        System.out.println(ten2binary(Integer.MIN_VALUE));
        assertThat(ten2binary(Integer.MIN_VALUE), Matchers.is(Integer.toBinaryString(Integer.MIN_VALUE)));

        System.out.println(ten2hex(10));
        assertThat(ten2hex(10), Matchers.is(Integer.toHexString(10)));
        System.out.println(ten2hex(3817));
        assertThat(ten2hex(3817), Matchers.is(Integer.toHexString(3817)));
        System.out.println(ten2hex(61234));
        assertThat(ten2hex(61234), Matchers.is(Integer.toHexString(61234)));
        System.out.println(ten2hex(Integer.MAX_VALUE));
        assertThat(ten2hex(Integer.MAX_VALUE), Matchers.is(Integer.toHexString(Integer.MAX_VALUE)));
        System.out.println(ten2hex(Integer.MAX_VALUE - 1));
        assertThat(ten2hex(Integer.MAX_VALUE - 1), Matchers.is(Integer.toHexString(Integer.MAX_VALUE - 1)));
        System.out.println(ten2hex(Integer.MIN_VALUE + 1));
        assertThat(ten2hex(Integer.MIN_VALUE + 1), Matchers.is(Integer.toHexString(Integer.MIN_VALUE + 1)));
        System.out.println(ten2hex(Integer.MIN_VALUE));
        assertThat(ten2hex(Integer.MIN_VALUE), Matchers.is(Integer.toHexString(Integer.MIN_VALUE)));
    }

}
