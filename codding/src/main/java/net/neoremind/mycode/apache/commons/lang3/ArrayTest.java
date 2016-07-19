package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class ArrayTest {

    @Test
    public void testToString() {
        int[] ints = new int[] {1, 2, 3, 4, 5};
        System.out.println(ints); // [I@4361bd48 bad printing...
        System.out.println(ArrayUtils.toString(ints)); // {1,2,3,4,5}
    }

}
