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
 * <p>
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
     * 是{@link #longestSubstringBruteForce(String, int)}的改进
     */
    public int longestSubstring2(String s, int k) {
        int i = 0;
        int res = 0;
        while (i + k < s.length()) {
            int[] counter = new int[26];
            int mask = 0;
            int maxIdx = i;
            for (int j = i; j < s.length(); j++) {
                int charIdx = s.charAt(j) - 'a';
                counter[charIdx]++;
                if (counter[charIdx] < k) {
                    mask |= (1 << charIdx);
                } else {
                    mask &= (~(1 << charIdx));  //变成0的办法
                }
                if (mask == 0) {
                    res = Math.max(res, j - i + 1);
                    maxIdx = j;
                }
            }
            i = maxIdx + 1;
        }
        return res;
    }

    /**
     * 这种也是AC的
     */
    public int longestSubstringBruteForce(String s, int k) {
        int i = 0;
        int res = 0;
        while (i + k < s.length()) {
            int[] counter = new int[26];
            int maxIdx = i;
            for (int j = i; j < s.length(); j++) {
                int charIdx = s.charAt(j) - 'a';
                counter[charIdx]++;
                boolean isAllGreaterOrEqualK = true;
                for (int m = 0; m < 26; m++) {
                    if (counter[m] != 0 && counter[m] < k) {
                        isAllGreaterOrEqualK = false;
                        break;
                    }
                }
                if (isAllGreaterOrEqualK) {
                    res = Math.max(res, j - i + 1);
                    maxIdx = j;
                }
            }
            i = maxIdx + 1;
        }
        return res;
    }
}
