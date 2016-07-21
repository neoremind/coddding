package net.neoremind.mycode.guava.primitives;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.UnsignedInteger;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class UnsignedIntegerTest extends TestCase {

    private static final ImmutableSet<Integer> TEST_INTS;
    private static final ImmutableSet<Long> TEST_LONGS;

    private static int force32(int value) {
        // GWT doesn't consistently overflow values to make them 32-bit, so we need to force it.
        return value & 0xffffffff;
    }

    static {
        ImmutableSet.Builder<Integer> testIntsBuilder = ImmutableSet.builder();
        ImmutableSet.Builder<Long> testLongsBuilder = ImmutableSet.builder();
        for (int i = -3; i <= 3; i++) {
            testIntsBuilder
                    .add(i)
                    .add(force32(Integer.MIN_VALUE + i))
                    .add(force32(Integer.MAX_VALUE + i));
            testLongsBuilder
                    .add((long) i)
                    .add((long) Integer.MIN_VALUE + i)
                    .add((long) Integer.MAX_VALUE + i)
                    .add((1L << 32) + i);
        }
        TEST_INTS = testIntsBuilder.build();
        TEST_LONGS = testLongsBuilder.build();
    }

    /**
     * 无符号是在0 - 2^32-1之间
     */
    public void testValueOfLong() {
        long min = 0;
        long max = (1L << 32) - 1;
        for (long value : TEST_LONGS) {
            boolean expectSuccess = value >= min && value <= max;
            try {
                assertEquals(value, UnsignedInteger.valueOf(value).longValue());
                assertTrue(expectSuccess);
            } catch (IllegalArgumentException e) {
                assertFalse(expectSuccess);
            }
        }
    }

}
