package net.neoremind.mycode.argorithm.leetcode;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

/**
 * Given a string that contains only digits 0-9 and a target value, return all possibilities to add binary operators
 * (not unary) +, -, or * between the digits so they evaluate to the target value.
 * <p>
 * Examples:
 * "123", 6 -> ["1+2+3", "1*2*3"]
 * "232", 8 -> ["2*3+2", "2+3*2"]
 * "105", 5 -> ["1*0+5","10-5"]
 * "00", 0 -> ["0+0", "0-0", "0*0"]
 * "3456237490", 9191 -> []
 *
 * @author xu.zhang
 */
public class ExpressionAddOperators {

    void dfs(List<String> ret, char[] path, int len, long left, long cur, char[] digits, int pos, int target) {
        if (pos == digits.length) {
            if (left + cur == target) ret.add(new String(path, 0, len));
            return;
        }
        long n = 0;
        int j = len + 1;
        for (int i = pos; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';
            path[j++] = digits[i];
            path[len] = '+';
            dfs(ret, path, j, left + cur, n, digits, i + 1, target);
            path[len] = '-';
            dfs(ret, path, j, left + cur, -n, digits, i + 1, target);
            path[len] = '*';
            dfs(ret, path, j, left, cur * n, digits, i + 1, target);
            if (digits[pos] == '0') break;
        }
    }

    public List<String> addOperators(String num, int target) {
        List<String> ret = new LinkedList<>();
        if (num.length() == 0) return ret;
        char[] path = new char[num.length() * 2 - 1];
        char[] digits = num.toCharArray();
        long n = 0;
        for (int i = 0; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';
            path[i] = digits[i];
            dfs(ret, path, i + 1, 0, n, digits, i + 1, target);
            if (n == 0) break;
        }
        return ret;
    }

    void dfs2(List<String> ret, char[] path, int len, long left, long cur, char[] digits, int pos, int target) {
        if (pos == digits.length) {
            if (left + cur == target) ret.add(new String(path, 0, len));
            return;
        }
        long n = 0;
        int j = len + 1;
        for (int i = pos; i < digits.length; i++) {
            n = n * 10 + digits[i] - '0';
            path[j++] = digits[i];
            path[len] = '+';
            dfs(ret, path, j, left + cur, n, digits, i + 1, target);
            path[len] = '-';
            dfs(ret, path, j, left + cur, -n, digits, i + 1, target);
            path[len] = '*';
            dfs(ret, path, j, left, cur * n, digits, i + 1, target);
            if (digits[pos] == '0') break;
        }
    }

    public static List<String> addOperators2(String num, int target) {
        List<String> res = new ArrayList<>();
        long n = 0;
        for (int i = 0; i < num.length(); i++) {
            n = n * 10 + num.charAt(i) - '0';
            List<String> temp = new ArrayList<>();
            temp.add(String.valueOf(n));
            helper(num, res, temp, i + 1, n, n, target);
            removeLastN(temp, 1);
            if (n == 0) {
                break; //stop because number does not start with 0
            }
        }
        return res;
    }

    public static void helper(String num, List<String> res, List<String> temp, int index, long lastNum, long acc, int
            target) {
        //System.out.println(num.substring(index) + " " + temp + ",lastNum=" + lastNum + ", acc=" + acc + ", index="
        // + index);
        if (index == num.length()) {
            if (acc == target) {
                res.add(temp.stream().collect(Collectors.joining("")));
            }
        } else {
            int n = 0;
            for (int i = index; i < num.length(); i++) {
                n = n * 10 + num.charAt(i) - '0';
                temp.add("+");
                temp.add(String.valueOf(n));
                helper(num, res, temp, i + 1, n, acc + n, target);
                removeLastN(temp, 2);

                temp.add("-");
                temp.add(String.valueOf(n));
                helper(num, res, temp, i + 1, -n, acc - n, target);
                removeLastN(temp, 2);

                temp.add("*");
                temp.add(String.valueOf(n));
                helper(num, res, temp, i + 1, lastNum * n, acc - lastNum + (lastNum * n), target);
                removeLastN(temp, 2);

                if (num.charAt(i) == '0') {
                    break; //stop because number does not start with 0
                }
            }
        }
    }

    public static void removeLastN(List<String> list, int n) {
        for (int i = 0; i < n; i++) {
            list.remove(list.size() - 1);
        }
    }

    @Test
    public void test() {
        assertThat(addOperators2("123", 6), Matchers.is(Lists.newArrayList("1+2+3", "1*2*3")));
        assertThat(addOperators2("232", 8), Matchers.is(Lists.newArrayList("2+3*2", "2*3+2")));
        assertThat(addOperators2("105", 5), Matchers.is(Lists.newArrayList("1*0+5", "10-5")));
        assertThat(addOperators2("00", 0), Matchers.is(Lists.newArrayList("0+0", "0-0", "0*0")));
        assertThat(addOperators2("3456237490", 9191), Matchers.is(Lists.newArrayList()));
        assertThat(addOperators2("345", -17), Matchers.is(Lists.newArrayList("3-4*5")));
        System.out.println(addOperators2("10009", 9));
        assertThat(addOperators2("2147483648", -2147483648), Matchers.is(Lists.newArrayList()));
    }

}
