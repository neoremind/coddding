package net.neoremind.mycode.argorithm.other;

import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 求最深节点的最小公共父节点
 * <p>
 * http://www.1point3acres.com/bbs/thread-199739-1-1.html
 *
 * @author xu.zhang
 */
public class LCAOfDeepestNode {

    public int lcaBFS(TreeNode root) {
        if (root == null) return -1;
        // <node -> parent node>
        Map<TreeNode, TreeNode> map = new HashMap<TreeNode, TreeNode>();

        TreeNode head = null, tail = null;        // the head and tail node in a level
        Queue<TreeNode> que = new LinkedList<TreeNode>();
        que.offer(root);
        map.put(root, null);
        while (!que.isEmpty()) {
            head = null;
            tail = null;
            int sz = que.size();
            while (sz-- > 0) {
                TreeNode curr = que.poll();
                if (head == null)
                    head = curr;
                if (sz == 0)
                    tail = curr;
                if (curr.left != null) {
                    map.put(curr.left, curr);
                    que.offer(curr.left);
                }
                if (curr.right != null) {
                    map.put(curr.right, curr);
                    que.offer(curr.right);
                }
            }
        }

        while (head != tail) {
            head = map.get(head);
            tail = map.get(tail);
        }
        return head.val;
    }

    @Test
    public void test() {
        /**
         * <pre>
         *      4
         *    /   \
         *   2     6
         *  / \   / \
         * 1   3 5   7
         * </pre>
         * 4
         */
        TreeNode s = TreeNodeHelper.init("4,2,6,1,3,5,7");
        System.out.println(lcaBFS(s));

        /**
         * <pre>
         *      4
         *    /   \
         *   2     6
         *  / \
         * 1   3
         * </pre>
         * 2
         */
        s = TreeNodeHelper.init("4,2,6,1,3,#,#");
        System.out.println(lcaBFS(s));

        /**
         * <pre>
         *      4
         *    /   \
         *   2     6
         *  / \   /
         * 1   3 5
         *    / \
         *   8  9
         * </pre>
         */
        s = TreeNodeHelper.init("4,2,6,1,3,5,#,#,#,8,9");
        System.out.println(lcaBFS(s));
    }

}
