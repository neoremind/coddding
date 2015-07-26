package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is
 * valid.
 * <p/>
 * The brackets must close in the correct order, "()" and "()[]{}" are all valid but "(]" and "([)]" are not.
 */
public class ValidParentheses {

    public static boolean isValid(String s) {
        char[] brackets = new char[] {'[', ']'};
        char[] braces = new char[] {'{', '}'};
        char[] parentheses = new char[] {'(', ')'};
        Set<Character> openSet = new HashSet<Character>(3);
        openSet.add('[');
        openSet.add('{');
        openSet.add('(');
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (openSet.contains(c)) {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                Character top = stack.pop();
                if (!(brackets[0] == top && c == brackets[1])
                        && !(braces[0] == top && c == braces[1])
                        && !(parentheses[0] == top && c == parentheses[1])) {
                    return false;
                }
            }
        }
        return stack.empty();
    }

    public static void main(String[] args) {
        String s = "{[]}()";
        boolean isValid = isValid(s);
        System.out.println(isValid);
        assertThat(isValid, is(true));

        s = "{}[]()";
        isValid = isValid(s);
        System.out.println(isValid);
        assertThat(isValid, is(true));

        s = "(]";
        isValid = isValid(s);
        System.out.println(isValid);
        assertThat(isValid, is(false));

        s = "[";
        isValid = isValid(s);
        System.out.println(isValid);
        assertThat(isValid, is(false));
    }

}
