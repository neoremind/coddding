package net.neoremind.mycode.guava.collections;

import com.google.common.collect.ImmutableSet;

public class ImmutableSetTest {

	public static void main(String[] args) {
		ImmutableSet<String> COLOR_NAMES = ImmutableSet.of("red", "orange", "yellow", "green", "blue", "purple");
		try {
			COLOR_NAMES.add("hehe");
		} catch (UnsupportedOperationException e) {
			System.out.println(e.getMessage());
		}

		ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<String>();
		builder.add("x").add("y");
		ImmutableSet<String> xySet = builder.build();
		System.out.println(xySet);
		
		ImmutableSet<String> abcSet = ImmutableSet.<String> builder().add("a").add("b").build();
		System.out.println(abcSet);
		
		ImmutableSet.copyOf(abcSet);
	}

}
