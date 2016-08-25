package net.neoremind.mycode.argorithm.string;

import static org.junit.Assert.assertThat;

import java.util.Stack;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class ReverseString {

    public String reverse(String s) {
        if (s == null) {
            return null;
        }
        char[] c = s.toCharArray();
        int length = s.length();
        int mid = length >> 1;
        for (int i = 0; i < mid; i++) {
            char temp = c[i];
            c[i] = c[length - i - 1];
            c[length - i - 1] = temp;
        }
        return new String(c);
    }

    public String reverse2(String s) {
        char[] str = s.toCharArray();
        int begin = 0;
        int end = s.length() - 1;
        while (begin < end) {
            str[begin] = (char) (str[begin] ^ str[end]);
            str[end] = (char) (str[begin] ^ str[end]);
            str[begin] = (char) (str[end] ^ str[begin]);
            begin++;
            end--;
        }
        return new String(str);
    }

    public String reverse3(String s) {
        char[] str = s.toCharArray();
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < str.length; i++) {
            stack.push(str[i]);
        }

        String reversed = "";
        for (int i = 0; i < str.length; i++) {
            reversed += stack.pop();
        }

        return reversed;
    }

    public String reverse4(String s) {
        int length = s.length();
        String reverse = "";
        for (int i = 0; i < length; i++) {
            reverse = s.charAt(i) + reverse;
        }
        return reverse;
    }

    public String reverse5(String s) {
        int length = s.length();
        if (length <= 1) {
            return s;
        }
        String left = s.substring(0, length / 2);
        String right = s.substring(length / 2, length);
        return reverse5(right) + reverse5(left);
    }

    @Test
    public void test() {
        assertThat(reverse(""), Matchers.is(""));
        assertThat(reverse("a"), Matchers.is("a"));
        assertThat(reverse("abc"), Matchers.is("cba"));
        assertThat(reverse("abcd"), Matchers.is("dcba"));
        assertThat(reverse("abcde"), Matchers.is("edcba"));
        assertThat(reverse2("abcde"), Matchers.is("edcba"));
        assertThat(reverse3("abcde"), Matchers.is("edcba"));
        assertThat(reverse4("abcde"), Matchers.is("edcba"));
        assertThat(reverse5("abcde"), Matchers.is("edcba"));
    }
}
