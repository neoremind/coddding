package net.neoremind.mycode.guava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.neoremind.mycode.guava.vo.Person;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class PredicateTest {

	public static void main(String[] args) {
		List<Person> peopleList = new ArrayList<Person>();
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		Collection<Person> oldPeople = Collections2.filter(peopleList, new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() >= 20;
			}
		});

		for (Person person : oldPeople) {
			System.out.println(person);
		}
		
		
		Collection<Person> nameAndOldPeople = Collections2.filter(peopleList, Predicates.and(nameContains("b"), ageBiggerThan(20)));

		for (Person person : nameAndOldPeople) {
			System.out.println(person);
		}
		
		
        Predicate<CharSequence> twoDigitsPredicate = Predicates.containsPattern("\\d\\d");
        System.out.println(twoDigitsPredicate.apply("Hello world 40"));
        
        Predicate<Long> elevenInCollectionPredicate = Predicates.in(Arrays.asList(11L, 22L, 33L, 44L));
        System.out.println(elevenInCollectionPredicate.apply(11L));
	}

	private static Predicate<Person> nameContains(final String str) {
		return new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getName().contains(str);
			}
		};
	}

	private static Predicate<Person> ageBiggerThan(final int age) {
		return new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() >= age;
			}
		};
	}

}
