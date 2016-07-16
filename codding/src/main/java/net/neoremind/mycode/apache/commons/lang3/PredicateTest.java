package net.neoremind.mycode.apache.commons.lang3;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.apache.commons.collections.functors.TransformedPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.guava.vo.Person;

/**
 * @author zhangxu
 */
public class PredicateTest {

    @Test
    public void testUnique() {
        List<String> names = Lists.newArrayList("abc", "xxx", "bbb", "xxx");
        CollectionUtils.filter(names, UniquePredicate.getInstance());
        System.out.println(names);
    }

    @Test
    public void testNotNull() {
        List<String> names = Lists.newArrayList("abc", null, "bbb", null);
        CollectionUtils.filter(names, NotNullPredicate.getInstance());
        System.out.println(names);
    }

    @Test
    public void testNot() {
        List<String> names = Lists.newArrayList("abc", "xxx", "bbb", null);
        CollectionUtils.filter(names, new NotPredicate(n -> n == null || ((String) n).startsWith("a")));
        System.out.println(names); //[xxx, bbb]
    }

    @Test
    public void testAll() {
        List<String> names = Lists.newArrayList("abc", "xxx", "bbb", null);
        CollectionUtils.filter(names, new AllPredicate(
                new Predicate[] {n -> n != null,
                        n -> ((String) n).startsWith("a"),
                        n -> ((String) n).length() > 2}));
        System.out.println(names); //[aaa]
    }

    @Test
    public void testTransformPredicate() {
        List<Person> peopleList = createPersons();
        CollectionUtils.filter(peopleList, TransformedPredicate.getInstance(p -> ((Person) p).getName(),
                n -> ((String) n).startsWith("b")));
        System.out.println(peopleList);
    }

    public List<Person> createPersons() {
        List<Person> peopleList = new ArrayList<Person>();
        peopleList.add(new Person("bowen", 27));
        peopleList.add(new Person("bob", 20));
        peopleList.add(new Person("Katy", 18));
        peopleList.add(new Person("Logon", 24));
        return peopleList;
    }
}
