package net.neoremind.mycode.argorithm.leetcode;

/**
 * Given two words word1 and word2, find the minimum number of steps required to convert word1 to word2. (each
 * operation is counted as 1 step.)
 * <p>
 * You have the following 3 operations permitted on a word:
 * <p>
 * a) Insert a character
 * b) Delete a character
 * c) Replace a character
 * <p>
 * 比较难想出来的动态规划题目，HARD。《编程之法》中有解释。
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/edit-distance/
 */
public class EditDistance {

    /**
     * <pre>
     * d[i][0] = i
     * d[0][j] = j
     * d[i][j] = d[i-1][j-1] if word1[i-1]=word2][j-1]
     * d[i][j] = min(d[i-1][j-1] + 1, d[i-1][j] + 1, d[i][j-1] + 1)
     * </pre>
     */
    public int minDistance(String word1, String word2) {
        if (word1 == null || word1.length() == 0) {
            return word2.length();
        }
        if (word2 == null || word2.length() == 0) {
            return word1.length();
        }
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 1; i <= word1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 1; j <= word2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + 1);
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }
}
