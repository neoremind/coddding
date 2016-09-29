package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
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
 * 这个是在理解题目意义错误的情况下做的Backtrack+DFS的方式。
 * <p>
 * 第一次写代码错误的解读了：
 * If the frog's last jump was k units, then its next jump must be either k - 1, k, or k + 1 units.
 * <p>
 * 认为如果frog是第K跳，则下次最多跳K-1，K，K+1的位置。
 * <p>
 * 其实是上次的跳跃结果。。。faint :-(
 * <p>
 * https://leetcode.com/problems/frog-jump/
 *
 * @author zhangxu
 */
@Deprecated
public class FrogJump {

    @Test
    public void test() {
        int[] stones = new int[] {0, 1, 3, 5, 6, 8, 12, 17};
        assertThat(canCross(stones), is(true));

        stones = new int[] {0, 1, 2, 3, 4, 8, 9, 11};
        assertThat(canCross(stones), is(true));
    }

    public boolean canCross(int[] stones) {
        if (stones[1] > 1) {
            return false;
        }
        return dfs(stones, 2, 1, 1);
    }

    boolean dfs(int[] stones, int step, int idx, int position) {
        System.out.println("step:" + step + ", idx=" + idx + ", position=" + position);
        List<Stone> nexts = getNexts(stones, step, idx, position);
        System.out.println("nexts may be:" + nexts);
        if (idx == stones.length - 1) {
            System.out.println("got to end!");
            return true;
        }
        if (nexts.isEmpty()) {
            System.out.println("no further, backtrack..");
            return false;
        }
        boolean canJumpToEnd = false;
        for (Stone next : nexts) {
            if (canJumpToEnd) {
                break;
            }
            canJumpToEnd = dfs(stones, step + 1, next.idx, next.position);
        }
        return canJumpToEnd;
    }

    List<Stone> getNexts(int[] path, int step, int idx, int position) {
        List<Stone> nexts = new ArrayList<>(3);
        for (int i = idx + 1; i < path.length; i++) {
            if (path[i] == position + step - 1 ||
                    path[i] == position + step ||
                    path[i] == position + step + 1) {
                nexts.add(new Stone(i, path[i]));
            }
        }
        return nexts;
    }

    class Stone {
        int idx;
        int position;

        Stone(int idx, int position) {
            this.idx = idx;
            this.position = position;
        }

        @Override
        public String toString() {
            return "Stone{" +
                    "idx=" + idx +
                    ", position=" + position +
                    '}';
        }
    }

}
