package net.neoremind.mycode.guava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;

import net.neoremind.mycode.guava.vo.Person;

public class FunctionTest {

	public static void main(String[] args) {
		List<Person> peopleList = new ArrayList<Person>();
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		Function<Person, String> f = new Function<Person, String>() {
			@Override
			public String apply(Person p) {
				if (p == null) {
					return "";
				}
				return p.getName();
			}

		};

		Collection<String> nameList = Collections2.transform(peopleList, f);
		for (String string : nameList) {
			System.out.println(string);
		}

		// ////////////

		Function<String, String> f2 = new Function<String, String>() {
			@Override
			public String apply(String s) {
				if (s == null) {
					return "";
				}
				return s.toUpperCase();
			}
		};
		
		//3、定义函数组合，将对每个元素执行f，然后对结果进行f2计算，即：result=f2(f(school))  
        Function<Person,String> f3 = Functions.compose(f2, f);  
          
        //4、开始转换集合  
        Collection<String> nameUpperList = Collections2.transform(peopleList, f3);  
        for (String string : nameUpperList) {
        	System.out.println(string);
		}
	}

}
