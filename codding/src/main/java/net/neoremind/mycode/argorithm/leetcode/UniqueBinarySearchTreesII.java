package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;

/**
 * Given an integer n, generate all structurally unique BST's (binary search trees) that store values 1...n.
 * <p>
 * For example,
 * Given n = 3, your program should return all 5 unique BST's shown below.
 * <p>
 * 1         3     3      2      1
 * \       /     /      / \      \
 * 3     2     1      1   3      2
 * /     /       \                 \
 * 2     1         2                 3
 * <p>
 * https://discuss.leetcode.com/topic/2940/java-solution-with-dp/2
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/unique-binary-search-trees-ii/
 */
//TODO 思路和unique-binary-search-tree一样，只不过稍微复杂需要输出所有的树
public class UniqueBinarySearchTreesII {

    TreeNode deepCopy(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode tmp = new TreeNode(1);
        tmp.left = deepCopy(root.left);
        tmp.right = deepCopy(root.right);
        return tmp;
    }

    int cur = 0;

    /**
     * in-order traversal
     */
    void setValue(TreeNode root) {
        if (root.left != null) {
            setValue(root.left);
        }
        root.val = cur++;
        if (root.right != null) {
            setValue(root.right);
        }
    }

    public List<TreeNode> generateTrees(int n) {
        if (n <= 0) {
            List<TreeNode> res = new ArrayList<TreeNode>();
            res.add(null);
            return res;
        }

        List<TreeNode>[] dp = new ArrayList[n + 1];
        for (int i = 0; i < n + 1; ++i) {
            dp[i] = new ArrayList<TreeNode>();
        }

        dp[0].add(null);

        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < i; ++j) {
                for (int k = 0; k < dp[j].size(); ++k) {
                    for (int l = 0; l < dp[i - 1 - j].size(); ++l) {
                        TreeNode tmp = new TreeNode(1);
                        tmp.left = deepCopy(dp[j].get(k));
                        tmp.right = deepCopy(dp[i - 1 - j].get(l));
                        dp[i].add(tmp);
                    }
                }
            }
        }

        // 上面只是把树的相对位置摆好了，剩下的就是填1..n的数字
        for (int i = 0; i < dp[n].size(); ++i) {
            cur = 1;
            setValue(dp[n].get(i));
        }
        return dp[n];
    }

}
