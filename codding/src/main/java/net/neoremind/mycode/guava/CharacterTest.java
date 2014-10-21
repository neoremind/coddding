package net.neoremind.mycode.guava;

/**
 * http://www.slideshare.net/tomaszdziurko/google-guava-almost-everything-you-need-to-know
 * 
 * https://github.com/tdziurko/Guava-Lessons/blob/master/src/test/java/pl/tomaszdziurko/guava/base/CharMatcherTest.java
 * 
 * @author helechen
 *
 */
public class CharacterTest {

//	@Test
//	public void shouldMatchChar() throws Exception {
//		assertThat(CharMatcher.anyOf("gZ").matchesAnyOf("anything")).isTrue();
//	}
//
//	@Test
//	public void shouldNotMatchChar() throws Exception {
//		assertThat(CharMatcher.noneOf("xZ").matchesAnyOf("anything")).isTrue();
//	}
//
//	@Test
//	public void shouldMatchAny() throws Exception {
//		assertThat(CharMatcher.ANY.matchesAllOf("anything")).isTrue();
//	}
//
//	@Test
//	public void shouldMatchBreakingWhitespace() throws Exception {
//		assertThat(CharMatcher.BREAKING_WHITESPACE.matchesAllOf("\r\n\r\n")).isTrue();
//	}
//
//	@Test
//	public void shouldMatchDigits() throws Exception {
//		assertThat(CharMatcher.DIGIT.matchesAllOf("1231212")).isTrue();
//	}
//
//	@Test
//	public void shouldMatchDigitsWithWhitespace() throws Exception {
//		assertThat(CharMatcher.DIGIT.matchesAnyOf("1231 aa212")).isTrue();
//	}
//
//	@Test
//	public void shouldRetainOnlyDigits() throws Exception {
//		assertThat(CharMatcher.DIGIT.retainFrom("Hello 1234 567")).isEqualTo("1234567");
//	}
//
//	@Test
//	public void shouldRetainDigitsOrWhiteSpaces() throws Exception {
//		assertThat(CharMatcher.DIGIT.or(CharMatcher.WHITESPACE).retainFrom("Hello 1234 567")).isEqualTo(" 1234 567");
//	}
//
//	@Test
//	public void shouldRetainNothingAsConstrainsAreExcluding() throws Exception {
//		assertThat(CharMatcher.DIGIT.and(CharMatcher.JAVA_LETTER).retainFrom("Hello 1234 567")).isEqualTo("");
//	}
//
//	@Test
//	public void shouldRetainLettersAndDigits() throws Exception {
//		assertThat(CharMatcher.DIGIT.or(CharMatcher.JAVA_LETTER).retainFrom("Hello 1234 567"))
//				.isEqualTo("Hello1234567");
//	}
//
//	@Test
//	public void shouldCollapseAllDigitsByX() throws Exception {
//		assertThat(CharMatcher.DIGIT.collapseFrom("Hello 1234 567", 'x')).isEqualTo("Hello x x");
//	}
//
//	@Test
//	public void shouldReplaceAllDigitsByX() throws Exception {
//		assertThat(CharMatcher.DIGIT.replaceFrom("Hello 1234 567", 'x')).isEqualTo("Hello xxxx xxx");
//	}
//
//	@Test
//	public void shouldCountDigits() throws Exception {
//		assertThat(CharMatcher.DIGIT.countIn("Hello 1234 567")).isEqualTo(7);
//	}
//
//	@Test
//	public void shouldReturnFirstIndexOfFirstWhitespace() throws Exception {
//		assertThat(CharMatcher.WHITESPACE.indexIn("Hello 1234 567")).isEqualTo(5);
//	}
//
//	@Test
//	public void shouldReturnLastIndexOfFirstWhitespace() throws Exception {
//		assertThat(CharMatcher.WHITESPACE.lastIndexIn("Hello 1234 567")).isEqualTo(10);
//	}
//
//	@Test
//	public void shouldRemoveDigitsBetween3and6() throws Exception {
//		assertThat(CharMatcher.inRange('3', '6').removeFrom("Hello 1234 567")).isEqualTo("Hello 12 7");
//	}
//
//	@Test
//	public void shouldRemoveAllExceptDigitsBetween3and6() throws Exception {
//		assertThat(CharMatcher.inRange('3', '6').negate().removeFrom("Hello 1234 567")).isEqualTo("3456");
//	}
//
//	@Test
//	public void shouldRemoveStartingAndEndingDollarsAndKeepOtherUnchanged() throws Exception {
//		assertThat(CharMatcher.is('$').trimFrom("$$$ This is a $ sign $$$")).isEqualTo(" This is a $ sign ");
//	}
//
//	@Test
//	public void shouldRemoveOnlyStartingDollarsAndKeepOtherUnchanged() throws Exception {
//		assertThat(CharMatcher.is('$').trimLeadingFrom("$$$ This is a $ sign $$$")).isEqualTo(" This is a $ sign $$$");
//	}
//
//	@Test
//	public void shouldRemoveStartingEndEndingDollarsAndReplaceOthersWithX() throws Exception {
//		assertThat(CharMatcher.is('$').trimAndCollapseFrom("$$$This is a $ sign$$$", 'X'))
//				.isEqualTo("This is a X sign");
//	}
//
//	@Test
//	public void shouldRemoveOnlyEndingDollarsAndKeepOtherUnchanged() throws Exception {
//		assertThat(CharMatcher.is('$').trimTrailingFrom("$$$ This is a $ sign $$$")).isEqualTo("$$$ This is a $ sign ");
//	}
//
//	@Test
//	public void shouldRemoveStartingAndEndingDollarsOrWhitespaceAndKeepOtherUnchanged() throws Exception {
//		assertThat(CharMatcher.is('$').or(CharMatcher.is(' ')).trimFrom("$$$ This is a $ sign $$$")).isEqualTo(
//				"This is a $ sign");
//	}

}
