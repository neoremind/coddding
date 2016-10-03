package net.neoremind.mycode.lang;

import org.junit.Test;

/**
 * Implements a thread-local storage, that is, a variable for which each thread has its own value. All threads share
 * the same ThreadLocal object, but each sees a different value when accessing it, and changes made by one thread do
 * not affect the other threads. The implementation supports null values.
 * <p>
 * 可以实现线程的本地存储机制，ThreadLocal变量是一个不同线程可以拥有不同值的变量。所有的线程可以共享同一个ThreadLocal
 * 对象，但是不同线程访问的时候可以取得不同的值，而且任意一个线程对它的改变不会影响其他线程。类实现是支持null值的（可以在set和get方法传递和访问null值）。
 * 概括来讲有三个特性：
 * <p>
 * - 不同线程访问时取得不同的值
 * <p>
 * - 任意线程对它的改变不影响其他线程
 * <p>
 * - 支持null
 *
 * @author zhangxu
 */
public class ThreadLocalTest {

    ThreadLocal<Long> longLocal = new ThreadLocal<Long>() {
        protected Long initialValue() {
            return Thread.currentThread().getId();
        }
    };
    ThreadLocal<String> stringLocal = new ThreadLocal<String>() {
        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

    @Test
    public void test() throws Exception {
        System.out.println(getLong());
        System.out.println(getString());

        Thread thread1 = new Thread() {
            public void run() {
                set();
                System.out.println(getLong());
                System.out.println(getString());
            }
        };
        thread1.start();
        thread1.join();

        System.out.println(getLong());
        System.out.println(getString());
    }
}
