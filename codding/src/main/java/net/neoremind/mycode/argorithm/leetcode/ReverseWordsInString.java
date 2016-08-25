package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given an input string, reverse the string word by word.
 * <p>
 * For example,
 * Given s = "the sky is blue",
 * return "blue is sky the".
 * <p>
 * Update (2015-02-12):
 * For C programmers: Try to solve it in-place in O(1) space.
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/reverse-words-in-a-string/
 */
public class ReverseWordsInString {

    public String reverseWords(String s) {
        if (s == null) {
            return null;
        }
        // 1. trim leading, trailing spaces
        s = s.trim();

        // 2. empty
        if (s.length() == 0) {
            return "";
        }

        // 3. trim inner multiple spaces
        // barrier - 1 is the trimmed ending index
        // like RemoveDuplicatesFromSortedArray
        char[] str = s.toCharArray();
        int barrier = 1;
        for (int i = 1; i < str.length; i++) {
            if (str[i] == ' ' && str[i] == str[i - 1]) {
                continue;
            } else {
                str[barrier++] = str[i];
            }
        }

        // 4. reverse the whole string
        reverse(str, 0, barrier - 1);

        // 5. reverse each word
        int start = 0;
        for (int i = 0; i < barrier; i++) {
            if (str[i] == ' ') {
                reverse(str, start, i - 1);
                start = i + 1;
            }
        }

        // 6. reverse the last word
        reverse(str, start, barrier - 1);

        //7. return new String by invoking new String(char value[], int offset, int count)
        return new String(str, 0, barrier);
    }

    private void reverse(char[] str, int begin, int end) {
        while (begin < end) {
            str[begin] = (char) (str[begin] ^ str[end]);
            str[end] = (char) (str[begin] ^ str[end]);
            str[begin] = (char) (str[end] ^ str[begin]);
            begin++;
            end--;
        }
    }

    @Test
    public void test() {
        assertThat(reverseWords("the sky is blue"), Matchers.is("blue is sky the"));
        assertThat(reverseWords("the sky is  blue"), Matchers.is("blue is sky the"));
    }
}
