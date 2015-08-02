package net.neoremind.mycode.guava.collections;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import net.neoremind.mycode.guava.vo.Person;

public class ListTest {

	public static void main(String[] args) {
		List<String> strlist = Lists.newArrayList("one", "two", "three");
		System.out.println(strlist);

		List<Integer> countUp = Ints.asList(1, 2, 3, 4, 5);
		System.out.println(countUp);
		List<Integer> countDown = Lists.reverse(countUp); // {5, 4, 3, 2, 1}
		System.out.println(countDown);
		List<List<Integer>> parts = Lists.partition(countUp, 2); // {{1, 2}, {3, 4}, {5}}
		System.out.println(parts);

		List<Person> peopleList = Lists.newArrayListWithCapacity(4);
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		// trans
		Collection<String> nameUpperList = Lists.transform(peopleList, new Function<Person, String>() {
			@Override
			public String apply(Person s) {
				if (s == null) {
					return "";
				}
				return s.getName().toUpperCase();
			}
		});
		System.out.println(nameUpperList);

	}

}
