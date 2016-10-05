package net.neoremind.mycode.argorithm.leetcode;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class LRUCacheDesign {

    @Test
    public void test() {
        LRUCache lruCache = new LRUCache(3);
        lruCache.set(1, 1);
        lruCache.set(2, 2);
        lruCache.set(3, 3);
        assertThat(lruCache.get(1), is(1));
        assertThat(lruCache.get(2), is(2));
        assertThat(lruCache.get(3), is(3));
        lruCache.dump();
        lruCache.set(4, 4);
        lruCache.dump();
        assertThat(lruCache.get(1), is(-1));
        assertThat(lruCache.get(4), is(4));

        //2,[set(2,1),set(2,2),get(2),set(1,1),set(4,1),get(2)]
        lruCache = new LRUCache(2);
        lruCache.set(2, 1);
        lruCache.set(2, 2);
        assertThat(lruCache.get(2), is(2));
        lruCache.dump();
        lruCache.set(1, 1);
        lruCache.set(4, 1);
        assertThat(lruCache.get(2), is(-1));

        //2,[set(2,1),set(1,1),get(2),set(4,1),get(1),get(2)]
        lruCache = new LRUCache(2);
        lruCache.set(2, 1);
        lruCache.set(1, 1);
        assertThat(lruCache.get(2), is(1));
        lruCache.dump();
        lruCache.set(4, 1);
        assertThat(lruCache.get(1), is(-1));
        assertThat(lruCache.get(2), is(1));

        //2,[set(2,1),set(3,2),get(3),get(2),set(4,3),get(2),get(3),get(4)]
        lruCache = new LRUCache(2);
        lruCache.set(2, 1);
        lruCache.set(3, 2);
        assertThat(lruCache.get(3), is(2));
        lruCache.dump();
        lruCache.set(4, 3);
        assertThat(lruCache.get(2), is(-1));
        assertThat(lruCache.get(3), is(2));
        assertThat(lruCache.get(4), is(3));
    }

    public class LRUCache {

        Node head = null;
        Node tail = null;
        int capacity = 0;
        Map<Integer, Node> map;
        int size = 0;

        public LRUCache(int capacity) {
            // assume capacity > 0
            this.capacity = capacity;
            map = new HashMap<>(capacity / 3 * 4);
        }

        public int get(int key) {
            Node target = map.get(key);
            if (target == null) {
                return -1;
            }
            moveAhead(target);
            return target.value;
        }

        public void set(int key, int value) {
            if (map.containsKey(key)) {
                Node node = map.get(key);
                node.value = value;
                moveAhead(node);
            } else {
                Node node = new Node(key, value);
                map.put(key, node);
                size++;  // size++在这里
                if (head == null && tail == null) {  //为空时候特殊处理
                    head = node;
                    tail = node;
                    tail.next = head;
                    head.prev = tail;
                    head.next = tail;
                    tail.prev = head;
                } else {
                    node.prev = tail;
                    node.next = head;
                    tail.next = node;
                    head.prev = node;
                    head = node;
                    if (size > capacity) {
                        deleteLRU();
                    }
                }
            }
        }

        void moveAhead(Node target) {
            if (target == head) {  //处理在对头和队尾两种特殊情况
                return;
            }
            if (tail == target) {
                tail = target.prev;
                head = target;
                return;
            }
            target.prev.next = target.next;
            target.next.prev = target.prev;
            tail.next = target;
            target.prev = tail;
            head.prev = target;
            target.next = head;
            head = target;
        }

        void deleteLRU() {
            Node t = tail;
            t.prev.next = head;
            head.prev = t.prev;
            tail = t.prev;
            size--;
            map.remove(t.key);
        }

        class Node {
            int key;
            int value;
            Node prev;
            Node next;

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        public void dump() {
            if (head == null) {
                return;
            }
            StringBuilder ret = new StringBuilder();
            Node node = head;
            while (node != null) {
                ret.append(node.value);
                if (node == tail) {
                    ret.append("->LOOP");
                    break;
                } else {
                    ret.append("->");
                }
                node = node.next;
            }
            System.out.println(ret.toString());
        }
    }

}
