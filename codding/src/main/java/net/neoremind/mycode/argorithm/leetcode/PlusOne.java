package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given a non-negative number represented as an array of digits, plus one to the number.
 * <p>
 * The digits are stored such that the most significant digit is at the head of the list.
 * <p>
 * Subscribe to see which companies asked this question
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/plus-one/
 */
public class PlusOne {

    public int[] plusOne(int[] digits) {
        if (digits == null || digits.length == 0) {
            return digits;
        }
        int i = digits.length - 1;
        while (i >= 0) {
            if (digits[i] == 9) {
                digits[i] = 0;
            } else {
                digits[i] = digits[i] + 1;
                break;
            }
            i--;
        }
        if (digits[0] == 0) {
            int[] ret = new int[digits.length + 1];
            ret[0] = 1;
            return ret;
        }
        return digits;
    }
}
