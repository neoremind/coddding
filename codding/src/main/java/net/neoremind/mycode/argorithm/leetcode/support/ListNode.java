package net.neoremind.mycode.argorithm.leetcode.support;

public class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    @Override
    public String toString() {
        return "[" + val + "]->" + (next == null ? "NULL" : next.val);
    }
}
