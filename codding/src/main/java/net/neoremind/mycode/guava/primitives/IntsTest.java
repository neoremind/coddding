package net.neoremind.mycode.guava.primitives;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class IntsTest extends TestCase {

    public void testContains() {
        int[] array = {1, 2, 3, 4, 5};
        assertTrue(Ints.contains(array, 1));
    }

    public void testIndexOf() {
        int[] array = {1, 2, 3, 4, 5};
        assertEquals(Ints.indexOf(array, 1), 0);
    }

    public void testLastIndexOf() {
        int[] array = {1, 1, 2, 3, 4, 5};
        assertEquals(Ints.lastIndexOf(array, 1), 1);
    }

    public void testMax() {
        int[] array = {1, 2, 3, 4, 5};
        assertEquals(Ints.max(array), 5);
    }

    public void testConcat() {
        int[] array = {1, 2, 3};
        int[] array2 = {4, 5};
        assertEquals(Ints.concat(array, array2).length, 5);
    }

    public void testCheckCast() {
        long l = 256L;
        Ints.checkedCast(l);
    }

    public void testFromAndToByteArray() {
        byte[] intByteArrayInBigEndian = Ints.toByteArray(96);
        int x = Ints.fromByteArray(intByteArrayInBigEndian);
        assertEquals(intByteArrayInBigEndian[0], 0);
        assertEquals(intByteArrayInBigEndian[1], 0);
        assertEquals(intByteArrayInBigEndian[2], 0);
        assertEquals(intByteArrayInBigEndian[3], 0x60);
        assertEquals(Integer.toBinaryString(x), "1100000");
        assertEquals(Integer.toHexString(x), "60");
    }

    public void testFromBytes() {
        assertEquals(0x12131415, Ints.fromBytes(
                (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15));
        assertEquals(0xFFEEDDCC, Ints.fromBytes(
                (byte) 0xFF, (byte) 0xEE, (byte) 0xDD, (byte) 0xCC));
    }

    public void testJoin() {
        int[] array = {1, 2, 3};
        assertEquals(Ints.join(",", array), "1,2,3");
    }

    public void testToArrayAndAsList() {
        List<Integer> intList = new ArrayList<Integer>();
        intList.add(1);
        intList.add(2);
        intList.add(3);
        int[] array = Ints.toArray(intList);
        System.out.println(Ints.toArray(intList));
        System.out.println(Ints.asList(array));
    }

    public void testFromBytesBadly() {
        int x = 128;
        byte[] bytes = Ints.toByteArray(x);
        Bytes.asList(bytes).stream().forEach(b -> System.out.println(Integer.toHexString(b & 0xff)));
        System.out.println(fromBytesBadly(bytes));
        assertEquals(Ints.fromByteArray(bytes), 128);  // correct!
        assertEquals(fromBytesBadly(bytes), -128);  // wrong!
    }

    public void testFromBytesBadly2() {
        int x = 128;
        byte[] bytes = Ints.toByteArray(x);
        Bytes.asList(bytes).stream().forEach(b -> System.out.println(Integer.toHexString(b & 0xff)));
        System.out.println(fromBytesBadly2(bytes));
        assertEquals(Ints.fromByteArray(bytes), 128);  // correct!
        assertEquals(fromBytesBadly2(bytes), -128);  // wrong!
    }

    /**
     * no {@code & 0xFF}
     * 这样的话，在byte提升为int的时候会做符号位的扩展
     */
    public static int fromBytesBadly(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1]) << 16 | (bytes[2]) << 8 | (bytes[3]);
    }

    /**
     * no {@code & 0xFF}
     */
    public static int fromBytesBadly2(byte[] bytes) {
        int result = 0xFF & bytes[0];
        result <<= 8;
        result += bytes[1];
        result <<= 8;
        result += bytes[2];
        result <<= 8;
        result += bytes[3];
        return result;
        //        int result = 0xFF & bytes[0];
        //        result <<= 8;
        //        result += 0xFF & bytes[1];
        //        result <<= 8;
        //        result += 0xFF & bytes[2];
        //        result <<= 8;
        //        result += 0xFF & bytes[3];
        //        return result;
    }

}
