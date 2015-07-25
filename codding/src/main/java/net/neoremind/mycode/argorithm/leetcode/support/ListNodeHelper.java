package net.neoremind.mycode.argorithm.leetcode.support;

/**
 *
 */
public class ListNodeHelper {

    public static void main(String[] args) {
        ListNode head = build(new int[] {2, 6, 3, 9});
        System.out.println(getPrintableListNode(head));
    }

    /**
     * Build a list from an array and returns the head node
     *
     * @param nums int array
     *
     * @return head
     */
    public static ListNode build(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        ListNode head = new ListNode(nums[0]);
        ListNode node = head;
        for (int i = 1; i < nums.length; i++) {
            ListNode next = new ListNode(nums[i]);
            node.next = next;
            node = next;
        }
        return head;
    }

    /**
     * Print out the list from the head node
     *
     * @param head head node
     *
     * @return list string
     */
    public static String getPrintableListNode(ListNode head) {
        if (head == null) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        ListNode node = head;
        while (node != null) {
            ret.append(node.val);
            if (node.next != null) {
                ret.append("->");
            } else {
                ret.append("->NULL");
            }
            node = node.next;
        }
        return ret.toString();
    }

}
