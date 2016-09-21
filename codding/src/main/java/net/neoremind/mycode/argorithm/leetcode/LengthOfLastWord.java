package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a string s consists of upper/lower-case alphabets and empty space characters ' ', return the length of last
 * word in the string.
 * <p>
 * If the last word does not exist, return 0.
 * <p>
 * Note: A word is defined as a character sequence consists of non-space characters only.
 * <p>
 * For example,
 * Given s = "Hello World",
 * return 5.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/length-of-last-word/
 */
public class LengthOfLastWord {

    /**
     * 参考JDK的trim写的很经典.
     *
     * @param s
     *
     * @return
     */
    public int lengthOfLastWord(String s) {
        s = s.trim();
        return s.substring(s.lastIndexOf(" ") + 1, s.length()).length();
    }

    @Test
    public void test() {
        assertThat(lengthOfLastWord(""), Matchers.is(0));
        assertThat(lengthOfLastWord("ggg"), Matchers.is(3));
        assertThat(lengthOfLastWord("aaaa "), Matchers.is(4));
        assertThat(lengthOfLastWord("sdf df 3434df"), Matchers.is(6));
    }
}
