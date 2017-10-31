package net.neoremind.mycode.argorithm.leetcode;

import org.junit.Test;

import java.util.ArrayList;

/**
 * You have 4 cards each containing a number from 1 to 9. You need to judge whether they could operated through *, /,
 * +, -, (, ) to get the value of 24.
 * <p>
 * Example 1:
 * Input: [4, 1, 8, 7]
 * Output: True
 * Explanation: (8-4) * (7-1) = 24
 * Example 2:
 * Input: [1, 2, 1, 2]
 * Output: False
 * Note:
 * The division operator / represents real division, not integer division. For example, 4 / (1 - 2/3) = 12.
 * Every operation done is between two numbers. In particular, we cannot use - as a unary operator. For example, with
 * [1, 1, 1, 1] as input, the expression -1 - 1 - 1 - 1 is not allowed.
 * You cannot concatenate numbers together. For example, if the input is [1, 2, 1, 2], we cannot write this as 12 + 12.
 * <p>
 * There are only 4 cards and only 4 operations that can be performed. Even when all operations do not commute, that
 * gives us an upper bound of 12 * 6 * 2 * 4 * 4 * 4 = 921612∗6∗2∗4∗4∗4=9216 possibilities, which makes it feasible
 * to just try them all. Specifically, we choose two numbers (with order) in 12 ways and perform one of 4 operations
 * (12 * 4). Then, with 3 remaining numbers, we choose 2 of them and perform one of 4 operations (6 * 4). Finally we
 * have two numbers left and make a final choice of 2 * 4 possibilities.
 * <p>
 * We will perform 3 binary operations (+, -, *, / are the operations) on either our numbers or resulting numbers.
 * Because - and / do not commute, we must be careful to consider both a / b and b / a.
 * <p>
 * For every way to remove two numbers a, b in our list, and for each possible result they can make, like a+b, a/b,
 * etc., we will recursively solve the problem on this smaller list of numbers.
 * <p>
 * 总共有9216种可能，所以就DFS的方式解决。
 * 找两个数字做+、-、*、/，合并成一个数字，然后对这剩下的3个数字再做同样的操作，直到只剩下一个数字，看是否等于24.
 *
 * @author xu.zhang
 * @see https://leetcode.com/problems/24-game/description/
 */
public class Game24 {

    public boolean judgePoint24(int[] nums) {
        ArrayList A = new ArrayList<Double>();
        for (int v : nums) A.add((double) v);
        return solve(A);
    }

    private boolean solve(ArrayList<Double> nums) {
        if (nums.size() == 0) return false;
        if (nums.size() == 1) return Math.abs(nums.get(0) - 24) < 1e-6;

        for (int i = 0; i < nums.size(); i++) {
            for (int j = 0; j < nums.size(); j++) {
                if (i != j) {
                    ArrayList<Double> nums2 = new ArrayList<Double>();
                    for (int k = 0; k < nums.size(); k++) {
                        if (k != i && k != j) {
                            nums2.add(nums.get(k));
                        }
                    }
                    for (int k = 0; k < 4; k++) {
                        if (k == 0) nums2.add(nums.get(i) + nums.get(j));
                        if (k == 1) nums2.add(nums.get(i) * nums.get(j));
                        if (k == 2) nums2.add(nums.get(i) - nums.get(j));
                        if (k == 3) {
                            if (nums.get(j) != 0) {
                                nums2.add(nums.get(i) / nums.get(j));
                            } else {
                                continue;
                            }
                        }
                        if (solve(nums2)) return true;
                        nums2.remove(nums2.size() - 1);
                    }
                }
            }
        }
        return false;
    }

    @Test
    public void test() {

    }

}
