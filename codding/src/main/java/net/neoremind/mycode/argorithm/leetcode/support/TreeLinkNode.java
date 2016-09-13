package net.neoremind.mycode.argorithm.leetcode.support;

public class TreeLinkNode {

    public int val;

    public TreeLinkNode left;

    public TreeLinkNode right;

    public TreeLinkNode next;

    public TreeLinkNode(int x) {
        val = x;
    }

    @Override
    public String toString() {
        return "[" + val + "]";
    }

}
