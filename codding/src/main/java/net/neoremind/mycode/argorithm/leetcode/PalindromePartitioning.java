package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * <p>
 * Return all possible palindrome partitioning of s.
 * <p>
 * For example, given s = "aab",
 * Return
 * <p>
 * [
 * ["aa","b"],
 * ["a","a","b"]
 * ]
 *
 * @author xu.zhang
 */
public class PalindromePartitioning {

    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        helper(s, 0, s.length(), res, new ArrayList<>());
        return res;
    }

    void helper(String s, int index, int len, List<List<String>> res, List<String> temp) {
        if (index == len) {
            res.add(new ArrayList<>(temp));
        } else {
            for (int i = index; i < len; i++) {
                String sub = s.substring(index, i + 1);
                if (isP(sub)) {
                    temp.add(sub);
                    helper(s, i + 1, len, res, temp);
                    temp.remove(temp.size() - 1);
                }
            }
        }
    }

    boolean isP(String s) {
        if (s == null || s.length() == 0) return true;
        if (s.length() == 1) return true;
        char[] chars = s.toCharArray();
        int left = 0;
        int right = chars.length - 1;
        while (left < right) {
            if (chars[left++] != chars[right--]) {
                return false;
            }
        }
        return true;
    }

}
