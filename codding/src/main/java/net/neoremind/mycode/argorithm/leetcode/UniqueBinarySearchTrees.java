package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given n, how many structurally unique BST's (binary search trees) that store values 1...n?
 * <p>
 * For example,
 * Given n = 3, there are a total of 5 unique BST's.
 * <p>
 * 1         3     3      2      1
 * \       /     /      / \      \
 * 3     2     1      1   3      2
 * /     /       \                 \
 * 2     1         2                 3
 * <p>
 * https://discuss.leetcode.com/topic/37310/fantastic-clean-java-dp-solution-with-detail-explaination
 * https://discuss.leetcode.com/topic/5673/dp-problem-10-lines-with-comments
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/unique-binary-search-trees/
 */
public class UniqueBinarySearchTrees {

    /**
     * Taking 1~n as root respectively:
     * 1 as root: # of trees = F(0) * F(n-1)  // F(0) == 1
     * 2 as root: # of trees = F(1) * F(n-2)
     * 3 as root: # of trees = F(2) * F(n-3)
     * ...
     * n-1 as root: # of trees = F(n-2) * F(1)
     * n as root:   # of trees = F(n-1) * F(0)
     * <p>
     * So, the formulation is:
     * F(n) = F(0) * F(n-1) + F(1) * F(n-2) + F(2) * F(n-3) + ... + F(n-2) * F(1) + F(n-1) * F(0)
     * <p>
     * <p>
     * F(0) = 1
     * F(1) = 1
     * F(2) = F(0)*F(1) + F(1)*F(0) = 1*1 + 1*1 = 2
     * F(3) = F(0)*F(2) + F(1)*F(1) + F(2)*F(0) = 1*2 + 1*1 + 1*2 = 5
     * F(4) = F(0)*F(3) + F(1)*F(2) + F(2)*F(1) + F(0)*F(3) = 1*5 + 1*2 + 2*1 + 5*1 = 14
     * F(5) = F(0)*F(4) + F(1)*F(3) + F(2)*F(2) + F(3)*F(1) + F(4)*F(0) = 1*14 + 1*5 + 2*2 + 5*1 * 14*1 = 42
     */
    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = 0;
            for (int j = 1; j <= i; j++) {
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }

    /**
     * <pre>
     * n=1
     * 1
     * </pre>
     * <pre>
     * n=2
     *   1     2
     *  /       \
     * 2         1
     * </pre>
     * <pre>
     * n=3
     * 左边一个没有，j=1
     *   1      1
     *    \      \
     *     2      3
     *      \    /
     *       3  2
     *
     * 左边有一个，j=2
     *   2
     *  / \
     * 1   3
     * 左边有两个，j=3，和j=1的情况一样，对称了！
     *      3     3
     *     /     /
     *    2     1
     *   /      \
     *  1        2
     * </pre>
     */
    @Test
    public void test() {
        assertThat(numTrees(1), is(1));
        assertThat(numTrees(2), is(2));
        assertThat(numTrees(3), is(5));
        assertThat(numTrees(4), is(14));
        assertThat(numTrees(5), is(42));
    }
}
