package net.neoremind.mycode.guava.base;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class CaseFormatTest extends TestCase {

    public void testLowerHyphenToLowerUnderscore() {
        assertEquals("foo", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "foo"));
        assertEquals("foo_bar", CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "foo-bar"));
    }

    public void testLowerUnderscoreToUpperUnderscore() {
        assertEquals("FOO", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, "foo"));
        assertEquals("FOO_BAR", CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, "foo_bar"));
    }

    public void testLowerCamelToLowerHyphen() {
        assertEquals("foo", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "foo"));
        assertEquals("foo-bar", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "fooBar"));
        assertEquals("h-t-t-p", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "HTTP"));
    }

    public void testLowerCamelToUpperUnderscore() {
        assertEquals("FOO", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "foo"));
        assertEquals("FOO_BAR", CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "fooBar"));
    }
}
