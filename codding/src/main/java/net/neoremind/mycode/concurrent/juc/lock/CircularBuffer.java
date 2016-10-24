package net.neoremind.mycode.concurrent.juc.lock;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

/**
 * http://stackoverflow.com/questions/590069/how-would-you-code-an-efficient-circular-buffer-in-java-or-c-sharp
 *
 * @author zhangxu
 */
public class CircularBuffer<T> {

    private T[] buffer;

    private int tail;

    private int head;

    @SuppressWarnings("unchecked")
    public CircularBuffer(int n) {
        buffer = (T[]) new Object[n];
        tail = 0;
        head = 0;
    }

    public void add(T toAdd) {
        if (head != (tail - 1)) {
            buffer[head++] = toAdd;
        } else {
            throw new BufferOverflowException();
        }
        head = head % buffer.length;
    }

    public T get() {
        T t = null;
        int adjTail = tail > head ? tail - buffer.length : tail;
        if (adjTail < head) {
            t = (T) buffer[tail++];
            tail = tail % buffer.length;
        } else {
            throw new BufferUnderflowException();
        }
        return t;
    }

    public String toString() {
        return "CircularBuffer(size=" + buffer.length + ", head=" + head + ", tail=" + tail + ")";
    }

    public static void main(String[] args) {
        CircularBuffer<String> b = new CircularBuffer<String>(3);
        for (int i = 0; i < 10; i++) {
            System.out.println("Start: " + b);
            b.add("One");
            System.out.println("One: " + b);
            b.add("Two");
            System.out.println("Two: " + b);
            System.out.println("Got '" + b.get() + "', now " + b);

            b.add("Three");
            System.out.println("Three: " + b);
            // Test Overflow
//             b.add("Four");
//             System.out.println("Four: " + b);

            System.out.println("Got '" + b.get() + "', now " + b);
            System.out.println("Got '" + b.get() + "', now " + b);
            // Test Underflow
            // System.out.println("Got '" + b.get() + "', now " + b);

            // Back to start, let's shift on one
            b.add("Foo");
            b.get();
        }
    }
}
