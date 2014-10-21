package net.neoremind.mycode.guava;

import com.google.common.base.CaseFormat;

public class CaseFormatTest {

	public static void main(String[] args) {
		System.out.println(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "HelloWorld"));
	}
	
}
