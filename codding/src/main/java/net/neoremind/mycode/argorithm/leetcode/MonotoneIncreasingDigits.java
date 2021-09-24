package net.neoremind.mycode.argorithm.leetcode;

/**
 * https://leetcode.com/problems/monotone-increasing-digits/
 *
 * @author xu.zx
 */
public class MonotoneIncreasingDigits {

  public int monotoneIncreasingDigits(int N) {
    if (N == 0) return 0;
    StringBuilder str = new StringBuilder();
    int pre = Integer.MAX_VALUE;
    while (N != 0) {
      int mod = N % 10;
      if (mod > pre) {
        int strLen = str.length();
        str.delete(0, str.length());
        for (int i = 0; i < strLen; i++) {
          str.append("9");
        }
        str.append(--mod);
      } else {
        str.append(mod);
      }
      pre = mod;
      N /= 10;
    }
    return Integer.parseInt(str.reverse().toString());
  }

  /** TLE */
  public int monotoneIncreasingDigits2(int N) {
    for (int i = N; i >= 0; i--) {
      if (isMono(i)) {
        return i;
      }
    }
    return 0;
  }

  private boolean isMono(int n) {
    int pre = Integer.MAX_VALUE;
    while (n != 0) {
      int mod = n % 10;
      if (mod > pre) {
        return false;
      }
      pre = mod;
      n /= 10;
    }
    return true;
  }

}
