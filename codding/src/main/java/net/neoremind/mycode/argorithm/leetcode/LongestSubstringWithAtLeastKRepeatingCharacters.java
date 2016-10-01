package net.neoremind.mycode.argorithm.leetcode;

/**
 * Find the length of the longest substring T of a given string (consists of lowercase letters only) such that every
 * character in T appears no less than k times.
 * <p>
 * Example 1:
 * <p>
 * Input:
 * s = "aaabb", k = 3
 * <p>
 * Output:
 * 3
 * <p>
 * The longest substring is "aaa", as 'a' is repeated 3 times.
 * Example 2:
 * <p>
 * Input:
 * s = "ababbc", k = 2
 * <p>
 * Output:
 * 5
 * <p>
 * The longest substring is "ababb", as 'a' is repeated 2 times and 'b' is repeated 3 times.
 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
 * <p>
 * https://discuss.leetcode.com/topic/57372/java-3ms-divide-and-conquer-recursion-solution/2
 *
 * http://www.cnblogs.com/grandyang/p/5852352.html
 *
 * @author zhangxu
 */
public class LongestSubstringWithAtLeastKRepeatingCharacters {

    public int longestSubstring(String s, int k) {
        char[] str = s.toCharArray();
        return helper(str, 0, s.length(), k);
    }

    private int helper(char[] str, int start, int end, int k) {
        if (end < start) {
            return 0;
        }
        if (end - start < k) {
            return 0;//substring length shorter than k.
        }
        int[] count = new int[26];
        for (int i = start; i < end; i++) {
            int idx = str[i] - 'a';
            count[idx]++;
        }
        for (int i = 0; i < 26; i++) {
            if (count[i] == 0) {
                continue;//i+'a' does not exist in the string, skip it.
            }
            if (count[i] < k) {
                for (int j = start; j < end; j++) {
                    if (str[j] == i + 'a') {
                        int left = helper(str, start, j, k);
                        int right = helper(str, j + 1, end, k);
                        return Math.max(left, right);
                    }
                }
            }
        }
        return end - start;
    }

    /**
     * class Solution {
     public:
     int longestSubstring(string s, int k) {
     int res = 0, i = 0, n = s.size();
     while (i + k < n) {
     int m[26] = {0}, mask = 0, max_idx = i;
     for (int j = i; j < n; ++j) {
     int t = s[j] - 'a';
     ++m[t];
     if (m[t] < k) mask |= (1 << t);
     else mask &= (~(1 << t));
     if (mask == 0) {
     res = max(res, j - i + 1);
     max_idx = j;
     }
     }
     i = max_idx + 1;
     }
     return res;
     }
     };
     */
}
