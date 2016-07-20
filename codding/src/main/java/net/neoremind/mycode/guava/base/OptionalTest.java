package net.neoremind.mycode.guava.base;

import java.util.Collections;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class OptionalTest extends TestCase {

    public void testAbsent() {
        Optional<String> optionalName = Optional.absent();
        assertFalse(optionalName.isPresent());
    }

    public void testOf() {
        assertEquals("training", Optional.of("training").get());
    }

    public void testOf_null() {
        try {
            Optional.of(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    public void testFromNullable() {
        Optional<String> optionalName = Optional.fromNullable("bob");
        assertEquals("bob", optionalName.get());
    }

    public void testFromNullable_null() {
        // not promised by spec, but easier to test
        assertSame(Optional.absent(), Optional.fromNullable(null));
    }

    public void testIsPresent_no() {
        assertFalse(Optional.absent().isPresent());
    }

    public void testIsPresent_yes() {
        assertTrue(Optional.of("training").isPresent());
    }

    public void testGet_absent() {
        Optional<String> optional = Optional.absent();
        try {
            optional.get();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    public void testGet_present() {
        assertEquals("training", Optional.of("training").get());
    }

    public void testOr_T_present() {
        assertEquals("a", Optional.of("a").or("default"));
    }

    public void testOr_T_absent() {
        assertEquals("default", Optional.absent().or("default"));
    }

    public void testOr_supplier_present() {
        assertEquals("a", Optional.of("a").or(Suppliers.ofInstance("fallback")));
    }

    public void testOr_supplier_absent() {
        assertEquals("fallback", Optional.absent().or(Suppliers.ofInstance("fallback")));
    }

    public void testOr_nullSupplier_absent() {
        Supplier<Object> nullSupplier = Suppliers.ofInstance(null);
        Optional<Object> absentOptional = Optional.absent();
        try {
            absentOptional.or(nullSupplier);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    public void testOr_nullSupplier_present() {
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        assertEquals("a", Optional.of("a").or(nullSupplier));
    }

    public void testOr_Optional_present() {
        assertEquals(Optional.of("a"), Optional.of("a").or(Optional.of("fallback")));
    }

    public void testOr_Optional_absent() {
        assertEquals(Optional.of("fallback"), Optional.absent().or(Optional.of("fallback")));
    }

    public void testOrNull_present() {
        assertEquals("a", Optional.of("a").orNull());
    }

    public void testOrNull_absent() {
        assertNull(Optional.absent().orNull());
    }

    public void testAsSet_present() {
        Set<String> expected = Collections.singleton("a");
        assertEquals(expected, Optional.of("a").asSet());
    }

    public void testAsSet_absent() {
        assertTrue("Returned set should be empty", Optional.absent().asSet().isEmpty());
    }

    public void testAsSet_presentIsImmutable() {
        Set<String> presentAsSet = Optional.of("a").asSet();
        try {
            presentAsSet.add("b");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testAsSet_absentIsImmutable() {
        Set<Object> absentAsSet = Optional.absent().asSet();
        try {
            absentAsSet.add("foo");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    public void testTransform_absent() {
        assertEquals(Optional.absent(), Optional.absent().transform(Functions.identity()));
        assertEquals(Optional.absent(), Optional.absent().transform(Functions.toStringFunction()));
    }

    public void testTransform_presentIdentity() {
        assertEquals(Optional.of("a"), Optional.of("a").transform(Functions.identity()));
    }

    public void testTransform_presentToString() {
        assertEquals(Optional.of("42"), Optional.of(42).transform(Functions.toStringFunction()));
    }

    public void testTransform_present_functionReturnsNull() {
        try {
            Optional<String> unused =
                    Optional.of("a")
                            .transform(
                                    new Function<String, String>() {
                                        @Override
                                        public String apply(String input) {
                                            return null;
                                        }
                                    });
            fail("Should throw if Function returns null.");
        } catch (NullPointerException expected) {
        }
    }

    public void testTransform_abssent_functionReturnsNull() {
        assertEquals(Optional.absent(),
                Optional.absent().transform(
                        new Function<Object, Object>() {
                            @Override
                            public Object apply(Object input) {
                                return null;
                            }
                        }));
    }

    public void testEqualsAndHashCode_present() {
        assertEquals(Optional.of("training"), Optional.of("training"));
        assertFalse(Optional.of("a").equals(Optional.of("b")));
        assertFalse(Optional.of("a").equals(Optional.absent()));
        assertEquals(Optional.of("training").hashCode(), Optional.of("training").hashCode());
    }

    public void testToString_absent() {
        assertEquals("Optional.absent()", Optional.absent().toString());
    }

    public void testToString_present() {
        assertEquals("Optional.of(training)", Optional.of("training").toString());
    }

    private static Optional<Integer> getSomeOptionalInt() {
        return Optional.of(1);
    }

    private static FluentIterable<? extends Number> getSomeNumbers() {
        return FluentIterable.from(ImmutableList.<Number>of());
    }

  /*
   * The following tests demonstrate the shortcomings of or() and test that the casting workaround
   * mentioned in the method Javadoc does in fact compile.
   */

    @SuppressWarnings("unused") // compilation test
    public void testSampleCodeError1() {
        Optional<Integer> optionalInt = getSomeOptionalInt();
        // Number value = optionalInt.or(0.5); // error
    }

    @SuppressWarnings("unused") // compilation test
    public void testSampleCodeError2() {
        FluentIterable<? extends Number> numbers = getSomeNumbers();
        Optional<? extends Number> first = numbers.first();
        // Number value = first.or(0.5); // error
    }

    @SuppressWarnings("unused") // compilation test
    public void testSampleCodeFine1() {
        Optional<Number> optionalInt = Optional.of((Number) 1);
        Number value = optionalInt.or(0.5); // fine
    }

    @SuppressWarnings("unused") // compilation test
    public void testSampleCodeFine2() {
        FluentIterable<? extends Number> numbers = getSomeNumbers();

        // Sadly, the following is what users will have to do in some circumstances.

        @SuppressWarnings("unchecked") // safe covariant cast
                Optional<Number> first = (Optional) numbers.first();
        Number value = first.or(0.5); // fine
    }
}
