package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.collections.Closure;
import org.junit.Test;

import net.neoremind.mycode.guava.vo.Person;

/**
 * @author zhangxu
 */
public class ClosureTest {

    @Test
    public void testClosure() {
        Person p = new Person("bowen", 27);
        Closure closure = o -> ((Person) o).setAge(100);
        closure.execute(p);
        System.out.println(p);
    }

}
