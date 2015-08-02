package net.neoremind.mycode.guava.collections;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.neoremind.mycode.guava.vo.Person;

public class IterableTest {

	public static void main(String[] args) {
		List<Person> peopleList = Lists.newArrayListWithCapacity(4);
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		System.out.println(Iterables.all(peopleList, new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() >= 20;
			}
		}));

		List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 1, 2);
		System.out.println(Iterables.frequency(intList, 1));

		Integer[] intArray = Iterables.toArray(intList, Integer.class);
		System.out.println(Arrays.toString(intArray));

		Iterables.removeIf(peopleList, new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() >= 20;
			}
		});
		System.out.println(peopleList);

	}
}
