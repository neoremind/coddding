package net.neoremind.mycode.argorithm.leetcode;

import java.util.Random;

/**
 * @author xu.zx
 */
public class ShuffleAnArray {

  int[] nums;

  int[] result;

  Random r = new Random();

  public ShuffleAnArray(int[] nums) {
    this.nums = nums;
    this.result = new int[nums.length];
    System.arraycopy(this.nums, 0, this.result, 0, nums.length);
  }

  /**
   * Resets the array to its original configuration and return it.
   */
  public int[] reset() {
    // System.out.println("reset");
    System.arraycopy(nums, 0, result, 0, nums.length);
    return nums;
  }

  /**
   * Returns a random shuffling of the array.
   */
  public int[] shuffle() {
    for (int i = nums.length - 1; i >= 0; i--) {
      //System.out.println(i);
      int index = r.nextInt(i + 1);
      swap(index, i, result);
    }
    // for (int i = 0; i < nums.length; i++) {
    //     int index = i + (int)(Math.random() * (nums.length - i));
    //     swap(index, i, result);
    // }
    return result;
  }

  private void swap(int i, int j, int[] result) {
    // System.out.println(result[i] + " " + result[j]);
    // if (i != j) {
    // result[i] ^= result[j];
    // result[j] ^= result[i];
    // result[i] ^= result[j];
    // }
    // System.out.println(result[i] + " " + result[j]);
    if (i != j) {
      int temp = result[i];
      result[i] = result[j];
      result[j] = temp;
    }
  }
}
