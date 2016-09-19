package net.neoremind.mycode.argorithm.dp;

import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Longest Common Subsequence, LCS, 最长公共子序列，不要求连续。
 * <p>
 * 和0/1背包问题非常相似，同时找子序列还用到了回溯法backtrack
 *
 *
 *
 * @author zhangxu
 * @see https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
 */
public class LCS {

    /**
     * 递推式如下，初值是第一行和第一列为0
     * 不断的遍历两个字符串，维护i和j两个索引，先遍历X串的某行，如果发现相同char的则最长的串+1，并且记录C[i][j]是这个长度，
     * 然后下一个j，直到遍历完毕这行，然后下一行
     * <pre>
     *            0                               if i = 0 or j = 0
     * C[i, j] =  C[i - 1, j - 1] + 1             if i,j > 0 and Xi = Yj
     *            max{C[i, j - 1] , c[i - 1, j]}  if i,j > 0 and Xi != Yj
     * </pre>
     * <pre>
     * function LCSLength(X[1..m], Y[1..n])
     *     C = array(0..m, 0..n)
     *     for i := 0..m
     *         C[i,0] = 0
     *     for j := 0..n
     *         C[0,j] = 0
     *     for i := 1..m
     *         for j := 1..n
     *             if X[i] = Y[j]
     *                 C[i,j] := C[i-1,j-1] + 1
     *             else
     *                 C[i,j] := max(C[i,j-1], C[i-1,j])
     *     return C[m,n]
     * </pre>
     */
    public int longestCommonSubsequenceDP(String a, String b) {
        if (a == null || b == null) {
            return 0;
        }

        int lenA = a.length();
        int lenB = b.length();
        int[][] D = new int[lenA + 1][lenB + 1];  //新建大一维的数组
        for (int i = 0; i <= lenA; i++) {
            D[i][0] = 0;
        }
        for (int j = 0; j <= lenB; j++) {
            D[0][j] = 0;
        }

        for (int i = 1; i <= lenA; i++) {
            for (int j = 1; j <= lenB; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    D[i][j] = D[i - 1][j - 1] + 1;
                } else {
                    D[i][j] = Math.max(D[i - 1][j], D[i][j - 1]);
                }
            }
        }

        // print 2D array
        System.out.println("    " + Arrays.toString(b.toCharArray()));
        for (int idx = 0; idx < D.length; idx++) {
            System.out.println((idx == 0 ? " " : a.charAt(idx - 1)) + Arrays.toString(D[idx]));
        }

        // print longest common subsequence
        System.out.println(backtrack(D, a, b, a.length(), b.length()));

        return D[lenA][lenB];
    }

    /**
     * 回溯方法找路径，不断的往左、上爬。
     * <pre>
     * function backtrack(C[0..m,0..n], X[1..m], Y[1..n], i, j)
     *     if i = 0 or j = 0
     *         return ""
     *     else if  X[i] = Y[j]
     *         return backtrack(C, X, Y, i-1, j-1) + X[i]
     *     else
     *         if C[i,j-1] > C[i-1,j]
     *             return backtrack(C, X, Y, i, j-1)
     *     else
     *         return backtrack(C, X, Y, i-1, j)
     * </pre>
     */
    private String backtrack(int[][] D, String a, String b, int i, int j) {
        if (i == 0 || j == 0) {
            return "";
        } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return backtrack(D, a, b, i - 1, j - 1) + a.charAt(i - 1);
        } else if (D[i][j - 1] > D[i - 1][j]) {
            return backtrack(D, a, b, i, j - 1);
        } else {
            return backtrack(D, a, b, i - 1, j);
        }
    }

    /**
     * <pre>
     *     [x, j, k, c, d, e, a, l, c, e, f, g, k]
     *  [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
     * a[0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1]
     * b[0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1]
     * c[0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2]
     * d[0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2]
     * e[0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3]
     * f[0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 3, 4, 4, 4]
     * g[0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 3, 4, 5, 5]
     * </pre>
     * <p>
     * <pre>
     *     [M, Z, J, A, W, X, U]
     *  [0, 0, 0, 0, 0, 0, 0, 0]
     * X[0, 0, 0, 0, 0, 0, 1, 1]
     * M[0, 1, 1, 1, 1, 1, 1, 1]
     * J[0, 1, 1, 2, 2, 2, 2, 2]
     * Y[0, 1, 1, 2, 2, 2, 2, 2]
     * A[0, 1, 1, 2, 3, 3, 3, 3]
     * U[0, 1, 1, 2, 3, 3, 3, 4]
     * Z[0, 1, 2, 2, 3, 3, 3, 4]
     * </pre>
     */
    @Test
    public void test() {
        String a = "abcdefg";
        String b = "xjkcdealcefgk";
        assertThat(longestCommonSubsequenceDP(a, b), Matchers.is(5)); //acefg
        a = "cnblogs";
        b = "belong";
        assertThat(longestCommonSubsequenceDP(a, b), Matchers.is(4)); //blog

        a = "XMJYAUZ";
        b = "MZJAWXU";
        assertThat(longestCommonSubsequenceDP(a, b), Matchers.is(4)); //blog
    }

}
