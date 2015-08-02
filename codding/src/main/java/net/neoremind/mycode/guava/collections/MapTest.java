package net.neoremind.mycode.guava.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import net.neoremind.mycode.guava.vo.Person;

public class MapTest {

	public static void main(String[] args) {
		List<Person> peopleList = new ArrayList<Person>();
		peopleList.add(new Person("bowen", 27));
		peopleList.add(new Person("bob", 20));
		peopleList.add(new Person("Katy", 18));
		peopleList.add(new Person("Logon", 24));

		Map<Integer, Person> mappedPerson = Maps.uniqueIndex(peopleList, new Function<Person, Integer>() {
			public Integer apply(Person p) {
				return p.getAge();
			}
		});

		for (Integer age : mappedPerson.keySet()) {
			System.out.println(age + "=>" + mappedPerson.get(age));
		}
		
		System.out.println("======");

		/**
		 * Returns a capacity that is sufficient to keep the map from being resized as long as it grows no larger than
		 * expectedSize and the load factor is >= its default (0.75).
		 */
		// new map with expected capacity /0.75
		Map<String, Person> expectedSizeMap = Maps.newHashMapWithExpectedSize(75);
		System.out.println(expectedSizeMap);

		// new map
		Map<String, Person> map = Maps.newHashMap();
		for (Person person : peopleList) {
			map.put(person.getName(), person);
		}

		// trans value
		Map<String, Integer> transformedValueMap = Maps.transformValues(map, new Function<Person, Integer>() {
			public Integer apply(Person person) {
				return person.getAge();
			}
		});
		System.out.println(transformedValueMap);

		// filter key
		Map<String, Person> filtedKeyMap = Maps.filterKeys(map, new Predicate<String>() {
			public boolean apply(String name) {
				return name.startsWith("b");
			}
		});
		System.out.println(filtedKeyMap);

		// filter value
		Map<String, Person> filtedValueMap = Maps.filterValues(map, new Predicate<Person>() {
			public boolean apply(Person person) {
				return person.getAge() > 20;
			}
		});
		System.out.println(filtedValueMap);

		// test properties
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("b", "2");

		Map<String, String> propMap = Maps.fromProperties(properties);
		System.out.println(propMap);

		// unique index
		ImmutableMap<String, Person> personNameMap = Maps.uniqueIndex(peopleList, new Function<Person, String>() {
			public String apply(Person person) {
				return person.getName();
			}
		});
		System.out.println(personNameMap);

		Map<String, Integer> left = ImmutableMap.of("a", 1, "b", 2, "c", 3);
		Map<String, Integer> right = ImmutableMap.of("a", 4, "b", 2, "d", 6);
		MapDifference<String, Integer> diff = Maps.difference(left, right);
		System.out.println(diff.entriesInCommon()); // {"b" => 2}
		System.out.println(diff.entriesOnlyOnLeft()); // {"a" => 1}
		System.out.println(diff.entriesOnlyOnRight()); // {"d" => 5}
	}

}
