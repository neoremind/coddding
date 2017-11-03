package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given two non-negative integers num1 and num2 represented as string, return the sum of num1 and num2.
 * <p>
 * Note:
 * <p>
 * The length of both num1 and num2 is < 5100.
 * Both num1 and num2 contains only digits 0-9.
 * Both num1 and num2 does not contain any leading zero.
 * You must not use any built-in BigInteger library or convert the inputs to integer directly.
 *
 * @author xu.zhang
 */
public class AddStrings {

    // 12
    // 34
    //---
    // 46
    //
    // 56
    // 78
    //---
    // 14
    //12
    //---
    //134
    public String addStrings(String num1, String num2) {
        if (num1 == null || num1.length() == 0) return num2;
        if (num2 == null || num2.length() == 0) return num1;
        StringBuilder sb = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;
        while (i >= 0 && j >= 0) {
            //TODO check k1 and k2 valid
            int k1 = num1.charAt(i) - '0';
            int k2 = num2.charAt(j) - '0';
            int value = carry + k1 + k2;
            sb.append(value % 10);
            carry = value / 10;
            i--;
            j--;
        }

        while (i >= 0) {
            int k1 = num1.charAt(i) - '0';
            int value = carry + k1;
            sb.append(value % 10);
            carry = value / 10;
            i--;
        }

        while (j >= 0) {
            int k2 = num2.charAt(j) - '0';
            int value = carry + k2;
            sb.append(value % 10);
            carry = value / 10;
            j--;
        }

        if (carry > 0) {
            sb.append(carry);
        }
        return sb.reverse().toString();
    }

}
