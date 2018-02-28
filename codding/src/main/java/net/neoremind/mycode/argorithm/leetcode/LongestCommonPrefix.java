package net.neoremind.mycode.argorithm.leetcode;

/**
 * Write a function to find the longest common prefix string amongst an array of strings.
 *
 * @author xu.zhang
 */
public class LongestCommonPrefix {

    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String pivot = strs[0];
        int res = 0;
        for (int i = 1; i <= pivot.length(); i++) {
            for (int j = 1; j < strs.length; j++) {
                if (strs[j] == null || strs[j].equals("")) return "";
                if (i > strs[j].length() ||
                        !strs[j].substring(0, i).equals(pivot.substring(0, i))) {
                    return pivot.substring(0, i - 1);
                }
            }
            res = i;
        }
        return pivot.substring(0, res);
    }
}
