package net.neoremind.mycode.argorithm.leetcode;

/**
 * https://leetcode.com/problems/wiggle-subsequence/
 *
 * @author xu.zx
 */
public class WiggleSequence {

  public int wiggleMaxLength(int[] nums) {
    if (nums == null) return 0;
    if (nums.length == 1) return 1;
    int preDiff = 0;
    int count = 1;
    for (int i = 1; i < nums.length; i++) {
      int diff = nums[i] - nums[i - 1];
      if ((diff < 0 && preDiff >= 0) ||
              (diff > 0 && preDiff <= 0)) {
        count++;
        preDiff = diff;
      }
    }
    return count;
  }

}
