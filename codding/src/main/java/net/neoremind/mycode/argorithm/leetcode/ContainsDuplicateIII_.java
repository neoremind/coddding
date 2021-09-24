package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bucket sort.
 *
 * @author xu.zx
 */
public class ContainsDuplicateIII_ {

  public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
    if (nums == null || nums.length == 0) return false;
    if (nums.length == 1) return false;
    // find min
    long min = findMin(nums);
    long max = findMax(nums);
    // make min == 0
    // 注意这里必须min + 1，因为要处理数字等于0的时候，求模的时候溢出问题，
    // 这个题就是注意各种long和int的转化，已经边界条件，才可以写对bucket sort的解法
    long[] a = new long[nums.length];
    if (min >= 0) {
      for (int i = 0; i < nums.length; i++) {
        a[i] = (long) nums[i] - min + 1L;
      }
      max -= (min - 1L);
    } else {
      long delta = ~min + 1;
      for (int i = 0; i < nums.length; i++) {
        a[i] = (long) nums[i] + delta;
      }
      max += delta;
    }
    long bucketNum = (max / ((long) t + 1)) + 1;
    // bucket
    Map<Long, List<Long>> bucket = new HashMap<>();
    int windowSize = 0;
    for (int i = 0; i < a.length; i++) {
      long index = a[i] / ((long) t + 1);
      if (bucket.get(index) == null) {
        bucket.put(index, new ArrayList<>());
      }
      if (!bucket.get(index).isEmpty()) {
        return true;
      }
      if (index > 0 && bucket.get(index - 1) != null &&
              a[i] - bucket.get(index - 1).get(0) <= t) {
        return true;
      }
      if (index < bucketNum && bucket.get(index + 1) != null &&
              bucket.get(index + 1).get(0) - a[i] <= t) {
        return true;
      }
      bucket.get(index).add(a[i]);
      if (++windowSize > k) {
        bucket.remove(a[i - k] / ((long) t + 1));
      }
    }
    return false;
  }

  int findMin(int[] a) {
    int min = a[0];
    for (int i = 1; i < a.length; i++) {
      min = Math.min(min, a[i]);
    }
    return min;
  }

  int findMax(int[] a) {
    int max = a[0];
    for (int i = 1; i < a.length; i++) {
      max = Math.max(max, a[i]);
    }
    return max;
  }

  public static void main(String[] args) {
    System.out.println(new ContainsDuplicateIII_().containsNearbyAlmostDuplicate(new int[]{1, 0, 1, 1}, 1, 2));
    System.out.println(new ContainsDuplicateIII_().containsNearbyAlmostDuplicate(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, 1, 1));
    System.out.println(new ContainsDuplicateIII_().containsNearbyAlmostDuplicate(new int[]{Integer.MAX_VALUE, -1, Integer.MAX_VALUE}, 1, Integer.MAX_VALUE));
  }
}
