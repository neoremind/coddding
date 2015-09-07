package net.neoremind.mycode.argorithm.hash;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class MyHashMapTest {

    @Test
    public void testPutGet() {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("abc", "123");
        map.put("mmm", "456");
        map.put("zzz", "999");
        map.put("xxx", null);
        assertThat(map.get("abc"), Matchers.is("123"));
        assertThat(map.get("mmm"), Matchers.is("456"));
        assertThat(map.get("zzz"), Matchers.is("999"));
        assertThat(map.get("xxx"), Matchers.nullValue());
        assertThat(map.get("uuu"), Matchers.nullValue());
        map.printViewable();
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put(null, "123");
    }

    /**
     * 扩容前：
     * <pre>
     * |0|->bbb(97314|0)->NULL
     * |1|->aaa(96321|1)->NULL
     * |2|
     * |3|->fff(101286|3)->NULL
     * |4|->eee(100293|4)->NULL
     * |5|->ddd(99300|5)->NULL
     * |6|->ccc(98307|6)->NULL
     * |7|
     * </pre>
     * <p/>
     * 扩容后：
     * <pre>
     * |0|->ddd(99300|0)->NULL
     * |1|
     * |2|
     * |3|->eee(100293|3)->NULL
     * |4|
     * |5|
     * |6|->zzz(121146|6)->fff(101286|6)->aaa(96321|6)->NULL
     * |7|
     * |8|
     * |9|->bbb(97314|9)->NULL
     * |10|
     * |11|
     * |12|->ccc(98307|12)->NULL
     * |13|
     * |14|
     * |15|
     * </pre>
     */
    @Test
    public void testRehash() {
        MyHashMap<String, String> map = new MyHashMap<>(8);
        map.put("aaa", "1");
        map.put("bbb", "2");
        map.put("ccc", "3");
        map.put("ddd", "4");
        map.put("eee", "5");
        map.put("fff", "6");
        assertThat(map.get("aaa"), Matchers.is("1"));
        assertThat(map.get("bbb"), Matchers.is("2"));
        assertThat(map.get("ccc"), Matchers.is("3"));
        assertThat(map.get("ddd"), Matchers.is("4"));
        assertThat(map.get("eee"), Matchers.is("5"));
        assertThat(map.get("fff"), Matchers.is("6"));
        assertThat(map.length(), Matchers.is(8));
        map.printViewable();

        System.out.println("===========");
        map.put("zzz", "99");
        assertThat(map.length(), Matchers.is(16));
        map.printViewable();
    }

    /**
     * 使用{@link NotNorJdkWayHashStrategy}没有异或混合高低位的strategy：
     * <pre>
     * |0|
     * |1|
     * |2|
     * |3|
     * |4|
     * |5|
     * |6|
     * |7|->4095(4095|7)->2047(2047|7)->1023(1023|7)->511(511|7)->255(255|7)->127(127|7)->NULL
     * </pre>
     * <p/>
     * 使用标准的jdk哈希策略：
     * <pre>
     * |0|->127(120|0)->NULL
     * |1|->255(241|1)->NULL
     * |2|
     * |3|->511(483|3)->NULL
     * |4|
     * |5|
     * |6|
     * |7|->4095(3871|7)->2047(1935|7)->1023(967|7)->NULL
     * </pre>
     */
    @Test
    public void testJdkWayHashStrategy() {
        MyHashMap<Integer, String> map = new MyHashMap<>(8, new NotNorJdkWayHashStrategy<Integer>());
        for (int i = Byte.MAX_VALUE; i < 4096; i = (i << 1) + 1) {
            map.put(i, "" + i);
        }
        assertThat(map.length(), Matchers.is(8));
        map.printViewable();

        System.out.println("=======");

        map = new MyHashMap<>(8, new JdkWayHashStrategy<Integer>());
        for (int i = Byte.MAX_VALUE; i < 4096; i = (i << 1) + 1) {
            map.put(i, "" + i);
        }
        assertThat(map.length(), Matchers.is(8));
        map.printViewable();
    }

}