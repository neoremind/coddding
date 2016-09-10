package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given a column title as appear in an Excel sheet, return its corresponding column number.
 * <p>
 * For example:
 * <p>
 * A -> 1
 * B -> 2
 * C -> 3
 * ...
 * Z -> 26
 * AA -> 27
 * AB -> 28
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/excel-sheet-column-number/
 */
public class ExcelSheetColumnNumber {

    public int titleToNumber(String s) {
        int len = s.length();
        int res = 0;
        for (int i = 0; i < len; i++) {
            res *= 26;
            res += (s.charAt(i) - '@');
        }
        return res;
    }

}
