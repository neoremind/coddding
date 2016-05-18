package net.neoremind.mycode.argorithm.hash;

/**
 * 自行完成的哈希散列数据结构，只是简单版本，仅提供<code>get</code>和<code>put</code>方法，和{@link HashMap}的实现类似
 * <p/>
 * 特点如下：
 * <ul>
 * <li>1. 非线程安全，若要安全的可以参考支持并发的{@link java.util.concurrent.ConcurrentHashMap}以及粗粒度锁的{@link java.util.Collections
 * .SynchronizedMap}</li>
 * <li>2. Key不允许为null，这点与HashMap不同，</li>
 * <li>3. 不保证元素顺序，put、get操作时间复杂度为O(1)</li>
 * <li>4. 散列表的实质就是一个数组槽（叫做bucket或者slot），每个槽放Entry，这个Entry包含Key，Value以及next指针，指向下一个Entry，可以把每个槽看做是一个链表</li>
 * <li>5. 为保证散列表性能，也就是put、get操作性能，当超过<tt>capacity * loadFactor</tt>时，（平衡因子默认为0
 * .75），自动扩容rehash，rehash会重新打乱元素，原来在A槽的元素现在可以在A'槽，原来共处一个槽的两个元素，可能各奔东西</li>
 * </ul>
 * <p/>
 * 关于散列函数：<br/>
 * 该散列表不同于HashMap的是支持自定义hash function，哈希函数对于散列性能至关重要，选择不好的哈希函数，会影响性能，严重时会退化为链表，因此JDK默认的实现是不暴露给外部的。
 * <p/>
 * 在哈希散列表容量（buckets或者slots）为length的情况下，为了使每个key都能在冲突（collision）最小的情况下映射到[0, length)（注意是左闭右开）的索引（index）内，一般有两种做法：
 * <ul>
 * <li>1. 让length为素数，然后hash(key) % length，线程安全的hashtable使用的是这种方式，留个//TODO为什么是素数?</li>
 * <li>2. 让length为2的指数倍（power of two），HashMap用这种，并且默认HashMap大小为1<<4=16个槽，然后hash(key) & (length - 1)，很好理解power of 2
 * - 1的值的比特位全是1，和某个hash(key)做与运算，自然就是槽的索引。关于JDK的实现有个trick是当length=16的时候，hashcode
 * 只有低位能够算，造成了冲突可能性加剧，因此做了一系列的异或运算，无符号右移20，12，7，4步，也就是将高低位混合起来运算，详细见{@link JdkWayHashStrategy}。</li>
 * </ul>
 * <p/>
 * 本例中默认使用{@link SimpleHashStrategy}，他只用泛型K类型的hashcode，也可以通过构造函数{@link #MyHashMap(int, HashStrategy)}指定哈希策略。
 * <p/>
 * 例如String字符串的hashcode用了Horner法则，hk = k0 + 37*k1 + 37^2*k2...。
 * <p/>
 * 关于解决冲突：
 * 有两个方法:
 * <ul>
 * <li>1. 分离链接法（seperate chaining），默认jdk使用</li>
 * <li>2. 探测法（probing hash table），有线性探测，会产生一次聚集问题，可以用平方探测来解决，还有双散列？</li>
 * <li>3. 再散列</li>
 * </ul>
 *
 * @author zhangxu
 */
public class MyHashMap<K, V> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    private int size;

    private int length;

    private final float loadFactor = 0.75f;

    private Entry<K, V>[] table;

    private HashStrategy<K> hashStrategy;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MyHashMap(int intiCapacity) {
        this(intiCapacity, new SimpleHashStrategy<K>());
    }

    public MyHashMap(int intiCapacity, HashStrategy<K> hashStrategy) {
        this.length = intiCapacity;
        table = new Entry[intiCapacity];
        this.hashStrategy = hashStrategy;
    }

    public int length() {
        return length;
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key should not be NULL!");
        }

        if (++size > length * loadFactor) {
            rehash();
        }

        int hash = hash(key);
        int bucketIndex = bucketIndexFor(hash);

        for (Entry<K, V> e = table[bucketIndex]; e != null; e = e.next) {
            if (e.key == key || e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;  // return old value
            }
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[bucketIndex];
        table[bucketIndex] = newEntry;
        return value;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Key should not be NULL!");
        }
        int hash = hash(key);
        int bucketIndex = bucketIndexFor(hash);

        for (Entry<K, V> e = table[bucketIndex]; e != null; e = e.next) {
            if (e.key == key || e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    private void rehash() {
        Entry<K, V>[] oldTable = table;
        this.length *= 2;
        table = new Entry[this.length];
        for (Entry<K, V> e : oldTable) {
            for (; e != null; e = e.next) {
                int hash = hash(e.key);
                int bucketIndex = bucketIndexFor(hash);
                table[bucketIndex] = new Entry<>(e.key, e.value, table[bucketIndex]);
            }
        }
    }

    private int hash(K key) {
        return hashStrategy.hash(key);
    }

    private int bucketIndexFor(int hash) {
        return hashStrategy.indexFor(hash, length);
    }

    public void printViewable() {
        for (int i = 0; i < length; i++) {
            if (table[i] == null) {
                System.out.println("|" + i + "|");
                continue;
            }
            System.out.print("|" + i + "|");
            for (Entry<K, V> e = table[i]; e != null; e = e.next) {
                System.out.print(String.format("->%s(%s|%s)", e.key, hash(e.key), bucketIndexFor(hash(e.key))));
            }
            System.out.print("->NULL");
            System.out.println();
        }
    }

}

class Entry<K, V> {

    K key;

    V value;

    Entry<K, V> next;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Entry(K key, V value, Entry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}

/**
 * 简单的取模运算哈希
 */
class SimpleHashStrategy<K> implements HashStrategy<K> {

    @Override
    public int hash(K key) {
        return key.hashCode();
    }

    @Override
    public int indexFor(int hash, int length) {
        return hash % length;
    }
}

/**
 * 模拟不使用异或混合的标准jdk的strategy，会发现一些情况下容器比较小的情况下比如默认16，
 * 对于32位int hash值低位相同的key，其冲突的情况会很不好，造成一个链表
 *
 * @param <K>
 */
class NotNorJdkWayHashStrategy<K> implements HashStrategy<K> {

    @Override
    public int hash(K key) {
        return key.hashCode();
    }

    @Override
    public int indexFor(int hash, int length) {
        return hash & (length - 1);
    }
}

/**
 * jdk的实现
 */
class JdkWayHashStrategy<K> implements HashStrategy<K> {

    int hashSeed = 0;

    /**
     * Retrieve object hash code and applies a supplemental hash function to the
     * result hash, which defends against poor quality hash functions.  This is
     * critical because HashMap uses power-of-two length hash tables, that
     * otherwise encounter collisions for hashCodes that do not differ
     * in lower bits. Note: Null keys always map to hash 0, thus index 0.
     */
    @Override
    public int hash(K key) {
        int h = hashSeed;
        if (0 != h && key instanceof String) {
            // only apply for JAVA7 or before!!!!!!! so comment here
            //return sun.misc.Hashing.stringHash32((String) key);
            return 0;
        }

        h ^= key.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    @Override
    public int indexFor(int hash, int length) {
        return hash & (length - 1);
    }
}