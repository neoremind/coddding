package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.junit.Test;

/**
 * @author zhangxu
 */
public class BufferTest {

    @Test
    public void testUnboundedFifoBuffer() {
        Buffer buffer = new UnboundedFifoBuffer();
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
        buffer.add("D");
        buffer.add("E");
        System.out.println(buffer.get());
        System.out.println(buffer.remove());
        System.out.println(buffer.remove());
        System.out.println(buffer);
    }

    @Test(expected = BufferOverflowException.class)
    public void testBoundedBufferNegative() {
        Buffer buffer = new BoundedFifoBuffer(2);
        buffer.add("A");
        buffer.add("B");
        buffer.add("C");
    }

    @Test
    public void testBoundedBuffer() {
        Buffer buffer = new BoundedFifoBuffer(2);
        buffer.add("A");
        buffer.add("B");
        try {
            buffer.add("C");
        } catch (BufferOverflowException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(buffer.remove()); //A
        buffer.add("D");
        System.out.println(buffer.get()); //B
        System.out.println(buffer.remove()); //B
        System.out.println(buffer); //[D]
    }

}
