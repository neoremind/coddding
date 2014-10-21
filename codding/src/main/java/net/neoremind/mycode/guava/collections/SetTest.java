package net.neoremind.mycode.guava.collections;

import java.util.HashSet;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class SetTest {

	public static void main(String[] args) {
		HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
		HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);

		SetView<Integer> union = Sets.union(setA, setB);
		System.out.println("union:" + union) ;

		SetView<Integer> differenceA = Sets.difference(setA, setB);
		System.out.println("difference:" + differenceA);
		
		SetView<Integer> differenceB = Sets.difference(setB, setA);
		System.out.println("difference:" + differenceB);
		
		SetView<Integer> intersection = Sets.intersection(setA, setB);
		System.out.println("intersection:" + intersection);
	}

}
