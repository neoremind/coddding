package net.neoremind.mycode.bit;

import com.google.common.primitives.Longs;


public class Long16Radix {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("打印最大long和最小long的十六进制、二进制明文");
		System.out.println("最大long十进制：" + Long.MAX_VALUE);
		System.out.println("最大long十六进制：" + Long.toHexString(Long.MAX_VALUE));
		System.out.println("最大long二进制（最高位是0没打印出来）：" + Long.toBinaryString(Long.MAX_VALUE));
		System.out.println("最小long十进制：" + Long.MIN_VALUE);
		System.out.println("最小long十六进制：" + Long.toHexString(Long.MIN_VALUE));
		System.out.println("最小long二进制：" + Long.toBinaryString(Long.MIN_VALUE));
		
		System.out.println("===========================");
		long l = 0xffffffffffffffffL;
		System.out.println("long l = 0xffffffffffffffffL的十进制：" + l);
		
	    byte[] numberByte = Longs.toByteArray(l);
	    System.out.println("使用guava api将l转成byte[]再转成long的结果：" + Longs.fromByteArray(numberByte));
		
		String hex = Long.toHexString(l);
		System.out.println("Long.toHexString(l)：" + hex);
		try {
			long ll = Long.parseLong(hex, 16);
		} catch (Exception e) {
		}
		System.out.println("Long.parseLong(\"ffffffffffffffff\", 16)发生异常");
		
		System.out.println("-------------------");
		l = 0x7fffffffffffffffL;
		System.out.println("long l = 0x7fffffffffffffffL的十进制：" + l);
		
	    numberByte = Longs.toByteArray(l);
	    System.out.println("使用guava api将l转成byte[]再转成long的结果：" + Longs.fromByteArray(numberByte));
		
		hex = Long.toHexString(l);
		System.out.println("Long.toHexString(l)：" + hex);
		long ll = Long.parseLong(hex, 16);
		System.out.println("Long.parseLong(\"7fffffffffffffff\", 16)：" + ll);
		
		System.out.println("===========================");
		l = 0x7fffffffffffffffL;
		byte[] bytes = longToBytes(l);
		long res = bytesToLong(bytes);
		System.out.println("l = 0x7fffffffffffffffL;byte[] bytes = longToBytes(l);long res = bytesToLong(bytes);的结果" + res);
		
		System.out.println("===========================");
		//java中有3个移位运算符
		//>> 算术右移：低位溢出，符号位不变，并用符号位补溢出的高位
		//>>>逻辑右移：低位溢出，高位补0
		long maxLong = Long.MAX_VALUE;
		System.out.println("Long.toHexString(maxLong):" + Long.toHexString(maxLong));
		System.out.println("Long.toBinaryString(maxLong):" + Long.toBinaryString(maxLong));
		System.out.println("Long.toHexString(maxLong>>1):" + Long.toHexString(maxLong>>1));
		System.out.println("Long.toBinaryString(maxLong>>1)低位溢出，符号位不变，并用符号位补溢出的高位:" + Long.toBinaryString(maxLong>>1));
		System.out.println("Long.toHexString(maxLong>>>1):" + Long.toHexString(maxLong>>>1));
		System.out.println("Long.toBinaryString(maxLong>>>1)低位溢出，高位补0:" + Long.toBinaryString(maxLong>>>1));
		
		System.out.println("-------------------");
		long minLong = Long.MIN_VALUE;
		System.out.println("Long.toHexString(minLong)" + Long.toHexString(minLong));
		System.out.println("Long.toBinaryString(minLong)" + Long.toBinaryString(minLong));
		System.out.println("Long.toHexString(minLong>>1)" + Long.toHexString(minLong>>1));
		System.out.println("Long.toBinaryString(minLong>>1)低位溢出，符号位不变，并用符号位补溢出的高位:" + Long.toBinaryString(minLong>>1));
		System.out.println("Long.toHexString(minLong>>>1)" + Long.toHexString(minLong>>>1));
		System.out.println("Long.toBinaryString(minLong>>>1)低位溢出，高位补0(变成了正数！):" + Long.toBinaryString(minLong>>>1));
	}
	
	public static byte[] longToBytes(long x) {
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) ((x >> (i * 8)) & 0xFF);// 低位存储
		}
		return b;
	}
	
	public static long bytesToLong(byte[] x) {
		long value = 0;
		for (int i = 0; i < x.length; i++)
		{
		   value += ((long) x[i] & 0xffL) << (8 * i);
		}
		return value;
	}
	
}
