package net.neoremind.mycode.guava;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.neoremind.mycode.guava.vo.Person;

public class OrderingTest {

	public static void main(String[] args) {
		List<Person> peopleList = new ArrayList<Person>();
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		Ordering<Person> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<Person, Integer>() {
			public Integer apply(Person p) {
				return p.getAge();
			}
		});

		System.out.println(ordering.isOrdered(peopleList)); // false
		Collections.sort(peopleList, ordering);
		System.out.println(peopleList);
		System.out.println(ordering.isOrdered(peopleList)); // true
		Person minPerson = Collections.min(peopleList, ordering);
		System.out.println(minPerson);
		Person maxPerson = Collections.max(peopleList, ordering);
		System.out.println(maxPerson);
		List<Person> subAList = ordering.leastOf(peopleList, 3);
		System.out.println(subAList);

		List<Integer> numbers = Lists.newArrayList(30, 20, 60, 80, 10);
		Ordering.natural().sortedCopy(numbers); // 10,20,30,60,80
		Ordering.natural().reverse().sortedCopy(numbers); // 80,60,30,20,10
		Ordering.natural().min(numbers); // 10
		Ordering.natural().max(numbers); // 80

		numbers = Lists.newArrayList(30, 20, 60, 80, null, 10);
		Ordering.natural().nullsLast().sortedCopy(numbers); // 10, 20,30,60,80,null
		Ordering.natural().nullsFirst().sortedCopy(numbers); // null,10,20,30,60,80
	}

}
