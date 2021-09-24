package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * https://leetcode.com/problems/queue-reconstruction-by-height/
 *
 * @author xu.zx
 */
public class QueueReconstructionByHeight {

  public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, new Comparator<int[]>() {
      public int compare(int[] a, int[] b) {
        if (a[0] == b[0]) {
          return Integer.compare(a[1], b[1]);
        }
        return Integer.compare(b[0], a[0]);
      }
    });
    List<int[]> res = new ArrayList<int[]>();
    for (int[] p : people) {
      res.add(p[1], p);
    }
    return res.toArray(new int[res.size()][]);
  }
}
