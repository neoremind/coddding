package net.neoremind.mycode.guava.collections;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BiMapTest {

	public static void main(String[] args) {
		BiMap<Integer, String> biMap = HashBiMap.create();
		biMap.put(1, "a");
		biMap.put(2, "b");
		biMap.forcePut(3, "c");
		System.out.println(biMap);
		BiMap<String, Integer> inversedBiMap = biMap.inverse();
		System.out.println(inversedBiMap);
	}
	
}
