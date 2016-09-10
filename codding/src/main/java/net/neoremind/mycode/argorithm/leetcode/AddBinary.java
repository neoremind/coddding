package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given two binary strings, return their sum (also a binary string).
 * <p>
 * For example,
 * a = "11"
 * b = "1"
 * Return "100".
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/add-binary/
 */
public class AddBinary {

    public String addBinary(String a, String b) {
        if (a == null || a.length() == 0) {
            return b;
        }
        if (b == null || b.length() == 0) {
            return a;
        }
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 && j >= 0) {
            int value = (a.charAt(i--) - '0') + (b.charAt(j--) - '0') + carry;
            sb.append(value % 2);
            carry = value / 2;
        }

        while (j >= 0) {
            int value = (b.charAt(j--) - '0') + carry;
            sb.append(value % 2);
            carry = value / 2;
        }

        while (i >= 0) {
            int value = (a.charAt(i--) - '0') + carry;
            sb.append(value % 2);
            carry = value / 2;
        }

        if (carry != 0) {
            sb.append(carry);
        }
        return sb.reverse().toString();
    }

}
