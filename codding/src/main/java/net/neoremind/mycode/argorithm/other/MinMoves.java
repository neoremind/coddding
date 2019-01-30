package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * @author xu.zx
 */
public class MinMoves {
  public static void main(String[] args) {
    float[] f1 = new float[]{1.4f, 2.3f, 3.5f};
    float[] f2 = new float[]{5.4f, 3.1f, 1.2f};
    float[] f3 = new float[]{2.4f, 1.3f, 1.3f, 5f};
    float[] f4 = new float[]{1.34f, 1.14f, 1.7f, 1.42f, 1.66f, 1.47f, 1.68f, 1.94f, 1.45f, 1.87f, 1.84f, 1.84f};
    System.out.println(move2(f1));
    System.out.println(move2(f2));
    System.out.println(move2(f3));
    System.out.println(move2(f4));
    Scanner scanner = new Scanner(System.in);
    int lines = Integer.parseInt(scanner.nextLine());
    for (int i = 0; i < lines; i++) {
      String[] strings = scanner.nextLine().split(" ");
      int count = Integer.parseInt(strings[0]);
      float[] a = new float[count];
      for (int j = 0; j < count; j++) {
        a[j] = Float.parseFloat(strings[j + 1]);
      }
      System.out.println(move2(a));
    }
  }

  private static int move(float[] a) {
    System.out.println("len=" + a.length);
    Queue<Float> pq = new PriorityQueue<>(a.length, (x, y) -> Float.compare(x, y));
    //Map<Float, Integer> numberIndex = new HashMap<>(a.length / 3 * 4);
    List<Float> list = new ArrayList<>(a.length);

    for (int i = 0; i < a.length; i++) {
      pq.add(a[i]);
//      numberIndex.put(a[i], i);
      list.add(a[i]);
    }
    int result = 0;
    while (!pq.isEmpty()) {
      float cur = pq.poll();
      int index = list.indexOf(cur);
      if (index > pq.size()) {
        System.out.println("move " + cur);
        result++;
        list.remove(cur);
      }
    }
    return result;
  }

  private static int move2(float[] nums) {
    return nums.length - LIS2(nums);
  }

  public static int LIS(float[] nums) {
    int[] longest = new int[nums.length];
    for (int i = 0; i < nums.length; i++) {
      longest[i] = 1;
    }
    for (int i = 0; i < nums.length; i++) {
      for (int j = 0; j < i; j++) {
        if (nums[i] >= nums[j] && longest[i] < longest[j] + 1) {
          longest[i] = longest[j] + 1;
        }
      }
    }
    int max = -1;
    for (int i = 0; i < longest.length; i++) {
      max = Math.max(longest[i], max);
    }
    return max;
  }

  public static int LIS2(float[] nums) {
    TreeSet<Float> set = new TreeSet<Float>();
    for (float num : nums) {
      Float ceil = set.ceiling(num);
      if (ceil != null) set.remove(ceil);
      set.add(num);
    }
    return set.size();
  }

}
