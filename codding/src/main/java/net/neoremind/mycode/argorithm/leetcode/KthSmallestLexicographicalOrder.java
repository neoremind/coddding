package net.neoremind.mycode.argorithm.leetcode;

import java.util.List;

import org.junit.Test;

/**
 * Given integers n and k, find the lexicographically k-th smallest integer in the range from 1 to n.
 * <p>
 * Note: 1 ≤ k ≤ n ≤ 109.
 * <p>
 * Example:
 * <p>
 * Input:
 * n: 13   k: 2
 * <p>
 * Output:
 * 10
 * <p>
 * Explanation:
 * The lexicographical order is [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9], so the second smallest number is 10.
 * <p>
 * https://leetcode.com/problems/k-th-smallest-in-lexicographical-order/
 * <p>
 * https://discuss.leetcode.com/topic/64624/concise-easy-to-understand-java-5ms-solution-with-explaination
 * <p>
 * Initially, image you are at node 1 (variable: curr),
 * the goal is move (k - 1) steps to the target node x. (substract steps from k after moving)
 * when k is down to 0, curr will be finally at node x, there you get the result.
 * <p>
 * we don't really need to do a exact k steps preorder traverse of the denary tree, the idea is to calculate the
 * steps between curr and curr + 1 (neighbor nodes in same level), in order to skip some unnecessary moves.
 * <p>
 * Main function
 * Firstly, calculate how many steps curr need to move to curr + 1.
 * <p>
 * if the steps <= k, we know we can move to curr + 1, and narrow down k to k - steps.
 * <p>
 * else if the steps > k, that means the curr + 1 is actually behind the target node x in the preorder path, we can't
 * jump to curr + 1. What we have to do is to move forward only 1 step (curr * 10 is always next preorder node) and
 * repeat the iteration.
 * <p>
 * calSteps function
 * <p>
 * how to calculate the steps between curr and curr + 1?
 * Here we come up a idea to calculate by level.
 * Let n1 = curr, n2 = curr + 1.
 * n2 is always the next right node beside n1's right most node (who shares the same ancestor "curr")
 * (refer to the pic, 2 is right next to 1, 20 is right next to 19, 200 is right next to 199).
 * <p>
 * so, if n2 <= n, what means n1's right most node exists, we can simply add the number of nodes from n1 to n2 to steps.
 * <p>
 * else if n2 > n, what means n (the biggest node) is on the path between n1 to n2, add (n + 1 - n1) to steps.
 * <p>
 * organize this flow to "steps += Math.min(n + 1, n2) - n1; n1 *= 10; n2 *= 10;"
 *
 * @author zhangxu
 */
public class KthSmallestLexicographicalOrder {

    /**
     * 非递归实现
     */
    public int findKthNumber(int n, int k) {
        int curr = 1;
        k = k - 1;
        while (k > 0) {
            int count = countNumber(n, curr, curr + 1);
            if (count <= k) {
                curr += 1;
                k -= count;
            } else {
                curr *= 10;
                k -= 1;
            }
        }
        return curr;
    }

    /**
     * 递归
     * <p>
     * 想法很简单，一层一层的count，先count 1下面的所有数量，如果小于k，那么就递归的跳过k-count，同时curr变成下一个是2.
     * <p>
     * backtrack的终止条件是k==1
     */
    public int findKthNumber2(int n, int k) {
        return helper(n, k, 1);
    }

    int helper(int n, int k, int curr) {
        if (k == 1) {
            return curr;
        }
        int count = countNumber(n, curr, curr + 1);
        if (count <= k) {
            return helper(n, k - count, curr + 1);
        } else {
            return helper(n, k - 1, curr * 10);
        }
    }

    /**
     * 必须为long，避免溢出
     */
    int countNumber(int n, long left, long right) {
        int count = 0;
        while (left <= n) {
            count += Math.min(n + 1, right) - left;
            left *= 10;
            right *= 10;
        }
        return count;
    }

    @Test
    public void test() {
        System.out.println(findKthNumber(199, 2));
        System.out.println(findKthNumber2(199, 2));
        System.out.println(findKthNumber(199, 121));
        System.out.println(findKthNumber2(199, 121));
        System.out.println(findKthNumber2(100000, 67543));
        System.out.println(findKthNumber2(100000, 67543));
    }

}
