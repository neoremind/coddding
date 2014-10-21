package net.neoremind.mycode.guava.collections;

import com.google.common.collect.HashMultimap;

public class HashMultiMapTest {

	public static void main(String[] args) {
		HashMultimap<Integer, String> map = HashMultimap.create();
		map.put(1, "a");
		map.put(1, "b");
		map.put(2, "k");
		map.put(3, "x");
		map.put(3, "y");
		System.out.println(map);
		System.out.println(map.get(3));
		System.out.println(map.get(99));
		System.out.println(map.containsValue("x"));
		System.out.println(map.keys());
		System.out.println(map.keySet());
	}

}
