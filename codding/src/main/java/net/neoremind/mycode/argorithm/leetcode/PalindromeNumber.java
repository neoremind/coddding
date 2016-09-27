package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Determine whether an integer is a palindrome. Do this without extra space.
 * <p/>
 * What is  <a href="https://en.wikipedia.org/wiki/Palindrome">palindrome</a>?
 * <p/>
 * <pre>
 * A palindrome is a word, phrase, number, or other sequence of characters which reads the same backward or forward.
 * Allowances may be made for adjustments to capital letters, punctuation, and word dividers. Famous examples include
 * "A man, a plan, a canal, Panama!", "Amor, Roma", "race car"
 * </pre>
 * <p/>
 * Problems related with numbers are frequently solved by / and %. No need of extra space is required. This problem
 * is similar with the Reverse Integer problem.
 * <p/>
 * Note: no extra space here means do not convert the integer to string, since string will be a copy of the integer
 * and take extra space. The space take by div, left, and right can be ignored.
 *
 * @author zhangxu
 */
public class PalindromeNumber {

    public static boolean isPalindrome(int x) {
        //negative numbers are not palindrome
        if (x < 0) {
            return false;
        }

        // initialize how many zeros
        int div = 1;
        while (x / div >= 10) {
            div *= 10;
        }

        while (x != 0) {
            int left = x / div;
            int right = x % 10;

            if (left != right) {
                return false;
            }

            x = (x % div) / 10;
            div /= 100;
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome(12321));
        assertThat(isPalindrome(12321), is(true));

        System.out.println(isPalindrome(1));
        assertThat(isPalindrome(1), is(true));

        System.out.println(isPalindrome(0));
        assertThat(isPalindrome(0), is(true));

        System.out.println(isPalindrome(123));
        assertThat(isPalindrome(123), is(false));

        System.out.println(isPalindrome(-101));
        assertThat(isPalindrome(-101), is(false));

        System.out.println(isPalindrome(1000021));
        assertThat(isPalindrome(1000021), is(false));
    }

}
