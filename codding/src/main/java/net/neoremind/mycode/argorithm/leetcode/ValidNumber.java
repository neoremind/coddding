package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Validate if a given string is numeric.
 * <p>
 * Some examples:
 * "0" => true
 * " 0.1 " => true
 * "abc" => false
 * "1 a" => false
 * "2e10" => true
 * Note: It is intended for the problem statement to be ambiguous. You should gather all requirements up front before
 * implementing one.
 *
 * @author xu.zhang
 */
public class ValidNumber {

    public boolean isNumber(String s) {
        s = s.trim();

        boolean pointSeen = false;
        boolean eSeen = false;
        boolean numberSeen = false;
        boolean numberAfterE = true;
        for (int i = 0; i < s.length(); i++) {
            if ('0' <= s.charAt(i) && s.charAt(i) <= '9') {
                numberSeen = true;
                numberAfterE = true;
            } else if (s.charAt(i) == '.') {
                if (eSeen || pointSeen) {
                    return false;
                }
                pointSeen = true;
            } else if (s.charAt(i) == 'e') {
                if (eSeen || !numberSeen) {
                    return false;
                }
                numberAfterE = false;
                eSeen = true;
            } else if (s.charAt(i) == '-' || s.charAt(i) == '+') {
                if (i != 0 && s.charAt(i - 1) != 'e') {
                    return false;
                }
            } else {
                return false;
            }
        }

        return numberSeen && numberAfterE;
    }

    @Test
    public void test() {
        assertThat(isNumber("0"), Matchers.is(true));
        assertThat(isNumber("0.1"), Matchers.is(true));
        assertThat(isNumber("abc"), Matchers.is(false));
        assertThat(isNumber("1 a"), Matchers.is(false));
        assertThat(isNumber("2e10"), Matchers.is(true));
        assertThat(isNumber("2e.10"), Matchers.is(false));
        assertThat(isNumber("2.1.10"), Matchers.is(false));
        assertThat(isNumber("e10"), Matchers.is(false));
        assertThat(isNumber("2e1e10"), Matchers.is(false));
        assertThat(isNumber("2e"), Matchers.is(false));
    }

}
