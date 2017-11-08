package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integer to English Words
 * <p>
 * Convert a non-negative integer to its english words representation. Given input is guaranteed to be less than 231
 * - 1.
 * <p>
 * For example,
 * 123 -> "One Hundred Twenty Three"
 * 12345 -> "Twelve Thousand Three Hundred Forty Five"
 * 1234567 -> "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 *
 * @author xu.zhang
 */
public class IntegerToEnglishWords {

    String[] less20 = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen"};
    String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    String[] thousands = {"", "Thousand", "Million", "Billion"};


    public String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        int i = 0;
        String str = "";
        while (num > 0) {
            if (num % 1000 != 0) {
                str = helper(num % 1000) + thousands[i] + " " + str;
            }
            num /= 1000;
            i++;
        }
        return str.trim();
    }

    public String helper(int num) {
        if (num == 0) {
            return "";
        } else if (num < 20) {
            return less20[num % 20] + " ";
        } else if (num >= 20 && num < 100) {
            return tens[num / 10] + " " + helper(num % 10);
        } else if (num >= 100 && num < 1000) {
            return less20[num / 100] + " Hundred " + helper(num % 100);
        }
        throw new RuntimeException("should not go here");
    }

    @Test
    public void test() {
        assertThat(numberToWords(0), is("Zero"));
        assertThat(numberToWords(9), is("Nine"));
        assertThat(numberToWords(12), is("Twelve"));
        assertThat(numberToWords(20), is("Twenty"));
        assertThat(numberToWords(24), is("Twenty Four"));
        assertThat(numberToWords(100), is("One Hundred"));
        assertThat(numberToWords(1000), is("One Thousand"));
        assertThat(numberToWords(135), is("One Hundred Thirty Five"));
        assertThat(numberToWords(2935), is("Two Thousand Nine Hundred Thirty Five"));
        assertThat(numberToWords(12935), is("Twelve Thousand Nine Hundred Thirty Five"));
        assertThat(numberToWords(152935), is("One Hundred Fifty Two Thousand Nine Hundred Thirty Five"));
        assertThat(numberToWords(1052935), is("One Million Fifty Two Thousand Nine Hundred Thirty Five"));
    }

}
