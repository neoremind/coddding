package net.neoremind.mycode.guava.primitives;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Primitives;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class PrimitivesTest extends TestCase {

    public void testWrap() {
        assertSame(Integer.class, Primitives.wrap(int.class));
        assertSame(Integer.class, Primitives.wrap(Integer.class));
        assertSame(String.class, Primitives.wrap(String.class));
    }

    public void testUnwrap() {
        assertSame(int.class, Primitives.unwrap(Integer.class));
        assertSame(int.class, Primitives.unwrap(int.class));
        assertSame(String.class, Primitives.unwrap(String.class));
    }

    public void testAllPrimitiveTypes() {
        Set<Class<?>> primitives = Primitives.allPrimitiveTypes();
        assertEquals(
                ImmutableSet.<Object>of(
                        boolean.class, byte.class, char.class, double.class,
                        float.class, int.class, long.class, short.class, void.class),
                primitives);

        try {
            primitives.remove(boolean.class);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testAllWrapperTypes() {
        Set<Class<?>> wrappers = Primitives.allWrapperTypes();
        assertEquals(
                ImmutableSet.<Object>of(
                        Boolean.class, Byte.class, Character.class, Double.class,
                        Float.class, Integer.class, Long.class, Short.class, Void.class),
                wrappers);

        try {
            wrappers.remove(Boolean.class);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }
}
