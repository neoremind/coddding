package net.neoremind.mycode.argorithm.leetcode;

/**
 * https://leetcode.com/problems/lemonade-change/
 *
 * @author xu.zx
 */
public class LemonadeChange {

  public boolean lemonadeChange(int[] bills) {

    int dollar5 = 0;
    int dollar10 = 0;
    int dollar20 = 0;
    for (int b : bills) {
      if (b == 5) {
        dollar5++;
      } else if (b == 10) {
        if (dollar5 > 0) {
          dollar5--;
          dollar10++;
        } else {
          return false;
        }
      } else if (b == 20) {
        if (dollar10 > 0 && dollar5 > 0) {
          dollar10--;
          dollar5--;
        } else if (dollar5 > 2) {
          dollar5 -= 3;
        } else {
          return false;
        }
      } else {
        throw new RuntimeException("should not happen");
      }
    }
    return true;
  }
}
