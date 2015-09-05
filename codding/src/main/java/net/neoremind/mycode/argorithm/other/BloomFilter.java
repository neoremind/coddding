package net.neoremind.mycode.argorithm.other;

import java.util.BitSet;

/**
 * A Bloom filter is a space-efficient <b>probabilistic data structure</b>, conceived by Burton Howard Bloom in 1970,
 * that is used to test whether an element is a member of a set. False positive matches are possible, but false
 * negatives are not, thus a Bloom filter has a 100% recall rate. In other words, a query returns either "possibly in
 * set" or "definitely not in set". Elements can be added to the set, but not removed (though this can be addressed
 * with a "counting" filter). The more elements that are added to the set, the larger the probability of false
 * positives.
 * <p/>
 * <p/>
 * 关键词：布隆过滤器实际上是一个很长的二进制向量和一系列随机映射函数False postive（误判）；100%召回率；支持可能存在，绝对没有。
 * <p/>
 * Bloom-Filter一般用于在大数据量的集合中判定某元素是否存在。例如邮件服务器中的垃圾邮件过滤器。在搜索引擎领域，Bloom-Filter最常用于网络蜘蛛(Spider)的URL过滤，网络蜘蛛通常有一个
 * URL列表，保存着将要下载和已经下载的网页的URL，网络蜘蛛下载了一个网页，从网页中提取到新的URL后，需要判断该URL是否已经存在于列表中。此时，Bloom-Filter算法是最好的选择。
 * 比如说，一个象 Yahoo,Hotmail 和 Gmai 那样的公众电子邮件（email）提供商，总是需要过滤来自发送垃圾邮件的人（spamer）的垃圾邮件。一个办法就是记录下那些发垃圾邮件的 email
 * 地址。由于那些发送者不停地在注册新的地址，全世界少说也有几十亿个发垃圾邮件的地址，将他们都存起来则需要大量的网络服务器。
 * <p/>
 * 假定我们存储一亿个电子邮件地址，我们先建立一个十六亿二进制（比特），即两亿字节的向量，然后将这十六亿个二进制位全部设置为零。对于每一个电子邮件地址 X，我们用八个不同的随机数产生器（F1,F2, ...,F8）
 * 产生八个信息指纹（f1, f2, ..., f8）。再用一个随机数产生器 G 把这八个信息指纹映射到 1 到十六亿中的八个自然数 g1, g2, ...,g8。现在我们把这八个位置的二进制位全部设置为一。当我们对这一亿个
 * email 地址都进行这样的处理后。一个针对这些 email 地址的布隆过滤器就建成了。（见下图）   现在，让我们看看如何用布隆过滤器来检测一个可疑的电子邮件地址 Y 是否在黑名单中。我们用相同的八个随机数产生器（F1,
 * F2, ..., F8）对这个地址产生八个信息指纹 s1,s2,...,s8，然后将这八个指纹对应到布隆过滤器的八个二进制位，分别是 t1,t2,...,t8。如果 Y 在黑名单中，显然，t1,t2,..,t8
 * 对应的八个二进制一定是一。这样在遇到任何在黑名单中的电子邮件地址，我们都能准确地发现。
 * <p/>
 * 布隆过滤器决不会漏掉任何一个在黑名单中的可疑地址。但是，它有一条不足之处。也就是它有极小的可能将一个不在黑名单中的电子邮件地址判定为在黑名单中，因为有可能某个好的邮件地址正巧对应八个都被设置成一的二进制位。好在这种可能性很小。我们把它称为误识概率。在上面的例子中，误识概率在万分之一以下。
 * <a href="http://pages.cs.wisc.edu/~cao/papers/summary-cache/node8.html">关于False Positive的一个数学分析</a>
 * <p/>
 * 根据参考文献，哈希函数个数k取10，位数组大小m设为字符串个数n的20倍时，false positive发生的概率是0.0000889 ，这个概率基本能满足网络爬虫的需求了。
 * <p/>
 * 布隆过滤器的好处在于快速，省空间。但是有一定的误识别率。
 * <p/>
 * 关于哈希函数的选择没有细致研究：http://blog.sina.com.cn/s/blog_7fb536190100zhmr.html
 *
 * @author zhangxu
 */
public class BloomFilter {

    private static final int DEFAULT_SIZE = 2 << 24;

    private static final int[] seeds = new int[] {5, 7, 11, 13, 31, 37, 61};

    private BitSet bits = new BitSet(DEFAULT_SIZE);

    private SimpleHash[] func = new SimpleHash[seeds.length];

    public BloomFilter(int bitSize) {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(bitSize, seeds[i]);
        }
    }

    public BloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    //内部类，simpleHash
    public static class SimpleHash {
        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }

    public static void main(String[] args) {
        String value = "neoremind@163.com";
        BloomFilter filter = new BloomFilter();
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));

        // 模拟false postive，当比特位很少时，发生的概率很高
        filter = new BloomFilter(20);
        filter.add("hehe@gmail.com");
        filter.add("hah@gmail.com");
        filter.add("cute@foxmail.com");
        System.out.println(filter.contains("hehe2@gmail.com"));
    }

}