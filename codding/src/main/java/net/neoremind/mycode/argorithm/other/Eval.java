package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

/**
 * <p>
 * Check if a string expression is correct or not (no parentheses, no division, no overflow).
 * For example: “1 + 22 * 3 = 23 * 2 + 41 - 1”
 * <p>
 * 1+22*3
 * left = 1+22*3
 * right = 23*2+41-1
 * String eval(String s) {
 * if (s == null || s.length() == 0) {
 * return “”;
 * }
 * s = s.trim();
 * int equalsIndex = -1;
 * for (int i = 0; i < s.length(); i++) {
 * if (s.charAt(i) == ‘=’) {
 * equalsIndex = i;
 * }
 * String leftS = s.substring(0, i).trim();
 * String rightS = s.substring(i + 1).trim();
 * int left = -1;
 * if (isNumber(leftS)) {
 * left = Integer.parseInt(leftS);
 * } else {
 * left = calc(leftS);
 * }
 * int right = -1;
 * if (isNumber(rightS)) {
 * right = Integer.parseInt(rightS);
 * } else {
 * right = calc(rightS);
 * }
 * return  left == right;
 * }
 * }
 * <p>
 * 1+22*3*44
 * 23*3*44
 * 1+22*3
 * 67, 66
 * <p>
 * 66 = 55
 * <p>
 * int calc(String s) {
 * if (s == null || s.length() == 0) {
 * return 0;
 * }
 * int n = 0;
 * int i = 0;
 * char[] c = s.toCharArray();
 * while (c[i] >= ‘0’ && c[i] <= ‘9’)
 * n = n * 10 + (c[i] - ‘0’);
 * i++;
 * }
 * return dfs(c, i, s.length(), n, 0);
 * }
 * <p>
 * int dfs(char[] c, int index, int originalLength, int lastNum, int acc) {
 * if (index ==  originalLength) {
 * return acc;
 * }
 * int i = index;
 * while (i < originalLength && c[i] == ‘ ’) {
 * i++;
 * }
 * char operator = c[i];
 * i++;
 * while (i < originalLength && c[i] == ‘ ’) {
 * i++;
 * }
 * int n = 0;
 * while (c[i] >= ‘0’ && c[i] <= ‘9’) {
 * n = n * 10 + (c[i] - ‘0’);
 * i++;
 * }
 * if (operator == ‘+’) {
 * acc = acc + n;
 * lastNum = n;
 * } else if (operator == ‘-’) {
 * acc = acc - n;
 * lastNum = n;
 * } else if (operator == ‘*’) {
 * acc = acc - lastNum;
 * acc += lastNum * n;
 * lastNum = lastNum * n;
 * } else {
 * throw new RuntimeException(“impossible”);
 * }
 * return dfs(c, i, originalLength, lastNum, acc);
 * }
 * <p>
 * // *3, 4, 22, 23
 *
 * @author xu.zhang
 */
public class Eval {

    boolean eval(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        s = s.trim();
        int equalsIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '=') {
                equalsIndex = i;
            }
        }
        String leftS = s.substring(0, equalsIndex).trim();
        String rightS = s.substring(equalsIndex + 1).trim();
        return calc(leftS) == calc(rightS);
    }


    int calc(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int n = 0;
        int i = 0;
        char[] c = s.toCharArray();
        while (i < s.length() && c[i] >= '0' && c[i] <= '9') {
            n = n * 10 + (c[i] - '0');
            i++;
        }
        return dfs(c, i, s.length(), n, n);
    }

    int dfs(char[] c, int index, int originalLength, int lastNum, int acc) {
        if (index == originalLength) {
            return acc;
        }
        int i = index;
        while (i < originalLength && c[i] == ' ') {
            i++;
        }
        char operator = c[i];
        i++;
        while (i < originalLength && c[i] == ' ') {
            i++;
        }
        int n = 0;
        while (i < originalLength && c[i] >= '0' && c[i] <= '9') {
            n = n * 10 + (c[i] - '0');
            i++;
        }
        if (operator == '+') {
            acc = acc + n;
            lastNum = n;
        } else if (operator == '-') {
            acc = acc - n;
            lastNum = -n;
        } else if (operator == '*') {
            acc = acc - lastNum;
            acc += lastNum * n;
            lastNum = lastNum * n;
        } else {
            throw new RuntimeException("impossible");
        }
        return dfs(c, i, originalLength, lastNum, acc);
    }

    @Test
    public void test() {
        System.out.println(eval("1+2=2+1"));
        System.out.println(eval("2+2*3=4+4"));
        System.out.println(eval("2+2*3=4+5"));
        System.out.println(eval("1+2*4-5=4"));
        System.out.println(eval("1+2*4-5=6"));
    }

}
