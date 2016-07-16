package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.collections.map.LRUMap;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class LRUMapTest {

    @Test
    public void testLRUMap() {
        LRUMap lruMap = new LRUMap(4);
        lruMap.putIfAbsent(1, "A");
        lruMap.putIfAbsent(1, "AA"); // NOT SUCCESS
        lruMap.putIfAbsent(2, "B");
        lruMap.put(3, "C");
        lruMap.put(4, "D");
        System.out.println(lruMap.isFull()); //true
        System.out.println(lruMap); //{1=A, 2=B, 3=C, 4=D}
    }

}
