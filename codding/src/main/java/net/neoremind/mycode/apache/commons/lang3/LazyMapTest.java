package net.neoremind.mycode.apache.commons.lang3;

import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.LazyMap;
import org.junit.Test;

import com.google.common.collect.Maps;

/**
 * 完全没有泛型安全检查，不建议使用
 *
 * @author zhangxu
 */
public class LazyMapTest {

    @Deprecated
    @Test
    public void testLRUMap() {
        Map<Integer, String> hashMap = Maps.newHashMap();
        hashMap.put(1, "abc");
        hashMap.put(2, "uuu");
        Map<Integer, String> lazyMap = LazyMap.decorate(hashMap, s -> ((String) s).toUpperCase());
        System.out.println(lazyMap);
        System.out.println(lazyMap.get("xx"));
        System.out.println(lazyMap);
    }

}
