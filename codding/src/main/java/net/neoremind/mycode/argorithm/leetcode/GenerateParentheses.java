package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 * <p>
 * For example, given n = 3, a solution set is:
 * <p>
 * [
 * "((()))",
 * "(()())",
 * "(())()",
 * "()(())",
 * "()()()"
 * ]
 * <p>
 * 以n=3为例，生成结果如下：
 * ((()))
 * (()())
 * (())()
 * ()(())
 * ()()()
 * <p>
 * 1）基本思想就是不断的添加左括号 (，直到添加不了了，然后补右括号。
 * 2）推一栈帧，补齐一个右括号)，然后剩下的继续如栈，重复1的操作。
 * <p>
 * 这个步骤比较抽象，较难理解:-(
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/generate-parentheses/
 */
public class GenerateParentheses {

    public List<String> generateParenthesis(int n) {
        List<String> list = new ArrayList<>();
        backtrack(list, "", 0, 0, n);
        return list;
    }

    public void backtrack(List<String> list, String str, int open, int close, int max) {
        System.out.println(String.format("backtrack{%s, %d, %d}", str, open, close));
        if (str.length() == max * 2) {
            list.add(str);
            return;
        }

        if (open < max) {
            backtrack(list, str + "(", open + 1, close, max);
        }
        if (close < open) {
            backtrack(list, str + ")", open, close + 1, max);
        }
    }

    @Test
    public void test() {
        List<String> result = generateParenthesis(1);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(1));

        result = generateParenthesis(2);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(2));

        result = generateParenthesis(3);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(5));

        result = generateParenthesis(4);
        result.stream().forEach(System.out::println);
        assertThat(result.size(), Matchers.is(14));
    }

}
