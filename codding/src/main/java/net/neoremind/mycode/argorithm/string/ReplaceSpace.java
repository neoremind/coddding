package net.neoremind.mycode.argorithm.string;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class ReplaceSpace {

    public String replaceSpace(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() == 0) {
            return "";
        }
        char[] str = input.toCharArray();
        int spaceCount = 0;
        for (char c : str) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        int len = str.length + spaceCount * 2;
        char[] res = new char[len];
        int idx = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                res[idx++] = '%';
                res[idx++] = '2';
                res[idx++] = '0';
            } else {
                res[idx++] = str[i];
            }
        }
        return new String(res);
    }

    @Test
    public void test() {
        assertThat(replaceSpace(""), Matchers.is(""));
        assertThat(replaceSpace(null), Matchers.nullValue());
        assertThat(replaceSpace("abc"), Matchers.is("abc"));
        assertThat(replaceSpace("abc bcd"), Matchers.is("abc%20bcd"));
        assertThat(replaceSpace("abc bcd "), Matchers.is("abc%20bcd%20"));
        assertThat(replaceSpace("abc  bcd "), Matchers.is("abc%20%20bcd%20"));
        assertThat(replaceSpace(" abc"), Matchers.is("%20abc"));
        assertThat(replaceSpace(" abc "), Matchers.is("%20abc%20"));
    }

}
