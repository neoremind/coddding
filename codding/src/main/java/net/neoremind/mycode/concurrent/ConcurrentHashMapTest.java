package net.neoremind.mycode.concurrent;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: ConcurrentHashMapTest <br/>
 * Function: 测试putIfAbsent，不存在时返回null，一旦存在则拥有不变了
 * 
 * @author Zhang Xu
 */
public class ConcurrentHashMapTest {

    public static void main(String[] args) {
        ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<String, String>();
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";
        assertThat(chm.putIfAbsent(key, value1), nullValue());
        System.out.println(chm.get(key));
        assertThat(chm.putIfAbsent(key, value2), is(value1));
        System.out.println(chm.get(key));
    }

}
