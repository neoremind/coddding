package net.neoremind.mycode.guava;

import com.google.common.base.Splitter;

public class SplitterTest {

	public static void main(String[] args) {
		// Split string
		String str = "a|b|c|d||e";
		Iterable<String> strIter = Splitter.on("|").trimResults().omitEmptyStrings().split(str);
		for (String string : strIter) {
			System.out.println(string);
		}
	}

}
