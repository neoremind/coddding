package net.neoremind.mycode.argorithm.leetcode;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/assign-cookies/
 *
 * @author xu.zx
 */
public class AssignCookies {

  public int findContentChildren(int[] g, int[] s) {
    Arrays.sort(g);
    Arrays.sort(s);
    int len1 = g.length;
    int len2 = s.length;
    int i = len1 - 1, j = len2 - 1;
    int count = 0;
    while (i >= 0 && j >= 0) {
      if (s[j] >= g[i]) {
        j--;
        i--;
        count++;
      } else {
        i--;
      }
    }
    return count;
  }
}
