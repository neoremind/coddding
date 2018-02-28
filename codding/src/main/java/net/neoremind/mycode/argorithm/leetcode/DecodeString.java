package net.neoremind.mycode.argorithm.leetcode;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertThat;

/**
 * Given an encoded string, return it's decoded string.
 * <p>
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated
 * exactly k times. Note that k is guaranteed to be a positive integer.
 * <p>
 * You may assume that the input string is always valid; No extra white spaces, square brackets are well-formed, etc.
 * <p>
 * Furthermore, you may assume that the original data does not contain any digits and that digits are only for those
 * repeat numbers, k. For example, there won't be input like 3a or 2[4].
 * <p>
 * Examples:
 * <p>
 * s = "3[a]2[bc]", return "aaabcbc".
 * s = "3[a2[c]]", return "accaccacc".
 * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
 * <p>
 * 20min AC的。
 *
 * @author xu.zhang
 */
public class DecodeString {

    public String decodeString(String s) {
        if (s == null || s.length() == 0) return "";
        Stack<String> stack = new Stack<>();
        char[] chars = s.toCharArray();
        //  System.out.println(chars.length);
        int i = 0;
        while (i < chars.length) {
            // System.out.println(i);
            char c = chars[i];
            if (Character.isDigit(c)) {
                StringBuilder digit = new StringBuilder();
                while (Character.isDigit(chars[i])) {
                    digit.append(chars[i]);
                    i++;
                }
                //  System.out.println("push number " + digit.toString());
                stack.push(digit.toString());
            } else if (c == '[') {
                //    System.out.println("push [");
                stack.push(String.valueOf(c));
                i++;
            } else if (c == ']') {
                String str = stack.pop();
                stack.pop();
                String number = stack.pop();
                StringBuilder repeatedStr = new StringBuilder();
                for (int j = 0; j < Integer.valueOf(number); j++) {
                    repeatedStr.append(str);
                }
                while (!stack.isEmpty() && !stack.peek().equals("[")) {
                    repeatedStr.insert(0, stack.pop());
                }
                // System.out.println("push string, " + repeatedStr.toString());
                stack.push(repeatedStr.toString());
                i++;
            } else if (c >= 'a' && c <= 'z') {
                StringBuilder alpha = new StringBuilder();
                while (i < chars.length && chars[i] >= 'a' && chars[i] <= 'z') {
                    alpha.append(chars[i]);
                    i++;
                }
                while (!stack.isEmpty() && !stack.peek().equals("[")) {
                    alpha.insert(0, stack.pop());
                }
                //  System.out.println("push new string " + alpha.toString() + ", i=" + i);
                stack.push(alpha.toString());
            }
        }
        //是否可以去掉？
//        StringBuilder res = new StringBuilder();
//        while (!stack.isEmpty()) {
//            res.insert(0, stack.pop());
//        }
//        return res.toString();
        return stack.pop();
    }

    @Test
    public void test() {
        assertThat(decodeString("3[a]2[bc]"), Matchers.is("aaabcbc"));
        assertThat(decodeString("3[a2[c]]"), Matchers.is("accaccacc"));
        assertThat(decodeString("2[abc]3[cd]ef"), Matchers.is("abcabccdcdcdef"));
        assertThat(decodeString("3[a]2[b4[f]c]"), Matchers.is("aaabffffcbffffc"));
    }
}
