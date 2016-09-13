package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.neoremind.mycode.argorithm.leetcode.support.TreeLinkNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNode;
import net.neoremind.mycode.argorithm.leetcode.support.TreeNodeHelper;

/**
 * Given a binary tree
 * <p>
 * struct TreeLinkNode {
 * TreeLinkNode *left;
 * TreeLinkNode *right;
 * TreeLinkNode *next;
 * }
 * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer
 * should be set to NULL.
 * <p>
 * Initially, all next pointers are set to NULL.
 * <p>
 * Note:
 * <p>
 * You may only use constant extra space.
 * You may assume that it is a perfect binary tree (ie, all leaves are at the same level, and every parent has two
 * children).
 * For example,
 * Given the following perfect binary tree,
 * <pre>
 *     1
 *    /  \
 *   2    3
 *  / \  / \
 * 4  5  6  7
 * </pre>
 * After calling your function, the tree should look like:
 * <pre>
 *     1 -> NULL
 *    /  \
 *   2 -> 3 -> NULL
 *  / \  / \
 * 4->5->6->7 -> NULL
 *  </pre>
 *
 * {@link PopulatingNextRightPointersInEachNodeII}一样的更加通用的做法
 *
 * @author zhangxu
 * @see https://leetcode.com/problems/populating-next-right-pointers-in-each-node/
 */
public class PopulatingNextRightPointersInEachNode {

}
