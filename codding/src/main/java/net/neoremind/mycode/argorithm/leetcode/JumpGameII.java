package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.ListNode;
import net.neoremind.mycode.argorithm.leetcode.support.ListNodeHelper;

/**
 * Given an array of non-negative integers, you are initially positioned at the first index of the array.
 * <p>
 * Each element in the array represents your maximum jump length at that position.
 * <p>
 * Your goal is to reach the last index in the minimum number of jumps.
 * <p>
 * For example:
 * Given array A = [2,3,1,1,4]
 * <p>
 * The minimum number of jumps to reach the last index is 2. (Jump 1 step from index 0 to 1, then 3 steps to the last
 * index.)
 * <p>
 * Note:
 * You can assume that you can always reach the last index.
 * <p>
 * 参考最小生成树的算法
 * http://www.geeksforgeeks.org/greedy-algorithms-set-5-prims-minimum-spanning-tree-mst-2/
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/jump-game-ii/
 */
public class JumpGameII {

    /**
     * 首先要明确，联想{@link JumpGame}，在位置i，最大跳nums[i]步骤，那么就是说下次最大跳到i+nums[i]的索引位置，
     * 每一步都保存这个最大值，那么如果遍历到某个位置，发现最大的max都比这个i小，自然表示肯定跳不到这了，这个问题就没有解。
     * <p>
     * 时间复杂度O(N)
     * <pre>
     * max=nums[0]目前可以reach的最大索引位置
     * last=0 上次reach的最大索引位置
     * count=0
     * for i in nums
     *     count++
     *     for idx in [last+1, max]  从max往上次能跳到的最大的max看这个区间里面下次能reach的最远索引位置
     *         max = MAX(nums[i]+i, max)
     *
     *     if max <= i
     *         return -1 原地不动了，调不到了。当然根据leetcode题目不可能出现，但是以防万一
     *     last = i
     *     i = max
     * return count
     * </pre>
     */
    public int jump(int[] nums) {
        int max = nums[0];
        int count = 0; //步数
        int last = 0;
        for (int i = 0; i < nums.length - 1; ) {
            count++;
            for (int j = i; j > last; j--) {
                max = Math.max(nums[j] + j, max);
                if (max >= nums.length - 1) {
                    return count;
                }
            }
            if (max <= i) { //跳不动了，甚至还往回跳了
                return -1;
            }
            last = i;
            i = max;
        }
        return count;
    }

    @Test
    public void test() {
        int[] nums = new int[] {2, 3, 1, 1, 4};
        assertThat(jump(nums), is(2));
        nums = new int[] {0};
        assertThat(jump(nums), is(0));
        nums = new int[] {1};
        assertThat(jump(nums), is(0));
        nums = new int[] {2};
        assertThat(jump(nums), is(0));
        nums = new int[] {2, 3, 1, 4, 0, 0, 0, 0, 1, 2};
        assertThat(jump(nums), is(-1));
        nums = new int[] {0, 3, 1, 4, 0, 0, 0, 0, 1, 2};
        assertThat(jump(nums), is(-1));
    }

}
