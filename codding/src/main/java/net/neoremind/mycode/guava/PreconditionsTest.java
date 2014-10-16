package net.neoremind.mycode.guava;

import com.google.common.base.Preconditions;

public class PreconditionsTest {

	public static void main(String[] args) {
		testCheckNotNull(new Object());
		testCheckArgument(5);
		testCheckArgument(-1);
	}

	public static void testCheckArgument(int i) {
		Preconditions.checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
	}

	public static void testCheckNotNull(Object obj) {
		Preconditions.checkNotNull(obj, "Argument should not be null");
	}

}
