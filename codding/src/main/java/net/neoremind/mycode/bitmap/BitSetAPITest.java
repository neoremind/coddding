package net.neoremind.mycode.bitmap;

import java.util.BitSet;

public class BitSetAPITest {
	public static void main(String[] args) {
		// BitSet的构造函数
		BitSet b1 = new BitSet();
		BitSet b2 = new BitSet(20);

		// set方法演示
		for (int i = 3; i < 20; i += 3)
			b1.set(i);
		System.out.println(b1.toString());// {3, 6, 9, 12, 15, 18}
		for (int i = 2; i < 15; i += 2)
			b1.set(i, false);
		System.out.println(b1.toString());// {3, 9, 15, 18}
		b2.set(5, 10);
		System.out.println(b2.toString());// {5, 6, 7, 8, 9}
		b2.set(8, 14, false);
		System.out.println(b2.toString());// {5, 6, 7}

		// flip方法演示
		b2.flip(10, 15);
		System.out.println(b2.toString());// {5, 6, 7, 10, 11, 12, 13, 14}
		b1.flip(15);
		System.out.println(b1.toString());// {3, 9, 18}

		// clear方法演示
		b2.clear(10);
		System.out.println(b2.toString());// {5, 6, 7, 11, 12, 13, 14}
		b2.clear(6, 9);
		System.out.println(b2.toString());// {5, 11, 12, 13, 14}
		b2.clear();
		System.out.println(b2.toString());// {}

		// get方法演示
		boolean get9 = b1.get(9);
		System.out.println(get9);// true
		BitSet b3 = b1.get(3, 10);
		System.out.println(b3.toString());// {0, 6}

		b1.set(7, 13);
		b2.set(9, 16);
		System.out.println(b1.toString());// {3, 7, 8, 9, 10, 11, 12, 18}
		System.out.println(b2.toString());// {9, 10, 11, 12, 13, 14, 15}
		// 位集操作
		b1.and(b2);
		System.out.println(b1.toString());// {9, 10, 11, 12}
		b2.xor(b1);
		System.out.println(b2.toString());// {13, 14, 15}
		b1.or(b2);
		System.out.println(b1.toString());// {9, 10, 11, 12, 13, 14, 15}
		b3.set(13, 15);
		b2.andNot(b3);
		System.out.println(b2.toString());// {15}

		// 设置位操作
		System.out.println(b1.cardinality());// 7
		System.out.println(b2.isEmpty());// false
		b2.clear();
		System.out.println(b2.isEmpty());// true

		System.out.println(b1.intersects(b3));// true

		// 大小操作
		System.out.println(b1.size());// 64
		System.out.println(b1.length());// 16=15+1

		// 查找
		System.out.println(b1.nextSetBit(9));// 9
		System.out.println(b1.nextClearBit(9));// 16

//		System.out.println(b1.previousSetBit(20));// 15
//		System.out.println(b1.previousClearBit(15));// 8

		// 类型转化操作
		// byte[] b=b1.toByteArray();
		// long[] l=b1.toLongArray();
	}
}