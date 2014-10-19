package net.neoremind.mycode.guava;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Ints;

public class IntsTest {

	public static void main(String[] args) {
		int[] array = { 1, 2, 3, 4, 5 };
		int a = 4;
		boolean contains = Ints.contains(array, a);
		System.out.println(contains);

		int indexOf = Ints.indexOf(array, a);
		System.out.println(indexOf);
		
		int lastIndexOf = Ints.lastIndexOf(array, a);
		System.out.println(lastIndexOf);

		int max = Ints.max(array);
		System.out.println(max);

		int min = Ints.min(array);
		System.out.println(min);

		int[] array2 = { 6, 7, 8 };
		int[] concat = Ints.concat(array, array2);
		System.out.println(concat);
		
		long l = 256L;
		int i = Ints.checkedCast(l);
		System.out.println(i);
		
		byte[] intByteInBigEndian = Ints.toByteArray(96);
		int x = Ints.fromByteArray(intByteInBigEndian);
		System.out.println(x);
		System.out.println(Integer.toBinaryString(x));
		System.out.println(Integer.toHexString(x));
		
		String intStr = Ints.join(",", new int[]{1,2,3,4,5});
		System.out.println(intStr);
		
		List<Integer> intList = new ArrayList<Integer>();
		intList.add(1);
		intList.add(2);
		intList.add(3);
		System.out.println(Ints.toArray(intList));
		
		System.out.println(Ints.asList(array));
		
		// 无符号整数进行比较，对于UnsignedInt来说，下面的small和big都是整数，因此需要flip一下，
		// flip的作用就是将负数转成正数，而正数不变
		int small = -127;
		int big = -136;
		System.out.println(Integer.toBinaryString(small));
		System.out.println(Integer.toBinaryString(big));
		System.out.println(flip(small));
		System.out.println(flip(big));
		System.out.println(Integer.toBinaryString(flip(small)));
		System.out.println(Integer.toBinaryString(flip(big)));
		
		System.out.println(Ints.compare(small, big)); //1
	}

	static int flip(int value) {
		return value ^ Integer.MIN_VALUE;
	}

}
