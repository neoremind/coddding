package net.neoremind.mycode.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {

	public static void main(String[] args) {
		ConcurrentHashMap<String, String> chm = new ConcurrentHashMap<String, String>();
		String key1 = "key1";
		String value1 = "value1";
		String value2 = "value2";
		System.out.println(chm.putIfAbsent(key1, value1));
		System.out.println(chm.get(key1));
		System.out.println(chm.putIfAbsent(key1, value2));
		System.out.println(chm.get(key1));
	}
}
