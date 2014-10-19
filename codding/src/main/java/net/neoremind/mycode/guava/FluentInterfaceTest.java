package net.neoremind.mycode.guava;

import java.util.ArrayList;
import java.util.List;

import net.neoremind.mycode.guava.vo.Person;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class FluentInterfaceTest {

	public static void main(String[] args) {
		List<Person> peopleList = new ArrayList<Person>();
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		Predicate<Person> predicate = new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() >= 20;
			}
		};
		
		Iterable<String> personNameIterable = FluentIterable.from(peopleList).filter(predicate).limit(2)
				.transform(new Function<Person, String>() {
					@Override
					public String apply(Person p) {
						return p.getName();
					}
				});
		
		for (String string : personNameIterable) {
			System.out.println(string);
		}
		
	}
}
