package net.neoremind.mycode.argorithm.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://leetcode.com/problems/partition-labels/
 *
 * @author xu.zx
 */
public class PartitionLabels {

  public List<Integer> partitionLabels(String S) {
    List<Integer> res = new ArrayList<>();
    char[] c = S.toCharArray();
    // 找到每个字母的最远位置
    Map<Character, Integer> map = new HashMap<>();
    for (int i = 0; i < c.length; i++) {
      map.put(c[i], i);
    }
    // 新建一个数组存最远位置
    int[] a = new int[c.length];
    for (int i = 0; i < c.length; i++) {
      a[i] = map.get(c[i]);
    }
    // 找分隔点
    int max = 0;
    for (int i = 0; i < c.length; i++) {
      max = Math.max(a[i], max);
      if (max == i) {
        res.add(i);
      }
    }

    // 输出
    List<Integer> res2 = new ArrayList<>();
    res2.add(res.get(0) + 1);
    for (int i = 1; i < res.size(); i++) {
      res2.add(res.get(i) - res.get(i - 1));
    }
    return res2;
  }

}
