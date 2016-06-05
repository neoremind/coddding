package net.neoremind.mycode.fj;

import fj.data.Array;

import static fj.data.Array.array;
import static fj.data.List.fromString;
import static fj.function.Characters.isLowerCase;
import static fj.function.Integers.even;
import static fj.Show.arrayShow;
import static fj.Show.intShow;
import static fj.function.Integers.add;

import fj.F;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class ArrayExample {

    @Test
    public void testArrayExist() {
        final Array<String> a = array("Hello", "There", "what", "DAY", "iS", "iT");
        final boolean b = a.exists(s -> fromString(s).forall(isLowerCase));
        System.out.println(b); // true ("what" is the only value that qualifies; try removing it)
    }

    @Test
    public void testArrayFilter() {
        final Array<Integer> a = array(97, 44, 67, 3, 22, 90, 1, 77, 98, 1078, 6, 64, 6, 79, 42);
        final Array<Integer> b = a.filter(even);
        final Array<Integer> c = a.filter(i -> i % 2 == 0);
        arrayShow(intShow).println(b); // {44,22,90,98,1078,6,64,6,42}
    }

    @Test
    public void testArrayFoldLeft() {
        final Array<Integer> a = array(97, 44, 67, 3, 22, 90, 1, 77, 98, 1078, 6, 64, 6, 79, 42);
        final int b = a.foldLeft(add, 0);

        F<Integer, F<Integer, Integer>> add2 = i -> (j -> i + j);
        final int c = a.foldLeft(add2, 0);
        System.out.println(b); // 1774
    }

    @Test
    public void testArrayForAll() {
        final Array<String> a = array("hello", "There", "what", "day", "is", "it");
        final boolean b = a.forall(s -> fromString(s).forall(isLowerCase));
        System.out.println(b); // false ("There" is a counter-example; try removing it)
    }

    @Test
    public void testArrayMap() {
        final Array<Integer> a = array(1, 2, 3);
        final Array<Integer> b = a.map(add.f(42));
        final Array<Integer> c = a.map(i -> i + 42);
        arrayShow(intShow).println(b); // {43,44,45}
        arrayShow(intShow).println(c); // {43,44,45}
    }

}
