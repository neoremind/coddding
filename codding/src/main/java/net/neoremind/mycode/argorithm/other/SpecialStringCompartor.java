package net.neoremind.mycode.argorithm.other;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * 奇葩字符串比较，fb题
 *
 * @author xu.zhang
 */
public class SpecialStringCompartor {

    public int compareTwoStrings(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null && b != null) return -1;
        if (a != null && b == null) return 1;
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) {
            char ca = a.charAt(i);
            char cb = b.charAt(j);
            if (Character.isLetter(ca) && Character.isLetter(cb)) {
                if (Character.compare(ca, cb) < 0) {
                    return -1;
                } else if (Character.compare(ca, cb) > 0) {
                    return 1;
                } else {
                    i++;
                    j++;
                }
            } else if (Character.isDigit(ca) && Character.isDigit(cb)) {
                StringBuilder digitA = new StringBuilder();
                while (i < a.length() && Character.isDigit(a.charAt(i))) {
                    digitA.append(a.charAt(i));
                    i++;
                }
                Integer numberA = Integer.parseInt(digitA.toString());
                StringBuilder digitB = new StringBuilder();
                while (j < b.length() && Character.isDigit(b.charAt(j))) {
                    digitB.append(b.charAt(j));
                    j++;
                }
                Integer numberB = Integer.parseInt(digitB.toString());
                if (numberA.compareTo(numberB) == 0) {
                    continue;
                } else {
                    return numberA.compareTo(numberB);
                }
            } else if (Character.isDigit(ca)) {
                return -1;
            } else {
                return 1;
            }
        }
        if (i == a.length() && j == b.length()) {
            return 0;
        }
        if (i == a.length()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Test
    public void test() {
        assertThat(compareTwoStrings("abc123", "abc12"), Matchers.is(1));
        assertThat(compareTwoStrings("abc123", "abc123"), Matchers.is(0));
        assertThat(compareTwoStrings("abc123", "abc0123"), Matchers.is(0));
        assertThat(compareTwoStrings("abc123", "abc1234"), Matchers.is(-1));
        assertThat(compareTwoStrings("abc9", "abc123"), Matchers.is(-1));
        assertThat(compareTwoStrings("abc9", "abc001"), Matchers.is(1));
        assertThat(compareTwoStrings("abc", "abc123"), Matchers.is(-1));
        assertThat(compareTwoStrings("abc", "ab1"), Matchers.is(1));
        assertThat(compareTwoStrings("abc123a", "abc123b"), Matchers.is(-1));
        assertThat(compareTwoStrings("abc123a", "abc8"), Matchers.is(1));
    }
}
