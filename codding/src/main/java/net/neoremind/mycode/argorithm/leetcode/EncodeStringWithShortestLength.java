package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Given a non-empty string, encode the string such that its encoded length is the shortest.
 * <p>
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated
 * exactly k times.
 * <p>
 * Note:
 * k will be a positive integer and encoded string will not be empty or have extra space.
 * You may assume that the input string contains only lowercase English letters. The string's length is at most 160.
 * If an encoding process does not make the string shorter, then do not encode it. If there are several solutions,
 * return any of them is fine.
 * Example 1:
 * <p>
 * Input: "aaa"
 * Output: "aaa"
 * Explanation: There is no way to encode it such that it is shorter than the input string, so we do not encode it.
 * Example 2:
 * <p>
 * Input: "aaaaa"
 * Output: "5[a]"
 * Explanation: "5[a]" is shorter than "aaaaa" by 1 character.
 * Example 3:
 * <p>
 * Input: "aaaaaaaaaa"
 * Output: "10[a]"
 * Explanation: "a9[a]" or "9[a]a" are also valid solutions, both of them have the same length = 5, which is the same
 * as "10[a]".
 * Example 4:
 * <p>
 * Input: "aabcaabcd"
 * Output: "2[aabc]d"
 * Explanation: "aabc" occurs twice, so one answer can be "2[aabc]d".
 * Example 5:
 * <p>
 * Input: "abbbabbbcabbbabbbc"
 * Output: "2[2[abbb]c]"
 * Explanation: "abbbabbbc" occurs twice, but "abbbabbbc" can also be encoded to "2[abbb]c", so one answer can be
 * "2[2[abbb]c]".
 *
 * @author xu.zhang
 *
 * @see {@link DecodeString}
 */
//TODO
public class EncodeStringWithShortestLength {

    public String encode(String s) {
        return null;
    }

    @Test
    public void test() {
        assertThat(encode("aaa"), Matchers.is("aaa"));
        assertThat(encode("aaaaa"), Matchers.is("5[a]"));
        assertThat(encode("aaaaaaaaaa"), Matchers.is("10[a]"));
        assertThat(encode("aabcaabcd"), Matchers.is("2[aabc]d"));
        assertThat(encode("abbbabbbcabbbabbbc"), Matchers.is("2[2[abbb]c]"));
    }

}
