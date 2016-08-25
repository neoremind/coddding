package net.neoremind.mycode.concurrent;

import org.junit.Test;

/**
 * Double Checked Locking on Singleton Class in Java
 * <p>
 *
 * @author zhangxu
 * @see http://wuchong.me/blog/2014/08/28/how-to-correctly-write-singleton-pattern/
 */
public class SingletonTest {

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 10000; i++) {
            Thread one = new Thread() {
                public void run() {
                    SingletonWithDoubleCheckedLockingUnsafeEdition s =
                            SingletonWithDoubleCheckedLockingUnsafeEdition.getInstance();
                    System.out.println(s.echo("abc"));
                }
            };
            one.start();
            one.join();
        }
    }

}

class SingletonWithDoubleCheckedLockingUnsafeEdition {

    private static volatile SingletonWithDoubleCheckedLockingUnsafeEdition INSTANCE = null;
    private static final Object LOCK = new Object();

    public static SingletonWithDoubleCheckedLockingUnsafeEdition getInstance() {
        if (INSTANCE == null) {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonWithDoubleCheckedLockingUnsafeEdition();
                }
            }
        }
        return INSTANCE;
    }

    public String echo(String s) {
        return s;
    }

    private SingletonWithDoubleCheckedLockingUnsafeEdition() {
    }

}
