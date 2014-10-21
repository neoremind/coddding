package net.neoremind.mycode.guava.collections;

import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

/**
 * 你可能会说这和 Set 接口的契约冲突，因为 Set 接口的 JavaDoc 里面规定不能放入重复元素。 事实上，Multiset 并没有实现 java.util.Set 接口，它更像是一个 Bag。普通的 Set 就像这样
 * :[car, ship, bike]，而 Multiset 会是这样 : [car x 2, ship x 6, bike x 3]。
 */
public class HashMultiSetTest {

	public static void main(String[] args) {
		List<String> wordList = Lists.newArrayList();
		wordList.add("a");
		wordList.add("b");
		wordList.add("b");
		HashMultiset<String> multiSet = HashMultiset.create();
		multiSet.addAll(wordList);
		int count = multiSet.count("b");
		System.out.println(count);
		multiSet.remove("b", 1);
		System.out.println(multiSet.count("b"));

		List<String> strings = Lists.newArrayList("A", "bc", "DEF");

		Function<String, Integer> lengthFunction = new Function<String, Integer>() {
			public Integer apply(String string) {
				return string.length();
			}
		};
		Predicate<String> allCaps = new Predicate<String>() {
			public boolean apply(String string) {
				return CharMatcher.JAVA_UPPER_CASE.matchesAllOf(string);
			}
		};
		Multiset<Integer> lengthSet = HashMultiset.create(Iterables.transform(Iterables.filter(strings, allCaps),
				lengthFunction));
		System.out.println(lengthSet);
	}

}
