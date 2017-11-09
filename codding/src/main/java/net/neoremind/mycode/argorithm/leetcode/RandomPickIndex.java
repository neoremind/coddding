package net.neoremind.mycode.argorithm.leetcode;

import java.util.Random;

/**
 * Given an array of integers with possible duplicates, randomly output the index of a given target number. You can
 * assume that the given target number must exist in the array.
 * <p>
 * Note:
 * The array size can be very large. Solution that uses too much extra space will not pass the judge.
 * <p>
 * Example:
 * <p>
 * int[] nums = new int[] {1,2,3,3,3};
 * Solution solution = new Solution(nums);
 * <p>
 * // pick(3) should return either index 2, 3, or 4 randomly. Each index should have equal probability of returning.
 * solution.pick(3);
 * <p>
 * // pick(1) should return 0. Since in the array only nums[0] is equal to 1.
 * solution.pick(1);
 * <p>
 * 蓄水池算法（Reservoir Sampling）Simple Reservoir Sampling solution。
 * //stream代表数据流
 * //reservoir代表返回长度为k的池塘
 * <p>
 * //从stream中取前k个放入reservoir；
 * <p>
 * <pre>
 * for ( int i = 1; i < k; i++)
 *     reservoir[i] = stream[i];
 * for (i = k; stream != null; i++) {
 *     p = random(0, i);
 *     if (p < k)
 *         reservoir[p] = stream[i];
 * return reservoir;
 * </pre>
 *
 * @author xu.zhang
 */
public class RandomPickIndex {

    int[] nums;
    Random rnd;

    public RandomPickIndex(int[] nums) {
        this.nums = nums;
        this.rnd = new Random();
    }

    public int pick(int target) {
        int result = -1;
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != target)
                continue;
            // 左开右闭
            if (rnd.nextInt(++count) == 0)
                result = i;
        }

        return result;
    }

}
