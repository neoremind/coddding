package net.neoremind.mycode.argorithm.other;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.digest.Md5Crypt;

/**
 * 带有虚拟节点的一致性哈希算法
 * <p>
 * http://www.tom-e-white.com/2007/11/consistent-hashing.html
 * <p>
 * http://blog.csdn.net/cywosp/article/details/23397179/
 * <p>
 * 其实仔细想想如果不用红黑树TreeMap做虚拟节点，或者干脆是一个不带有虚拟节点的一致性哈希实现，
 * 用有序数组就够用了，二分查找也是O(logN)，但是对于添加节点和删除节点就不那么自由了。
 */
public class ConsistentHash<T> {

    private final HashFunction hashFunction; // key和node的hash function，一般使用MD5，足够分散
    private final int numberOfReplicas;  // 一个物理节点被复制成多少个虚拟节点
    private final SortedMap<Integer, T> circle = new TreeMap<>();
    //理解上是一个环，但是为了快速查找，使用了红黑树，因为引入了虚拟节点（replica数量会很大），因此线性扫描肯定性能太差了，比如logN的复杂度

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;
        for (T node : nodes) {
            add(node);
        }
    }

    /**
     * 添加机器，也就是节点
     */
    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hash(node.toString() + i), node);
        }
    }

    /**
     * 删除节点
     */
    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(node.toString() + i));
        }
    }

    /**
     * 获取某个对象对应的节点，一个环的末尾就映射到头
     */
    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public void dump() {
        for (Map.Entry<Integer, T> entry : circle.entrySet()) {
            System.out.println(entry.getKey() + "=>" + entry.getValue());
        }
    }

    public static void main(String[] args) {
        Statistic statistic = Statistic.newInstance();

        /**
         * 当replica=2时，非常不均匀。
         * Server A contains 240 objects
         Server B contains 95 objects
         Server C contains 665 objects

         * 当replica=100时，还是比较均匀的。
         * Server A contains 360 objects
         Server B contains 307 objects
         Server C contains 333 objects
         */
        ConsistentHash<String> ch = new ConsistentHash<>(new SimpleHashFunction(), 2, Collections.emptyList());
        ch.add("A");
        ch.add("B");
        ch.add("C");
        ch.dump();

        int count = 1000;
        for (int i = 0; i < count; i++) {
            String key = ch.get(i);
            statistic.incr(key);
        }

        statistic.show();
    }

    /**
     * 统计，每个物理节点对应了多少个key
     */
    static class Statistic {

        public Statistic() {
        }

        public static Statistic newInstance() {
            return new Statistic();
        }

        Map<String, Integer> serverStatistic = new HashMap<>();

        public void incr(String server) {
            serverStatistic.put(server, serverStatistic.getOrDefault(server, 0) + 1);
        }

        public void show() {
            for (Map.Entry<String, Integer> m : serverStatistic.entrySet()) {
                System.out.println("Server " + m.getKey() + " contains " + m.getValue() + " objects");
            }
        }

    }

    interface HashFunction {
        int hash(Object o);
    }

    static class SimpleHashFunction implements HashFunction {

        @Override
        public int hash(Object o) {
            return Md5Crypt.md5Crypt(o.toString().getBytes()).hashCode();
        }
    }

}
