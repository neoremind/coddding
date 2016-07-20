package net.neoremind.mycode.guava.base;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class CharsetsTest extends TestCase {

    public void testUsAscii() {
        assertEquals(Charset.forName("US-ASCII"), Charsets.US_ASCII);
    }

    public void testIso88591() {
        assertEquals(Charset.forName("ISO-8859-1"), Charsets.ISO_8859_1);
    }

    public void testUtf8() {
        assertEquals(Charset.forName("UTF-8"), Charsets.UTF_8);
    }

    public void testUtf16be() {
        assertEquals(Charset.forName("UTF-16BE"), Charsets.UTF_16BE);
    }

    public void testUtf16le() {
        assertEquals(Charset.forName("UTF-16LE"), Charsets.UTF_16LE);
    }

    public void testUtf16() {
        assertEquals(Charset.forName("UTF-16"), Charsets.UTF_16);
    }

}
