package net.neoremind.mycode.argorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 带记忆化搜索的版本。
 *
 * 题目要求maxChoosableInteger < 20，所以用一个int4个字节做encode就可。
 *
 * @author xu.zx
 */
public class CanIWin_ {

  public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
    if (desiredTotal == 0) return true;
    if (sum(maxChoosableInteger) < desiredTotal) {
      return false;
    }
    Map<Integer, Boolean> state = new HashMap<>();
    return canIWin(maxChoosableInteger, desiredTotal, 0, state);
  }

  private boolean canIWin(int maxChoosableInteger, int target, int visited, Map<Integer, Boolean> state) {
    if (target <= 0) {
      return false;
    }
    if (state.containsKey(visited)) {//visted状态已经出现过，直接返回结果
      return state.get(visited);
    }
    for (int i = 1; i <= maxChoosableInteger; i++) {
      //i这个数之前取用过
      if ((visited & (1 << i)) > 0) {
        continue;
      }
      //本人当局可赢，或对方在本人取i后会输
      if (!canIWin(maxChoosableInteger, target - i, visited | (1 << i), state)) {
        state.put(visited, true);
        return true;
      }

    }
    state.put(visited, false);
    return false;
  }

  private int sum(int n) {
    return (n + 1) * n / 2;
  }
}
