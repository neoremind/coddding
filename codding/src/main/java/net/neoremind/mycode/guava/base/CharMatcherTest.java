package net.neoremind.mycode.guava.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class CharMatcherTest extends TestCase {

    /**
     * Remove Special Characters from a String
     */
    public void whenRemoveSpecialCharacters_thenRemoved() {
        String input = "H*el.lo,}12";
        CharMatcher matcher = CharMatcher.JAVA_LETTER_OR_DIGIT;
        String result = matcher.retainFrom(input);
        assertEquals("Hello12", result);
    }

    /**
     * Remove non ASCII characters from String
     */
    public void whenRemoveNonASCIIChars_thenRemoved() {
        String input = "あhello₤";
        String result = CharMatcher.ASCII.retainFrom(input);
        assertEquals("hello", result);
        result = CharMatcher.inRange('0', 'z').retainFrom(input);
        assertEquals("hello", result);
    }

    /**
     * Remove Characters not in the Charset
     */
    public void whenRemoveCharsNotInCharset_thenRemoved() {
        Charset charset = Charset.forName("cp437");
        CharsetEncoder encoder = charset.newEncoder();

        Predicate<Character> inRange = new Predicate<Character>() {
            @Override
            public boolean apply(Character c) {
                return encoder.canEncode(c);
            }
        };

        String result = CharMatcher.forPredicate(inRange)
                .retainFrom("helloは");
        assertEquals("hello", result);
    }

    /**
     * Validate String
     */
    public void whenValidateString_thenValid() {
        String input = "hello";

        boolean result = CharMatcher.JAVA_LOWER_CASE.matchesAllOf(input);
        assertTrue(result);

        result = CharMatcher.is('e').matchesAnyOf(input);
        assertTrue(result);

        result = CharMatcher.JAVA_DIGIT.matchesNoneOf(input);
        assertTrue(result);

        assertThat(CharMatcher.anyOf("abc").matchesAnyOf("abcd"), is(true));
        assertThat(CharMatcher.anyOf("abc").matchesAllOf("abc"), is(true));
        assertThat(CharMatcher.anyOf("abc").matchesNoneOf("xyz"), is(true));
        assertThat(CharMatcher.anyOf("abc").matches('a'), is(true));

        assertThat(CharMatcher.is('a').matchesAllOf("aaaa"), is(true));
        assertThat(CharMatcher.is('a').matchesAnyOf("abc"), is(true));
        assertThat(CharMatcher.is('a').matchesNoneOf("ebc"), is(true));

        assertThat(CharMatcher.JAVA_LETTER.matchesAllOf("abc"), is(true));
        assertThat(CharMatcher.JAVA_LETTER.matchesAllOf("abc%%"), is(false));
        assertThat(CharMatcher.JAVA_LETTER.or(CharMatcher.DIGIT).matchesAllOf("abc666"), is(true));
        assertThat(CharMatcher.JAVA_LETTER.or(CharMatcher.DIGIT).matchesAllOf("abc666"), is(true));
    }

    /**
     * Remove all lowercase letters
     */
    public void remove_all_lower_case_from_string() {
        String allButLowerCase = CharMatcher.JAVA_LOWER_CASE
                .negate()
                .retainFrom("B-double E double R U-N beer run");
        assertEquals("B- E  R U-N  ", allButLowerCase);
    }

    /**
     * Trim String
     */
    public void whenTrimString_thenTrimmed() {
        String input = "---hello,,,";

        String result = CharMatcher.is('-').trimLeadingFrom(input);
        assertEquals("hello,,,", result);

        result = CharMatcher.is(',').trimTrailingFrom(input);
        assertEquals("---hello", result);

        result = CharMatcher.anyOf("-,").trimFrom(input);
        assertEquals("hello", result);
    }

    /**
     * Replace from String
     */
    public void whenReplaceFromString_thenReplaced() {
        String input = "apple-banana.";

        String result = CharMatcher.anyOf("-.").replaceFrom(input, '!');
        assertEquals("apple!banana!", result);

        result = CharMatcher.is('-').replaceFrom(input, " and ");
        assertEquals("apple and banana.", result);
    }

    /**
     * Count Character Occurrences
     */
    public void whenCountCharInString_thenCorrect() {
        String input = "a, c, z, 1, 2";

        int result = CharMatcher.is(',').countIn(input);
        assertEquals(4, result);

        result = CharMatcher.inRange('a', 'h').countIn(input);
        assertEquals(2, result);
    }

    /**
     * Remove all * from string
     */
    public void remove_all_asterisk_from_string() {
        String stringWithoutAstricks = CharMatcher.is('*')
                .removeFrom("(* This is a comment.  The compiler will ignore it. *)");
        assertEquals("( This is a comment.  The compiler will ignore it. )",
                stringWithoutAstricks);
    }

    /**
     * Validate letter and digit
     */
    public void validate_all_charachters_in_string_is_digit_or_letter() {
        boolean randomPharse = CharMatcher.JAVA_LETTER_OR_DIGIT
                .matchesAllOf("wefwewef3r343fwdSVD()I#KMFI");
        assertFalse(randomPharse);
    }

    /**
     * Grade is passing
     */
    public void grade_within_passing_range() {
        boolean failedGrade = CharMatcher
                .inRange('A', 'C')
                .apply('F');
        assertFalse(failedGrade);
    }

    /**
     * Obtain digits from telephone number
     */
    public void obtain_digits_from_telephone_number() {
        String telephoneNumber = CharMatcher
                .inRange('0', '9')
                .retainFrom("123-456-7890");
        assertEquals("1234567890", telephoneNumber);

        // worried about performance
        CharMatcher digits = CharMatcher
                .inRange('0', '9')
                .precomputed();
        String teleNumber = digits.retainFrom("123-456-7890");
        assertEquals("1234567890", teleNumber);
    }

    /**
     * Count matching chars
     */
    public void count_number_of_matching_chars() {
        int numberOfDigits = CharMatcher.DIGIT.countIn("123-LevelUpLunch");
        assertEquals(3, numberOfDigits);
    }

    /**
     * Collapse whitespace to dash
     */
    public void collapse_whitespace_dash() {
        String address = "505 Williams Street";
        String addressWithDash = CharMatcher.WHITESPACE.collapseFrom(address, '-');
        assertEquals("505-Williams-Street", addressWithDash);
    }

    /**
     * Collapse a String
     */
    public void whenCollapseFromString_thenCollapsed() {
        String input = "       hel    lo      ";

        String result = CharMatcher.is(' ').collapseFrom(input, '-');
        assertEquals("-hel-lo-", result);

        result = CharMatcher.is(' ').trimAndCollapseFrom(input, '-');
        assertEquals("hel-lo", result);
    }
}
