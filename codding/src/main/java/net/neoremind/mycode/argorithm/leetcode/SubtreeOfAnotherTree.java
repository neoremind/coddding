package net.neoremind.mycode.argorithm.leetcode;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Given two non-empty binary trees s and t, check whether tree t has exactly the same structure and node values with
 * a subtree of s. A subtree of s is a tree consists of a node in s and all of this node's descendants. The tree s
 * could also be considered as a subtree of itself.
 * <p>
 * Example 1:
 * Given tree s:
 * <p>
 * 3
 * / \
 * 4   5
 * / \
 * 1   2
 * Given tree t:
 * 4
 * / \
 * 1   2
 * Return true, because t has the same structure and node values with a subtree of s.
 * Example 2:
 * Given tree s:
 * <p>
 * 3
 * / \
 * 4   5
 * / \
 * 1   2
 * /
 * 0
 * Given tree t:
 * 4
 * / \
 * 1   2
 * Return false.
 *
 * @author xu.zhang
 */
public class SubtreeOfAnotherTree {

    public boolean isSubtree(TreeNode s, TreeNode t) {
        if (s != null) {
            if (sameTree(s, t) || isSubtree(s.left, t) || isSubtree(s.right, t)) {
                return true;
            }
        }
        return false;
    }

    public boolean sameTree(TreeNode s, TreeNode t) {
        if (s == null || t == null) {
            return s == t;
        }
        return s.val == t.val && sameTree(s.left, t.left) && sameTree(s.right, t.right);
    }

    /**
     * <pre>
     *      4
     *    /   \
     *   2     6
     *  / \   / \
     * 1   3 5   7
     * </pre>
     */
    @Test
    public void test() {
        TreeNode s = TreeNodeHelper.init("4,2,6,1,3,5,7");
        TreeNode t = TreeNodeHelper.init("2,1,3");
        assertThat(isSubtree(s, t), Matchers.is(true));

        t = TreeNodeHelper.init("3");
        assertThat(isSubtree(s, t), Matchers.is(true));

        t = TreeNodeHelper.init("1,2,3");
        assertThat(isSubtree(s, t), Matchers.is(false));

        s = TreeNodeHelper.init("1,2,3");
        t = TreeNodeHelper.init("1,2");
        assertThat(isSubtree(s, t), Matchers.is(true));
    }

}
