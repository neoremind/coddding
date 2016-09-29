package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

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
 *
 * @author zhangxu
 */
public class FrogJump {

    @Test
    public void test() {
        int[] stones = new int[] {0, 1, 3, 5, 6, 8, 12, 16};
        assertThat(canCross(stones), is(true));

        //        stones = new int[] {0, 1, 2};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 3};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 4};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 5};
        //        assertThat(canCross(stones), is(false));
        //
        //        stones = new int[] {0, 1, 3, 4};
        //        assertThat(canCross(stones), is(false));
        //
        //        stones = new int[] {0, 1, 3, 5};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 3, 6};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 3, 7};
        //        assertThat(canCross(stones), is(true));
        //
        //        stones = new int[] {0, 1, 3, 9};
        //        assertThat(canCross(stones), is(false));
    }

    public boolean canCross(int[] stones) {
        if (stones[1] > 1) {
            return false;
        }
        int max = 0;
        for (int stone : stones) {
            max = Math.max(stone, max);
        }
        int[] path = new int[max + 1];
        for (int stone : stones) {
            path[stone] = 1;
        }
        return helper(path, 2, 1);
    }

    boolean helper(int[] path, int step, int idx) {
        System.out.println("step:" + step + ", idx=" + idx);
        if (idx > path.length - 1 || path[idx] != 1) {
            System.out.println("idx=" + idx + " which is invalid, so backtrack");
            return false;
        }
        int[] nexts = getNexts(step, idx);
        System.out.println("nexts may be:" + Arrays.toString(nexts));
        if (idx == path.length - 1) {
            return true;
        }
        boolean canJumpToEnd = helper(path, step + 1, nexts[0]) ||
                helper(path, step + 1, nexts[1]) ||
                helper(path, step + 1, nexts[2]);
        return canJumpToEnd;
    }

    int[] getNexts(int step, int idx) {
        return new int[] {idx + step - 1, idx + step, idx + step + 1};
    }

}
