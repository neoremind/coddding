package net.neoremind.mycode.bit;

import com.google.common.primitives.Ints;

public class ProtobufBitTest {

    public static void main(String[] args) {

        int n = -128;
        System.out.println(n + "|" + Integer.toBinaryString(n) + "|"
                + Integer.toHexString(n));
        n = encodeZigZag(n);
        System.out.println(n + "|" + Integer.toBinaryString(n) + "|"
                + Integer.toHexString(n));
        System.out.println(~0x7F);
        System.out.println(tst(-8));

        System.out.println("---------");
        int value = -666666666;
        byte[] rawBytes = Ints.toByteArray(value);
        printByteArray(rawBytes);
        byte[] varint32Bytes = writeToVarint32(value);
        printByteArray(varint32Bytes);
    }

    public static void printByteArray(byte[] byteArray) {
        for (int i = 0; i < byteArray.length; i++) {
            System.out.println(i + ":" + byteArray[i] + "=0x"
                    + Integer.toHexString(byteArray[i] & 0xFF) + "="
                    + Integer.toBinaryString(byteArray[i] & 0xFF));
        }
        System.out.println("===");
    }

    /**
     * value must bigger than 0
     * 
     * @param value
     * @return
     */
    public static byte[] writeToVarint32(int value) {
        int size = computeRawVarint32Size(value);
        byte[] res = new byte[size];
        int offset = 0;
        if (size == 1) {
            res[offset] = (byte) value;
        } else {
            for (int i = 0; i < size - 1; i++, value >>>= 7) {
                res[offset++] = ((byte) ((value & 0x7F) | 0x80));
            }
            res[offset] = (byte) value;
        }
        return res;
    }

    public static int computeRawVarint32Size(final int value) {
        if ((value & (0xffffffff << 7)) == 0) {
            return 1;
        }
        if ((value & (0xffffffff << 14)) == 0) {
            return 2;
        }
        if ((value & (0xffffffff << 21)) == 0) {
            return 3;
        }
        if ((value & (0xffffffff << 28)) == 0) {
            return 4;
        }
        return 5;
    }

    /**
     * value must bigger than 0
     * 
     * @param value
     * @return
     */
    public static byte[] writeToProtostuffVarint32(int value) {
        int size = computeRawVarint32Size(value);
        byte[] res = new byte[size];
        int offset = 0;
        while (true) {
            if ((value & ~0x7F) == 0) {
                res[offset++] = (byte) value;
                return res;
            }
            res[offset++] = ((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
    }

    /**
     * 符号位倒置 <br/>
     * 异或运算符是用符号“^”表示的，其运算规律是： 两个操作数的位中，相同则结果为0，不同则结果为1。 15^2=13
     * 
     * <pre>
     * 1111
     * 0010
     * ----
     * 1101
     * </pre>
     * 
     * 举例来说，
     * 
     * <pre>
     * a     = 11111111111111111111111110000000 (ffffff80)
     * a<<1  = 11111111111111111111111100000000 (ffffff00)
     * a>>31 = 11111111111111111111111111111111 (ffffffff有符号右移)
     * ----------------------------------------
     *         00000000000000000000000011111111 (ff)
     * </pre>
     * 
     * @param n
     * @return
     */
    public static int encodeZigZag(final int n) {
        return (n << 1) ^ (n >> 31);
    }

    public static byte[] tst(int value) {
        byte[] res = new byte[4];
        int offset = 0;
        while (true) {
            if ((value & ~0x7F) == 0) {
                System.out.println((byte) value);
                res[offset] = (byte) value;
                return res;
            }
            System.out.println((byte) ((value & 0x7F) | 0x80));
            res[offset++] = ((byte) ((value & 0x7F) | 0x80));
            System.out.println(value >>>= 7);
            value >>>= 7;
        }
    }

}
