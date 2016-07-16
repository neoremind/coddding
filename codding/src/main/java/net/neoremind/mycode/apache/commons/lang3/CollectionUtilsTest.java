package net.neoremind.mycode.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

import net.neoremind.mycode.guava.vo.Person;

/**
 * 由于是较老的类库，所以没有泛型的检查，没有编译器的保护，从某些角度来说这点已经不适合当前的发展了。
 *
 * @author zhangxu
 */
public class CollectionUtilsTest {

    @Test
    public void testForAllDo() {
        List<Person> peopleList = createPersons();
        CollectionUtils.forAllDo(peopleList, p -> ((Person) p).setAge(100));
        System.out.println(peopleList);
    }

    @Test
    public void testFind() {
        List<Person> peopleList = createPersons();
        CollectionUtils.find(peopleList, p -> ((Person) p).getName().startsWith("b"));
        System.out.println(peopleList);
    }

    /**
     * 就地转换，只对于对于List类型
     */
    @Test
    public void testTransform() {
        List<Person> peopleList = createPersons();
        CollectionUtils.transform(peopleList, p -> ((Person) p).getName());
        System.out.println(peopleList); // in-place更改了peopleList类型为List<String>
    }

    /**
     * 新建一个新对象的转换
     */
    @Test
    public void testCollect() {
        List<Person> peopleList = createPersons();
        Collection<String> names = CollectionUtils.collect(peopleList, p -> ((Person) p).getName());
        System.out.println(peopleList);
        System.out.println(names); // 新建了一个新的peopleList类型为List<String>
    }

    /**
     * 就地删除
     */
    @Test
    public void testFilter() {
        List<Person> peopleList = createPersons();
        CollectionUtils.filter(peopleList, p -> ((Person) p).getAge() > 19);
        System.out.println(peopleList);
    }

    /**
     * 筛选出一个符合条件的新集合
     */
    @Test
    public void testSelect() {
        List<Person> peopleList = createPersons();
        Collection<Person> names = CollectionUtils.select(peopleList, p -> ((Person) p).getName().startsWith("b"));
        System.out.println(peopleList);
        System.out.println(names); // 新建了一个新的peopleList类型为List<String>
    }

    @Test
    public void testExist() {
        List<Person> peopleList = createPersons();
        boolean exists = CollectionUtils.exists(peopleList, p -> ((Person) p).getName().startsWith("b"));
        System.out.println(exists);
    }

    @Test
    public void testCountMatches() {
        List<Person> peopleList = createPersons();
        int count = CollectionUtils.countMatches(peopleList, p -> ((Person) p).getName().startsWith("b"));
        System.out.println(count); //2
    }

    /**
     * 出现的次数
     */
    @Test
    public void testCardinality() {
        List<Integer> src = Lists.newArrayList(1, 1, 1, 2, 3, 4, 5, 6);
        int count = CollectionUtils.cardinality(1, src);
        System.out.println(count); //3
    }

    /**
     * 保留，src中的元素引用不变，但是返回了新的集合
     */
    @Test
    public void testRetainAll() {
        List<Integer> src = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> retain = Lists.newArrayList(2, 4, 6, 8, 10);
        Collection<Integer> res = CollectionUtils.retainAll(src, retain);
        System.out.println(res); //[2, 4, 6]
    }

    /**
     * @see {@link #testRetainAll()} 和这个一样
     */
    @Test
    public void testRemoveAll() {
        List<Integer> src = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> remove = Lists.newArrayList(2, 4, 6, 8, 10);
        Collection<Integer> res = CollectionUtils.removeAll(src, remove);
        System.out.println(res); //[2, 4, 6]
    }

    /**
     * 排除，src中的元素引用不变，但是返回了新的集合
     */
    @Test
    public void testSubtract() {
        List<Integer> src = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> sub = Lists.newArrayList(2, 4, 6, 8, 10);
        Collection<Integer> res = CollectionUtils.subtract(src, sub);
        System.out.println(res); //[1, 3, 5]
    }

    /**
     * 这里的union是会保留某个集合中出现次数最多的那些个元素
     */
    @Test
    public void testUnion() {
        List<Integer> src = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> target = Lists.newArrayList(2, 2, 4, 6, 8, 10);
        Collection<Integer> res = CollectionUtils.union(src, target);
        System.out.println(res); //[1, 2, 2, 3, 4, 5, 6, 8, 10] 注意有两个2
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableCollection() {
        List<Person> peopleList = createPersons();
        Collection<String> names = CollectionUtils.collect(peopleList, p -> ((Person) p).getName());
        Collection<String> unmodifiableNames = Collections.unmodifiableCollection(names);
        unmodifiableNames.add("123");
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
