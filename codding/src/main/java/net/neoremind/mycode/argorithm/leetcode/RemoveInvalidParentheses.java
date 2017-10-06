package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Remove the minimum number of invalid parentheses in order to make the input string valid. Return all possible
 * results.
 * <p>
 * Note: The input string may contain letters other than the parentheses ( and ).
 * <p>
 * Examples:
 * "()())()" -> ["()()()", "(())()"]
 * "(a)())()" -> ["(a)()()", "(a())()"]
 * ")(" -> [""]
 *
 * @author xu.zhang
 */
public class RemoveInvalidParentheses {

    public List<String> removeInvalidParentheses(String s) {
        List<String> res = new ArrayList<>();
        if (s == null) return res;
        Queue<String> q = new LinkedList<>();
        q.offer(s);
        Set<String> visited = new HashSet<>();
        // found这个标志位很重要，用于保证题目中删除"最少的字符"的要求，
        // 否则就会产生很多的解法，包括(())(), ()()(), (()), ()(), ()
        // 只需要找到了之后把剩下的再队列中的元素检查完即可。
        boolean found = false;
        while (!q.isEmpty()) {
            String str = q.poll();
            System.out.println(str);
            if (visited.contains(str)) {
                continue;
            }
            visited.add(str);
            if (isValid(str)) {
                System.out.println("OK");
                res.add(str);
                found = true;
            }
            if (found) {
                continue;
            }
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '(' && str.charAt(i) != ')') continue;
                String temp = str.substring(0, i) + str.substring(i + 1);
                q.offer(temp);
            }
        }
        return res;
    }

    /**
     * 复用valid parentheses题目的解法，加入判断非([{等符号的字符continue判断
     */
    public boolean isValid(String s) {
        if (s == null || s.length() == 0) return true;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '(' && c != '[' && c != '{' &&
                    c != ')' && c != ']' && c != '}') {
                continue;
            }
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if (!(top == '(' && c == ')') &&
                        !(top == '[' && c == ']') &&
                        !(top == '{' && c == '}')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // helper function checks if string s contains valid parentheses
    boolean isValid2(String s) {
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') count++;
            if (c == ')' && count-- == 0) return false;
        }

        return count == 0;
    }

    @Test
    public void test() {
        assertThat(removeInvalidParentheses("()())()"), is(Lists.newArrayList("(())()", "()()()")));
        System.out.println("======");
        assertThat(removeInvalidParentheses(")(f"), is(Lists.newArrayList("f")));
        assertThat(removeInvalidParentheses(""), is(Lists.newArrayList("")));
    }

}
