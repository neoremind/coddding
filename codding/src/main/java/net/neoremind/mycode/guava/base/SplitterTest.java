package net.neoremind.mycode.guava.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class SplitterTest extends TestCase {

    private static final Splitter COMMA_SPLITTER = Splitter.on(',');

    public void testCharacterSimpleSplit() {
        String simple = "a,b,c";
        Iterable<String> letters = COMMA_SPLITTER.split(simple);
        System.out.println(letters);
    }

    public void testCharacterSimpleSplitToList() {
        String simple = "a,b,c";
        List<String> letters = COMMA_SPLITTER.limit(2).splitToList(simple);
        System.out.println(letters); //[a, b,c] 有limit所有在第二个的时候停止了split
    }

    public void testCharacterSplitWithTrim() {
        String jacksons = "arfo(Marlon)aorf  , (Michael)or fa, afro(Jackie)orfa, "
                + "ofar(Jemaine), aff(Tito)";
        Iterable<String> family = COMMA_SPLITTER
                .trimResults(CharMatcher.anyOf("afro").or(CharMatcher.WHITESPACE))
                .split(jacksons);
        System.out.println(family);
    }

    public void testPatternSplitMatchingIsGreedy() {
        String longDelimiter = "a, b,   c";
        Iterable<String> letters = Splitter.on(Pattern.compile(",\\s*"))
                .split(longDelimiter);
        System.out.println(letters);
    }

    public void testStringSplitWithDoubleDelimiterOmitEmptyStrings() {
        String doubled = "a..b.c";
        Iterable<String> letters = Splitter.on('.')
                .omitEmptyStrings().split(doubled);
        System.out.println(letters); //[a, b, c]
    }

    public void testMapSplitter_trimmedBoth() {
        Map<String, String> m = COMMA_SPLITTER
                .trimResults()
                .withKeyValueSeparator(Splitter.on(':').trimResults())
                .split("boy  : tom , girl: tina , cat  : kitty , dog: tommy ");
        ImmutableMap<String, String> expected =
                ImmutableMap.of("boy", "tom", "girl", "tina", "cat", "kitty", "dog", "tommy");
        assertThat(m, is(expected));
        System.out.println(m);
        System.out.println(expected);
    }

}
