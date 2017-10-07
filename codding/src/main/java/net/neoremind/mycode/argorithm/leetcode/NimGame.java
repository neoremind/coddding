package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * You are playing the following Nim Game with your friend: There is a heap of stones on the table, each time one of
 * you take turns to remove 1 to 3 stones. The one who removes the last stone will be the winner. You will take the
 * first turn to remove the stones.
 * <p>
 * Both of you are very clever and have optimal strategies for the game. Write a function to determine whether you
 * can win the game given the number of stones in the heap.
 * <p>
 * For example, if there are 4 stones in the heap, then you will never win the game: no matter 1, 2, or 3 stones you
 * remove, the last stone will always be removed by your friend.
 * <p>
 * 两个人玩游戏，在一堆石头里面捡石子，可以捡1，2，3个石子，假设两个人都是clever and have optimal strategies的策略，问当还剩几个石子的时候，我肯定可以赢。
 *
 * @author xu.zhang
 */
public class NimGame {

    public boolean canWinNim(int n) {
        if (n <= 3) return true;
        for (int i = 1; i <= 3; i++) {
            if (!canWinNim(n - i)) {
                return true;
            }
        }
        return false;
    }

    public boolean canWinNim2(int n) {
        if (n <= 3) return true;
        boolean[] dp = new boolean[n + 1];
        dp[1] = true;
        dp[2] = true;
        dp[3] = true;
        for (int i = 4; i <= n; i++) {
            for (int j = 1; j <= 3; j++) {
                if (!dp[i - j]) {
                    dp[i] = true;
                }
            }
        }
        return dp[n];
    }

    public boolean canWinNim3(int n) {
        if (n <= 3) return true;
        int[] dp = new int[n / 32 + 1];
        encode(1, dp);
        encode(2, dp);
        encode(3, dp);
        for (int i = 4; i <= n; i++) {
            for (int j = 1; j <= 3; j++) {
                if (!isSet(i - j, dp)) {
                    encode(i, dp);
                }
            }
        }
        return isSet(n, dp);
    }

    void encode(int n, int[] dp) {
        dp[n / 32] |= 1 << (n % 32);
    }

    boolean isSet(int x, int[] dp) {
        return (dp[x / 32] & (1 << (x % 32))) != 0;
    }

    public boolean canWinNim4(int n) {
        return n % 4 != 0;
    }

    @Test
    public void test() {
        for (int i = 1; i < 10000; i++) {
            if (i % 4 == 0) {
                assertThat(canWinNim(i), is(false));
            } else {
                assertThat(canWinNim(i), is(true));
            }
        }

    }
}
