package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * In the "100 game," two players take turns adding, to a running total, any integer from 1..10. The player who first
 * causes the running total to reach or exceed 100 wins.
 * <p>
 * What if we change the game so that players cannot re-use integers?
 * <p>
 * For example, two players might take turns drawing from a common pool of numbers of 1..15 without replacement until
 * they reach a total >= 100.
 * <p>
 * Given an integer maxChoosableInteger and another integer desiredTotal, determine if the first player to move can
 * force a win, assuming both players play optimally.
 * <p>
 * You can always assume that maxChoosableInteger will not be larger than 20 and desiredTotal will not be larger than
 * 300.
 * <p>
 * Example
 * <p>
 * Input:
 * maxChoosableInteger = 10
 * desiredTotal = 11
 * <p>
 * Output:
 * false
 * <p>
 * Explanation:
 * No matter which integer the first player choose, the first player will lose.
 * The first player can choose an integer from 1 up to 10.
 * If the first player choose 1, the second player can only choose integers from 2 up to 10.
 * The second player will win by choosing 10 and get a total = 11, which is >= desiredTotal.
 * Same with other integers chosen by the first player, the second player will always win.
 *
 * @author xu.zhang
 */
@Deprecated
public class CanIWin {

    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        if (desiredTotal <= 0) return true;
        //如果1到最大能选的值所有和都不能满足目标值，那么肯定失败
        if (maxChoosableInteger * (maxChoosableInteger + 1) / 2 < desiredTotal) return false;
        List<Integer> numbers = new ArrayList<>(maxChoosableInteger);
        for (int i = 1; i <= maxChoosableInteger; i++) {
            numbers.add(i);
        }
        boolean[] used = new boolean[maxChoosableInteger];
        return helper(numbers, desiredTotal, used, new HashMap<>());
    }

    boolean helper(List<Integer> numbers, int desiredTotal, boolean[] used, Map<String, Boolean> mem) {
        String key = encode(numbers);
        if (mem.containsKey(key)) {
            return mem.get(key);
        }
        if (desiredTotal <= 0) {
            return false;
        }
        for (int i = 0; i < numbers.size(); i++) {
            int temp = numbers.get(i);
            numbers.remove(i);
            if (!helper(numbers, desiredTotal - temp, used, mem)) {
                mem.put(key, true);
                numbers.add(i, temp);
                return true;
            }
            numbers.add(i, temp);
        }
        mem.put(key, false);
        return false;
    }

    String encode(List<Integer> numbers) {
        StringBuilder sb = new StringBuilder();
        for (Integer number : numbers) {
            sb.append(number);
            sb.append(",");
        }
        return sb.toString();
    }

    @Test
    public void test() {
        assertThat(canIWin(2, 2), is(true));
        assertThat(canIWin(2, 3), is(false));
        assertThat(canIWin(10, 11), is(false));
        assertThat(canIWin(18, 79), is(true));
        assertThat(canIWin(12, 23), is(true));
    }
}
