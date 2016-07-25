package net.neoremind.mycode.guava.reflection;

import java.util.Map;

import com.google.common.reflect.Reflection;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class ReflectionTest extends TestCase {

    public void testGetPackageName() throws Exception {
        assertEquals("java.lang", Reflection.getPackageName(Iterable.class));
        assertEquals("java", Reflection.getPackageName("java.MyType"));
        assertEquals("java.lang", Reflection.getPackageName(Iterable.class.getName()));
        assertEquals("", Reflection.getPackageName("NoPackage"));
        assertEquals("java.util", Reflection.getPackageName(Map.Entry.class));
    }

    private static int classesInitialized = 0;

    private static class A {
        static {
            ++classesInitialized;
        }
    }

    private static class B {
        static {
            ++classesInitialized;
        }
    }

    private static class C {
        static {
            ++classesInitialized;
        }
    }

    public void testInitialize() {
        assertEquals("This test can't be included twice in the same suite.", 0, classesInitialized);

        Reflection.initialize(A.class);
        assertEquals(1, classesInitialized);

        Reflection.initialize(
                A.class,  // Already initialized (above)
                B.class,
                C.class);
        assertEquals(3, classesInitialized);
    }

}
