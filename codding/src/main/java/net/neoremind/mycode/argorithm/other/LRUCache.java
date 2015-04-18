package net.neoremind.mycode.argorithm.other;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * ClassName: LRUCache <br/>
 * Function: 线程不安全的LRU缓存，又名Least Recently Used Cache。首先这是一个缓存，利用<code>HashMap</code>
 * 哈希数据结构来存储数据，可以做到O(1)时间复杂度的检索性能；同时，这个哈希表的数量也不是一直增长的，当数据量超过存储阈值时，<br/>
 * 它将近期最少使用数据淘汰出缓存。
 * <p/>
 * 使用
 * 
 * <pre>
 * new LRUCache<K, V>(int capacity);
 * </pre>
 * 
 * 来新建，传入构造参数<tt>capacity</tt>表示存储大小。 <br/>
 * 内部使用哈希表<code>HashMap</code>来存储，同时维护了一个双向链表，表头<tt>head</tt>到表尾<tt>tail</tt> 依次存储最近使用的数据节点<tt>Node</tt>，当有
 * <code>get<code>或者<code>put</code>操作时，会把访问的数据重新置到表头，所以每次插入或者查询的时候，会额外有一个双向链表的移动指针操作，但是时间复杂度不变。当超过阈值时，先从表尾删除节点。
 * 
 * @author Zhang Xu
 */
public class LRUCache<K, V> {

    /**
     * 哈希表
     */
    private Map<K, Node<K, V>> innerHashMap;

    /**
     * 表头
     */
    private Node<K, V> head;

    /**
     * 表尾
     */
    private Node<K, V> tail;

    /**
     * 哈希表最大容量，超过最大容量开始淘汰近期最长时间未使用的数据
     */
    private int capacity;

    /**
     * 填充数据的数量，也就是<code>innerHashMap.size()</code>，保证内存可见性
     */
    private volatile int size;

    /**
     * Creates a new instance of LRUCache.
     * 
     * @param capacity
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        // 防止resize
        innerHashMap = new HashMap<K, Node<K, V>>(capacity + capacity / 3);
    }

    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public V get(K key) {
        Node<K, V> ret = innerHashMap.get(key);
        if (ret != null) {
            moveNode2Head(ret);
            return ret.getValue();
        }
        return null;
    }

    /**
     * 填充键值对数据
     * 
     * @param key
     * @param value
     * @return
     */
    public V put(K key, V value) {
        Node<K, V> e = innerHashMap.get(key);
        if (e == null) {
            e = new Node<K, V>(key, value);
        } else {
            e.setValue(value);
        }
        innerHashMap.put(key, e);
        moveNode2Head(e);
        return value;
    }

    /**
     * 删除某个建
     * 
     * @param key
     * @return
     */
    public V remove(K key) {
        Node<K, V> e = innerHashMap.get(key);
        if (e == null) {
            return null;
        } else {
            deleteNode(e);
            // 删除到什么都不剩了，就把表头表尾都设置为null
            if (e.getPrev() == e && e.getNext() == e) {
                head = null;
                tail = null;
            }
            return innerHashMap.remove(key).getValue();
        }
    }

    /**
     * 删除双向链表的某个节点
     * 
     * @param e
     */
    private void deleteNode(Node<K, V> e) {
        // 如果节点存在，则将节点的前后对接，自己可以被GC回收
        if (e != null && e.getPrev() != null && e.getNext() != null) {
            e.getPrev().setNext(e.getNext());
            e.getNext().setPrev(e.getPrev());
            // 如果节点本身就是表头，则把后一个作为表头
            if (e == head) {
                head = e.getNext();
            }
            // 如果节点本身就是表尾，则把前一个作为表尾
            if (e == tail) {
                tail = e.getPrev();
            }
            size--;
        }
    }

    /**
     * 将节点放到表头
     * 
     * @param e
     */
    private void moveNode2Head(Node<K, V> e) {
        if (head == null) {
            // 当map为空时候的情况
            head = e;
            tail = e;
            e.setNext(tail);
            e.setPrev(tail);
        } else {
            // 如果表中已经存在了这个数据，则先从双向链表中移除该节点
            deleteNode(e);
            // 将节点next当做表头
            e.setNext(head);
            head.setPrev(e);
            // 将表尾prev连接到节点
            e.setPrev(tail);
            tail.setNext(e);
            // 表头置为节点
            head = e;
        }
        size++;
        if (size > capacity) {
            // 删除表尾，将前一个数据做为表尾，连接表尾prev的next为表头，表头next的prev为表尾
            tail.getPrev().setNext(head);
            head.setPrev(tail.getPrev());
            tail = tail.getPrev();
            size--;
        }
    }

    /**
     * getCapacity
     * 
     * @return
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * getSize
     * 
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * 打印LRU缓存信息
     */
    public void printInfo() {
        System.out.println("Inner Map: " + innerHashMap);
        Node<K, V> e = head;
        System.out.print("NodeList:");
        while (e != null) {
            System.out.print(e.getKey());
            if (e == tail) {
                break;
            }
            System.out.print("->");
            e = e.getNext();
        }
        System.out.println("\n=========");
    }

    /**
     * 获取双向链表信息
     * 
     * @return 双向链表从表头到表尾的数据key组成的list
     */
    public ArrayList<String> getNodeListKeys() {
        ArrayList<String> ret = new ArrayList<String>(size);
        Node<K, V> e = head;
        while (e != null) {
            ret.add(e.getKey().toString());
            if (e == tail) {
                break;
            }
            e = e.getNext();
        }
        return ret;
    }

    /**
     * 测试方法
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Init a LRU cache with capacity of 5
        LRUCache<String, String> lruCache = new LRUCache<String, String>(4);

        // Put k1-v1
        lruCache.put("k1", "v1");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k1")));
        lruCache.printInfo();

        // Put k2-v2
        lruCache.put("k2", "v2");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k2", "k1")));
        lruCache.printInfo();

        // Put k3-v3
        lruCache.put("k3", "v3");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k3", "k2", "k1")));
        assertThat(lruCache.getSize(), is(3));
        lruCache.printInfo();

        // Put k4-v4
        lruCache.put("k4", "v4");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k4", "k3", "k2", "k1")));
        lruCache.printInfo();

        // Re-put k2-v22
        lruCache.put("k2", "v22");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k2", "k4", "k3", "k1")));
        lruCache.printInfo();

        // Get k3
        System.out.println(lruCache.get("k3"));
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k3", "k2", "k4", "k1")));
        lruCache.printInfo();

        // Get k2
        System.out.println(lruCache.get("k2"));
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k2", "k3", "k4", "k1")));
        lruCache.printInfo();

        // Get k1
        System.out.println(lruCache.get("k1"));
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k1", "k2", "k3", "k4")));
        lruCache.printInfo();

        // Get k1
        System.out.println(lruCache.get("k1"));
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k1", "k2", "k3", "k4")));
        assertThat(lruCache.getSize(), is(4));
        lruCache.printInfo();

        // Put k5-v5
        lruCache.put("k5", "v5");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k5", "k1", "k2", "k3")));
        assertThat(lruCache.getSize(), is(4));
        lruCache.printInfo();

        // Remove k2
        lruCache.remove("k2");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k5", "k1", "k3")));
        assertThat(lruCache.getSize(), is(3));

        // Put k6-v6
        lruCache.put("k6", "v6");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k6", "k5", "k1", "k3")));
        assertThat(lruCache.getSize(), is(4));
        lruCache.printInfo();

        // Get k1
        String valueK1 = lruCache.get("k99");
        System.out.println(valueK1);
        assertThat(valueK1, nullValue());
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k6", "k5", "k1", "k3")));
        assertThat(lruCache.getSize(), is(4));
        lruCache.printInfo();

        // Remove k2
        lruCache.remove("k999");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k6", "k5", "k1", "k3")));
        assertThat(lruCache.getSize(), is(4));
        lruCache.printInfo();

        // Remove k6
        lruCache.remove("k6");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k5", "k1", "k3")));
        assertThat(lruCache.getSize(), is(3));
        lruCache.printInfo();

        // Remove k3
        lruCache.remove("k3");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k5", "k1")));
        assertThat(lruCache.getSize(), is(2));
        lruCache.printInfo();

        // Remove k5
        lruCache.remove("k5");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k1")));
        assertThat(lruCache.getSize(), is(1));
        lruCache.printInfo();

        // Remove k1
        lruCache.remove("k1");
        assertThat(lruCache.getNodeListKeys(), is(new ArrayList<String>(0)));
        assertThat(lruCache.getSize(), is(0));
        lruCache.printInfo();

        // Remove k9999
        lruCache.remove("k9999");
        assertThat(lruCache.getNodeListKeys(), is(new ArrayList<String>(0)));
        assertThat(lruCache.getSize(), is(0));
        lruCache.printInfo();

        // Get k0
        System.out.println(lruCache.get("k0"));
        assertThat(lruCache.getNodeListKeys(), is(new ArrayList<String>(0)));
        assertThat(lruCache.getSize(), is(0));
        lruCache.printInfo();

        // Put k0-v0
        lruCache.put("k0", "v0");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k0")));
        assertThat(lruCache.getSize(), is(1));
        lruCache.printInfo();

        // Put k00-v00
        lruCache.put("k00", "v00");
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k00", "k0")));
        assertThat(lruCache.getSize(), is(2));
        lruCache.printInfo();

        // Get k0
        String valueK0 = lruCache.get("k0");
        System.out.println(valueK0);
        assertThat(valueK0, is("v0"));
        assertThat(lruCache.getNodeListKeys(), is(Lists.newArrayList("k0", "k00")));
        assertThat(lruCache.getSize(), is(2));
        lruCache.printInfo();
    }

    /**
     * 双向链表Node节点
     */
    @SuppressWarnings("hiding")
    class Node<K, V> {

        /**
         * 键
         */
        private K key;

        /**
         * 值
         */
        private V value;

        /**
         * 前一个节点
         */
        private Node<K, V> prev;

        /**
         * 后一个节点
         */
        private Node<K, V> next;

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[key:");
            sb.append(key);
            sb.append(",value:");
            sb.append(value);
            sb.append(",prev:");
            sb.append(prev.getKey());
            sb.append(",next:");
            sb.append(next.getKey());
            sb.append("]");
            return sb.toString();
        }

        /**
         * Creates a new instance of Node.
         * 
         * @param key
         * @param value
         */
        public Node(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

        // getter and setter

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getPrev() {
            return prev;
        }

        public void setPrev(Node<K, V> prev) {
            this.prev = prev;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

    }

}
