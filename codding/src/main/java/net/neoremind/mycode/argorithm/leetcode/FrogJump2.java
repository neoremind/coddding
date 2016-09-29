package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

/**
 * 403. Frog Jump  QuestionEditorial Solution  My Submissions
 * Total Accepted: 3273
 * Total Submissions: 9781
 * Difficulty: Hard
 * A frog is crossing a river. The river is divided into x units and at each unit there may or may not exist a stone.
 * The frog can jump on a stone, but it must not jump into the water.
 * <p>
 * Given a list of stones' positions (in units) in sorted ascending order, determine if the frog is able to cross the
 * river by landing on the last stone. Initially, the frog is on the first stone and assume the first jump must be 1
 * unit.
 * <p>
 * If the frog's last jump was k units, then its next jump must be either k - 1, k, or k + 1 units. Note that the
 * frog can only jump in the forward direction.
 * <p>
 * Note:
 * <p>
 * The number of stones is ≥ 2 and is < 1,100.
 * Each stone's position will be a non-negative integer < 231.
 * The first stone's position is always 0.
 * Example 1:
 * <p>
 * [0,1,3,5,6,8,12,17]
 * <p>
 * There are a total of 8 stones.
 * The first stone at the 0th unit, second stone at the 1st unit,
 * third stone at the 3rd unit, and so on...
 * The last stone at the 17th unit.
 * <p>
 * Return true. The frog can jump to the last stone by jumping
 * 1 unit to the 2nd stone, then 2 units to the 3rd stone, then
 * 2 units to the 4th stone, then 3 units to the 6th stone,
 * 4 units to the 7th stone, and 5 units to the 8th stone.
 * Example 2:
 * <p>
 * [0,1,2,3,4,8,9,11]
 * <p>
 * Return false. There is no way to jump to the last stone as
 * the gap between the 5th and 6th stone is too large.
 * <p>
 * https://leetcode.com/problems/frog-jump/
 * <p>
 * <pre>
 *  _ _   _   _ _   _         _              _
 *  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
 *  |_|   |   | |   |
 *   1    |   | |   |
 *    |___|   | |   |
 *      2     | |   |
 *        |___| |   |
 *          2   |   |
 *           |__|   |
 *             1    |
 *              |___|
 *                2  ===> [1,2,3] furtherest to 11 not 12 so backtrack
 *
 * 在5的位置“跳力”是[1,2,3]，那么下次可以选择跳到6和8，如果是6则如上跳不出去了，所以需要DFS选择8继续，如下图所示即可以跳出去。
 *  _ _   _   _ _   _         _              _
 *  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
 *  |_|   |   |     |         |              |
 *   1    |   |     |         |              |
 *    |___|   |     |         |              |
 *      2     |     |         |              |
 *        |___|     |         |              |
 *          2       |         |              |
 *            |_____|         |              |
 *               3            |              |
 *                  |_________|              |
 *                       4                   |
 *                            |______________|
 *                                    5
 *
 * 此时如果8不是下一个数字，而是9，那么也跳不出去，那么返回的结论就是跳不出去了。
 *
 *
 * </pre>
 *
 * @author zhangxu
 */
public class FrogJump2 {

    @Test
    public void test() {
        int[] stones = new int[] {0, 1, 3, 5, 6, 8, 12, 17};
        assertThat(canCross(stones), is(true));
        assertThat(canCross2(stones), is(true));

        stones = new int[] {0, 1, 2, 3, 4, 8, 9, 11};
        assertThat(canCross(stones), is(false));
        assertThat(canCross2(stones), is(false));

        stones = new int[] {0, 1, 3, 4, 5, 7, 9, 10, 12};
        assertThat(canCross(stones), is(true));
        assertThat(canCross2(stones), is(true));
    }

    public boolean canCross(int[] stones) {
        if (stones[1] > 1) {
            return false;
        }
        return dfs(stones, 0, 0);
    }

    boolean dfs(int[] stones, int index, int lastStep) {
        System.out.println("index=" + index + ", value=" + stones[index] + ", lastStep=" + lastStep);
        for (int i = index + 1; i < stones.length; i++) {
            if (stones[i] - stones[index] < lastStep - 1) {
                System.out.println("i=" + i + ", value=" + stones[i] + ", lastStep=" + lastStep + " go on");
                continue;
            }
            if (stones[i] - stones[index] > lastStep + 1) {
                System.out.println("i=" + i + ", value=" + stones[i] + ", lastStep=" + lastStep + " too long");
                return false;
            }
            if (dfs(stones, i, stones[i] - stones[index])) {
                return true;
            }
        }
        return index == stones.length - 1;
    }

    public boolean canCross2(int[] stones) {
        if (stones[1] > 1) {
            return false;
        }
        int[][] dp = new int[stones.length][stones.length];
        return dfs2(stones, 0, 0, dp);
    }

    boolean dfs2(int[] stones, int index, int lastStep, int[][] dp) {
        if (dp[index][lastStep] == 1) {
            return false;
        }
        for (int i = index + 1; i < stones.length; i++) {
            if (stones[i] - stones[index] < lastStep - 1) {
                continue;
            }
            if (stones[i] - stones[index] > lastStep + 1) {
                dp[i][lastStep] = 1;
                return false;
            }
            if (dfs2(stones, i, stones[i] - stones[index], dp)) {
                return true;
            }
        }
        return index == stones.length - 1;
    }

}
