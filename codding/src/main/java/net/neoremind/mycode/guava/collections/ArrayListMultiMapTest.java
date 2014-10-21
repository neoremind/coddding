package net.neoremind.mycode.guava.collections;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ArrayListMultiMapTest {

	public static void main(String[] args) {
		Multimap<Integer, String> multimap = ArrayListMultimap.create();
		multimap.put(1, "a");
		multimap.put(1, "b");
		multimap.put(2, "k");
		multimap.put(3, "x");
		multimap.put(3, "y");
		multimap.put(3, "ya");
		System.out.println(multimap);
	}
	
}
