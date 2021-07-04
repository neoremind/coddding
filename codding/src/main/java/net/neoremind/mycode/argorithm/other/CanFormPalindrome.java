package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CanFormPalindrome {

    private boolean checkPalin(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        if (s.length() == 1) {
            return true;
        }
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            map.put(chars[i], map.getOrDefault(chars[i], 0) + 1);
        }

        boolean found = false;
        for (Character key: map.keySet()) {
            int count = map.get(key);
            if (count % 2 == 0) {
                continue;
            } else {
                if (found) {
                    return false;
                }
                found = true;
            }
        }

        return true;
    }

    @Test
    public void test() {
        assertThat(checkPalin("a"), is(true));
        assertThat(checkPalin("aba"), is(true));
        assertThat(checkPalin("abba"), is(true));
        assertThat(checkPalin("acb"), is(false));
        assertThat(checkPalin("aabc"), is(false));
        assertThat(checkPalin("aab"), is(true));
    }
}
