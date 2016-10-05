package net.neoremind.mycode.argorithm.leetcode;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Given a positive integer, return its corresponding column title as appear in an Excel sheet.
 * <p>
 * For example:
 * <p>
 * 1 -> A
 * 2 -> B
 * 3 -> C
 * ...
 * 26 -> Z
 * 27 -> AA
 * 28 -> AB
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/excel-sheet-column-title/
 */
public class ExcelSheetColumnTitle {

    public String convertToTitle(int n) {
        // if (n < 1) {
        //     return "";
        // }
        // StringBuilder sb = new StringBuilder();
        // if (n < 27) {
        //     sb.append((char)(((n - 1) % 26) + 'A'));
        //     n = 0;
        // }
        // while (n != 0) {
        //     n--;
        //     sb.append((char)((n % 26) + 'A'));
        //     n = n / 26;
        // }
        // return sb.reverse().toString();
        StringBuilder result = new StringBuilder();

        while (n > 0) {
            n--;
            result.insert(0, (char) ('A' + n % 26));
            n /= 26;
        }

        return result.toString();
    }

    @Test
    public void test() {
        assertThat(convertToTitle(1), Matchers.is("A"));
        assertThat(convertToTitle(26), Matchers.is("Z"));
        assertThat(convertToTitle(52), Matchers.is("AZ"));
        assertThat(convertToTitle(53), Matchers.is("BA"));
        assertThat(convertToTitle(54), Matchers.is("BB"));
    }

}
