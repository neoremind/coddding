package net.neoremind.mycode.argorithm.leetcode;

/**
 * A linked list is given such that each node contains an additional random pointer which could point to any node in
 * the list or null.
 * <p>
 * Return a deep copy of the list.
 *
 * 方法1：使用map存储老node->新的node的映射。时间O(N)，空间O(N)
 * 方法2：老node中间插入新的node，空间会优化。
 *
 * @author xu.zhang
 */
public class CopyListWithRandomPointer {

//    public RandomListNode copyRandomList(RandomListNode head) {
//        if (head == null) return null;
//
//        Map<RandomListNode, RandomListNode> map = new HashMap<RandomListNode, RandomListNode>();
//
//        // loop 1. copy all the nodes
//        RandomListNode node = head;
//        while (node != null) {
//            map.put(node, new RandomListNode(node.label));
//            node = node.next;
//        }
//
//        // loop 2. assign next and random pointers
//        node = head;
//        while (node != null) {
//            map.get(node).next = map.get(node.next);
//            map.get(node).random = map.get(node.random);
//            node = node.next;
//        }
//
//        return map.get(head);
//    }

}
